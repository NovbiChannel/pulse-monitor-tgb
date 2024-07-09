package org.novbicreate.controller

object Commands {
    const val HELP = "/help"
    const val START = "/start"
    const val TOP_EVENTS = "/top_events"
    val commandsList = listOf(
        "$HELP - список доступных команд",
        "$START - запустить бота",
        "$TOP_EVENTS - получить топ 10 поисковых запросов"
    )
}