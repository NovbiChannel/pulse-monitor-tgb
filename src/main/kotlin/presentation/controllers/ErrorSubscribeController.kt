package org.novbicreate.presentation.controllers

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.annotations.CommandHandler
import eu.vendeli.tgbot.api.media.sendDocument
import eu.vendeli.tgbot.api.message.message
import eu.vendeli.tgbot.types.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.novbicreate.controller.ControllerRoutes.BUG_REPORT_ROUTE
import org.novbicreate.domain.ApiRepository

class ErrorSubscribeController {
    @CommandHandler.CallbackQuery([BUG_REPORT_ROUTE])
    suspend fun subscribeToBugReports(user: User, bot: TelegramBot) {
        val message = "Отлично, вы подписались на рассылку уведомлений об аномальных проблемах приложения."
        val repository = ApiRepository(
            sendMessage = { msg ->
                message(msg).send(user, bot)
            },
            sendDocument = { file ->
                sendDocument(file).send(user, bot)
            }
        )
        message(message).send(user, bot)
        repository.subscribeToErrorStream()
    }
}