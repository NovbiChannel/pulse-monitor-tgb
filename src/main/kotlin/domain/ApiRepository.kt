package org.novbicreate.domain

import eu.vendeli.tgbot.types.internal.ImplicitFile
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
import org.novbicreate.domain.ApiRoutes.ERROR_SUBSCRIBE
import org.novbicreate.domain.ApiRoutes.GET_TOP_EVENTS
import org.novbicreate.domain.ApiRoutes.HOST
import org.novbicreate.domain.ApiRoutes.PORT
import org.novbicreate.domain.model.EventReceive
import java.io.File
import java.net.ConnectException
import java.util.concurrent.TimeoutException

class ApiRepository(
    private val sendMessage: suspend (String) -> Unit,
    private val sendDocument: suspend (ImplicitFile.InpFile) -> Unit
) {
    private val client = HttpClient(CIO) {
        install(WebSockets)
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }
    suspend fun subscribeToErrorStream() {
        try {
            client.webSocket(method = HttpMethod.Get, host = HOST, port = PORT, path = ERROR_SUBSCRIBE) {
                while (isActive) {
                    when (val frame = incoming.receive()) {
                        is Frame.Text -> {
                            val text = frame.readText()
                            sendMessage(text)
                        }
                        is Frame.Binary -> {
                            val fileBytes = frame.readBytes()
                            val file = File("file.txt")
                            file.writeBytes(fileBytes)
                            sendDocument(file.toImplicitFile("errors_${System.currentTimeMillis()}"))
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
            val response = client.get(url).body<List<String>>()
            var message = "Все события:"
            response.forEach { event ->
                message += "\n$event"
            }
            sendMessage(message)
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
        sendMessage(message)
    }
}