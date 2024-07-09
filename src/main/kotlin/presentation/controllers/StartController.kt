package org.novbicreate.presentation.controllers

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.annotations.CommandHandler
import eu.vendeli.tgbot.api.message.message
import eu.vendeli.tgbot.types.User
import org.novbicreate.controller.Commands.START
import org.novbicreate.controller.CallbackRoutes.BUG_REPORT_ROUTE
import org.novbicreate.presentation.common.gettingErrorButton
import org.novbicreate.presentation.common.welcomeMessage

class StartController {
    @CommandHandler([START])
    suspend fun start(user: User, bot: TelegramBot) {
        message(welcomeMessage).inlineKeyboardMarkup { gettingErrorButton callback BUG_REPORT_ROUTE }.send(user, bot)
    }
}