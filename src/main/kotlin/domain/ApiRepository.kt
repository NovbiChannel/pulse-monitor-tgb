package org.novbicreate.domain

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.api.media.sendDocument
import eu.vendeli.tgbot.api.message.message
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.utils.toImplicitFile
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.websocket.*
import kotlinx.coroutines.isActive
import kotlinx.serialization.json.Json
import org.novbicreate.controller.Commands.HELP
import org.novbicreate.controller.Commands.START
import org.novbicreate.controller.Commands.TOP_EVENTS
import org.novbicreate.domain.ApiRoutes.ERROR_SUBSCRIBE
import org.novbicreate.domain.ApiRoutes.GET_TOP_EVENTS
import org.novbicreate.domain.ApiRoutes.HOST
import org.novbicreate.domain.ApiRoutes.PORT
import java.io.File
import java.net.ConnectException
import java.util.concurrent.TimeoutException

class ApiRepository(
    private val user: User,
    private val bot: TelegramBot
) {
    private val _client = HttpClient(CIO) {
        install(WebSockets)
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }
    suspend fun subscribeToErrorStream() {
        try {
            _client.webSocket(method = HttpMethod.Get, host = HOST, port = PORT, path = ERROR_SUBSCRIBE) {
                val message = "Отлично, вы подписались на рассылку уведомлений об аномальных проблемах приложения."
                message(message).send(user, bot)
                while (isActive) {
                    when (val frame = incoming.receive()) {
                        is Frame.Text -> {
                            message(frame.readText()).send(user, bot)
                        }
                        is Frame.Binary -> {
                            sendFile(frame.readBytes())
                        }
                        else -> {}
                    }
                }
            }
        } catch (e: Exception) {
            errorHandler(e)
        }
    }
    suspend fun getTopEventSearchQuery() {
        try {
            val url = "http://$HOST:$PORT$GET_TOP_EVENTS"
            val response = _client.get(url).body<List<String>>()
            var message = "Все события:"
            response.forEach { event ->
                message += "\n$event"
            }
            message(message).send(user, bot)
        } catch (e: Exception) {
            errorHandler(e)
        }
    }
    suspend fun sendAllCommands() {
        try {
            val commandsList = listOf(
                "$HELP - список доступных комманд",
                "$START - запустить бота",
                "$TOP_EVENTS - получить топ 10 поисковых запросов"
            )
            var message = "Вот список доступных комманд:"
            commandsList.forEach { command ->
                message += "\n$command"
            }
            message(message).send(user, bot)
        } catch (e: Exception) {
            errorHandler(e)
        }
    }
    private suspend fun errorHandler(e: Exception) {
        e.printStackTrace()
        val message = when (e) {
            is ConnectException -> "Связь с сервером потеряна, попробуйте поже"
            is TimeoutException -> "Время ожидания ответа от сервера истекло. Пожалуйста, попробуйте еще раз."
            else -> "Упс... Что-то пошло не так. Попробуйте позже"
        }
        message(message).send(user, bot)
    }
    private suspend fun sendFile(byteArray: ByteArray) {
        val file = File("file.txt")
        file.writeBytes(byteArray)
        sendDocument(file.toImplicitFile("errors_${System.currentTimeMillis()}")).send(user, bot)
        file.delete()
    }
}