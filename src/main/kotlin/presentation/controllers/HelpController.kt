package org.novbicreate.presentation.controllers

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.annotations.CommandHandler
import eu.vendeli.tgbot.api.message.message
import eu.vendeli.tgbot.types.User
import org.novbicreate.controller.Commands.GET_TOP_EVENTS
import org.novbicreate.controller.Commands.HELP
import org.novbicreate.controller.Commands.START

class HelpController {
    @CommandHandler([HELP])
    suspend fun help(user: User, bot: TelegramBot) {
        val commandsList = listOf(
            "$HELP - список доступных комманд",
            "$START - запустить бота",
            "$GET_TOP_EVENTS - получить топ 10 поисковых запросов"
            )
        var message = "Вот список доступных комманд:"
        commandsList.forEach { command ->
            message += "\n$command"
        }
        message(message).send(user, bot)
    }
}