package org.novbicreate.domain

import eu.vendeli.tgbot.types.internal.ImplicitFile
import eu.vendeli.tgbot.utils.toImplicitFile
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.isActive
import org.novbicreate.domain.ApiRoutes.ERROR_SUBSCRIBE
import org.novbicreate.domain.ApiRoutes.GET_TOP_EVENTS
import org.novbicreate.domain.ApiRoutes.HOST
import org.novbicreate.domain.ApiRoutes.PORT
import org.novbicreate.presentation.common.*
import java.io.File
import java.net.ConnectException
import java.util.concurrent.TimeoutException

class ApiRepositoryImpl(private val client: HttpClient): ApiRepository {
    override suspend fun handleErrorStreamSubscription(
        sendMessage: suspend (String) -> Unit,
        sendFile: suspend (ImplicitFile) -> Unit
    ) {
        return try {
            client.webSocket(method = HttpMethod.Get, host = HOST, port = PORT, path = ERROR_SUBSCRIBE) {
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
    override suspend fun generateEventListMessage(): String {
        return try {
            val url = "http://$HOST:$PORT$GET_TOP_EVENTS"
            val response = client.get(url).body<List<String>>()
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