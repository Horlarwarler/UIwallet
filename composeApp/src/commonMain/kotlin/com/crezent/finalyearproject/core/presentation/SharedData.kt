package com.crezent.finalyearproject.core.presentation

import com.crezent.finalyearproject.core.domain.model.User
import com.crezent.finalyearproject.data.dto.LoggedInUser
import kotlinx.coroutines.flow.MutableStateFlow

object SharedData {

    var loggedInUser = MutableStateFlow<User?>(null)

    fun editLoggedInUser(user: User) {
        loggedInUser.value = user
    }
}