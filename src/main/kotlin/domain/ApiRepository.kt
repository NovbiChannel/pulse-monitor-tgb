package org.novbicreate.domain

import eu.vendeli.tgbot.types.internal.ImplicitFile

interface ApiRepository {
    suspend fun handleErrorStreamSubscription(
        sendMessage: suspend (String) -> Unit,
        sendFile: suspend (ImplicitFile) -> Unit
    )
    suspend fun generateEventListMessage(): String
}