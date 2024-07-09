package org.novbicreate.presentation.controllers

import eu.vendeli.tgbot.annotations.CommandHandler
import org.novbicreate.controller.Commands.GET_TOP_EVENTS

class EventController {
    @CommandHandler([GET_TOP_EVENTS])
    suspend fun getTopSearchQuery() {
        repository.getTopEventSearchQuery()
    }
}