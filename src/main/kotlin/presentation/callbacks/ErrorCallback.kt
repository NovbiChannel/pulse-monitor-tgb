package org.novbicreate.presentation.callbacks

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.annotations.CommandHandler
import eu.vendeli.tgbot.api.media.sendDocument
import eu.vendeli.tgbot.api.message.message
import eu.vendeli.tgbot.types.User
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.novbicreate.controller.CallbackRoutes.BUG_REPORT_ROUTE
import org.novbicreate.domain.ApiRepository

class ErrorCallback: KoinComponent {
    private val _repository: ApiRepository by inject()

    @CommandHandler.CallbackQuery([BUG_REPORT_ROUTE])
    suspend fun subscribeToBugReports(user: User, bot: TelegramBot) {
        _repository.handleErrorStreamSubscription(
            sendMessage = { msg -> message(msg).send(user, bot) },
            sendFile = { file -> sendDocument(file).send(user, bot) }
        )
    }
}