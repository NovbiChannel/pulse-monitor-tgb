package org.novbicreate.presentation.controllers

import eu.vendeli.tgbot.annotations.CommandHandler
import org.novbicreate.controller.ControllerRoutes.BUG_REPORT_ROUTE

class ErrorSubscribeController {
    @CommandHandler.CallbackQuery([BUG_REPORT_ROUTE])
    suspend fun subscribeToBugReports() {
        repository.subscribeToErrorStream()
    }
}