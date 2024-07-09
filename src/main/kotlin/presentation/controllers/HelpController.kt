package org.novbicreate.presentation.controllers

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.annotations.CommandHandler
import eu.vendeli.tgbot.types.User
import org.novbicreate.controller.Commands.HELP
import org.novbicreate.domain.ApiRepository

class HelpController {
    @CommandHandler([HELP])
    suspend fun help(user: User, bot: TelegramBot) {
        val repository = ApiRepository(user, bot)
        repository.sendAllCommands()
    }
}