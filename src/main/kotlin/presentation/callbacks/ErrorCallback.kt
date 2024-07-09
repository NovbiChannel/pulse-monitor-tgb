package org.novbicreate.presentation.callbacks

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.annotations.CommandHandler
import eu.vendeli.tgbot.api.media.sendDocument
import eu.vendeli.tgbot.api.message.message
import eu.vendeli.tgbot.types.User
import org.novbicreate.controller.ControllerRoutes.BUG_REPORT_ROUTE
import org.novbicreate.domain.ApiRepository

class ErrorCallback {
    @CommandHandler.CallbackQuery([BUG_REPORT_ROUTE])
    suspend fun subscribeToBugReports(user: User, bot: TelegramBot) {
        val repository = ApiRepository()
        repository.handleErrorStreamSubscription(
            sendMessage = { msg -> message(msg).send(user, bot) },
            sendFile = { file -> sendDocument(file).send(user, bot) }
        )
    }
}