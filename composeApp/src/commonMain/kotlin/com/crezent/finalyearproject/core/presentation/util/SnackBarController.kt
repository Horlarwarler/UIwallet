package com.crezent.finalyearproject.core.presentation.util

import androidx.compose.material3.SnackbarDuration
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow


sealed interface SnackBarEvent {
    data class ShowSnackBar(
        val message: String,
        val snackBarAction: SnackBarAction? = null,
        val duration: SnackbarDuration = SnackbarDuration.Short,
        val dismissAction: Boolean = false
    ) : SnackBarEvent

    data object DismissSnackBar : SnackBarEvent
}

data class SnackBarAction(
    val name: String,
    val action: () -> Unit
)

object SnackBarController {

    private val _snackBarEvent = Channel<SnackBarEvent>()

    val snackBarEvent = _snackBarEvent.receiveAsFlow()

    suspend fun sendEvent(snackBarEvent: SnackBarEvent) {
        _snackBarEvent.send(element = snackBarEvent)
    }


}