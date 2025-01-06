package com.crezent.finalyearproject.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ServerResponse<T>(
    val data: T? = null,
    val message: String? = null
)

fun String.toServerResponse(): ServerResponse<Unit> {
    return ServerResponse(
        message = this

    )
}
