package com.crezent.finalyearproject.splash.presesentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crezent.finalyearproject.core.data.security.encryption.KeyPairGenerator
import com.crezent.finalyearproject.core.domain.preference.EncryptedSharePreference
import com.crezent.finalyearproject.core.domain.preference.SharedPreference
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SplashViewModel(
    private val sharePreference: SharedPreference,
    private val encryptedSharePreference: EncryptedSharePreference,
    private val  keyPairGenerator: KeyPairGenerator
) : ViewModel() {

    private val _splashScreenState = MutableStateFlow(SplashScreenState())


    val channel = Channel<SplashScreenEvent>()


    val splashScreenState = _splashScreenState
        .asStateFlow()

    init {
        navigateUser()
        generateKeyPair()
    }

    private fun generateKeyPair() {
        keyPairGenerator.generateKeyStore()
    }


    private fun navigateUser() {
        viewModelScope.launch {
            delay(3000L)

            val navigateToOnboard = sharePreference.showOnboarding
            if (navigateToOnboard) {
                channel.send(SplashScreenEvent.NavigateToOnboard)
                cancel()
                return@launch
            }

            val userSetPin = encryptedSharePreference.userPin != null

            if (userSetPin) {
                channel.send(SplashScreenEvent.NavigateToPinScreen)
                return@launch
            }

            channel.send(SplashScreenEvent.NavigateToSignInScreen)

        }

    }

    override fun onCleared() {
        channel.close()
    }

}