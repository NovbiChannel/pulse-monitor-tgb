package org.novbicreate.domain

object ApiRoutes {
    val HOST = System.getenv("HOST") ?: "localhost"
    val PORT = System.getenv("PORT").toInt()
    const val ERROR_SUBSCRIBE = "/error_subscribe"
    const val GET_TOP_EVENTS = "/top_events"
}