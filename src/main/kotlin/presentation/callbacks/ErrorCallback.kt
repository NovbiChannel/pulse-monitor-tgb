package org.novbicreate.presentation.callbacks

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.annotations.CommandHandler
import eu.vendeli.tgbot.types.User
import org.novbicreate.controller.ControllerRoutes.BUG_REPORT_ROUTE
import org.novbicreate.domain.ApiRepository

class ErrorCallback {
    @CommandHandler.CallbackQuery([BUG_REPORT_ROUTE])
    suspend fun subscribeToBugReports(user: User, bot: TelegramBot) {
        val repository = ApiRepository(user, bot)
        repository.subscribeToErrorStream()
    }
}