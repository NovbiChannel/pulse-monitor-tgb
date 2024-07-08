package org.novbicreate.presentation.controllers

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.annotations.CommandHandler
import eu.vendeli.tgbot.api.media.sendDocument
import eu.vendeli.tgbot.api.message.message
import eu.vendeli.tgbot.types.User
import org.novbicreate.controller.Commands.GET_TOP_EVENTS
import org.novbicreate.domain.ApiRepository

class EventController {
    @CommandHandler([GET_TOP_EVENTS])
    suspend fun getTopSearchQuery(user: User, bot: TelegramBot) {
        val repository = ApiRepository(
            sendMessage = { msg ->
                message(msg).send(user, bot)
            },
            sendDocument = { file ->
                sendDocument(file).send(user, bot)
            }
        )
        repository.getTopEventSearchQuery()
    }
}