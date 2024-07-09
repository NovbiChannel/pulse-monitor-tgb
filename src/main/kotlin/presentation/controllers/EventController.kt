package org.novbicreate.presentation.controllers

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.annotations.CommandHandler
import eu.vendeli.tgbot.api.message.message
import eu.vendeli.tgbot.types.User
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.novbicreate.controller.Commands.TOP_EVENTS
import org.novbicreate.domain.ApiRepository

class EventController: KoinComponent {
    private val _repository: ApiRepository by inject()

    @CommandHandler([TOP_EVENTS])
    suspend fun getTopSearchQuery(user: User, bot: TelegramBot) {
        val message = _repository.generateEventListMessage()
        message(message).send(user, bot)
    }
}