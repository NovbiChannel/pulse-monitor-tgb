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
import org.novbicreate.presentation.common.*
import java.io.File
import java.net.ConnectException
import java.util.concurrent.TimeoutException

class ApiRepository {
    private val _client = HttpClient(CIO) {
        install(WebSockets)
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }
    suspend fun handleErrorStreamSubscription(
        sendMessage: suspend (String) -> Unit,
        sendFile: suspend (ImplicitFile) -> Unit
    ) {
        return try {
            _client.webSocket(method = HttpMethod.Get, host = HOST, port = PORT, path = ERROR_SUBSCRIBE) {
                sendMessage(subscriptionMessage)
                while (isActive) {
                    when (val frame = incoming.receive()) {
                        is Frame.Text -> {
                            sendMessage(frame.readText())
                        }
                        is Frame.Binary -> {
                            sendFile(generateImplicitFile(frame.readBytes()))
                        }
                        else -> {}
                    }
                }
            }
        } catch (e: Exception) { sendMessage(handleError(e)) }
    }
    suspend fun generateEventListMessage(): String {
        return try {
            val url = "http://$HOST:$PORT$GET_TOP_EVENTS"
            val response = _client.get(url).body<List<String>>()
            var message = titleListEvents
            response.forEach { event -> message += "\n$event" }
            message
        } catch (e: Exception) { handleError(e) }
    }
    private fun handleError(e: Exception): String {
        e.printStackTrace()
        return when (e) {
            is ConnectException -> connectErrorMessage
            is TimeoutException -> timeoutErrorMessage
            else -> unknownErrorMessage
        }
    }
    private fun generateImplicitFile(byteArray: ByteArray): ImplicitFile {
        val file = File("file.txt")
        file.writeBytes(byteArray)
        return file.toImplicitFile("errors_${System.currentTimeMillis()}")
    }
}