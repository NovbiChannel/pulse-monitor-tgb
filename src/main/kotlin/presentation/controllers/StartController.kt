package org.novbicreate.presentation.controllers

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.annotations.CommandHandler
import eu.vendeli.tgbot.api.message.message
import eu.vendeli.tgbot.types.User
import org.novbicreate.controller.Commands.START
import org.novbicreate.controller.ControllerRoutes.BUG_REPORT_ROUTE
import org.novbicreate.domain.ApiRepository

class StartController {
    @CommandHandler([START])
    suspend fun start(user: User, bot: TelegramBot) {
        val message = "Привет! \uD83D\uDC4B Я ваш бот-детектор аномалий на сервере. Я буду следить за состоянием ваших серверов и клиентов, а так-же уведомлять вас о любой необычной активности. Будьте в безопасности и в курсе! \uD83D\uDEA8 Подпишись на интересующие тебя уведомления!"
        message(message).inlineKeyboardMarkup {
            "Получать ошибки" callback BUG_REPORT_ROUTE
        }.send(user, bot)
    }
}