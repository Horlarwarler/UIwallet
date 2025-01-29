package com.crezent.finalyearproject.home.presentation

import com.crezent.finalyearproject.domain.util.RemoteError

sealed interface HomeScreenEvent {

    data class AuthenticatedUserError(val error: RemoteError) : HomeScreenEvent
}