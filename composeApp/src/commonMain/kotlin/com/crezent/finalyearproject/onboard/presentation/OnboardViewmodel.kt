package com.crezent.finalyearproject.onboard.presentation

import androidx.lifecycle.ViewModel
import com.crezent.finalyearproject.core.domain.preference.SharedPreference

class OnboardViewmodel(
    private val sharedPreference: SharedPreference
) : ViewModel() {

    fun saveOnboarding() {
        sharedPreference.editShowOnboarding(false)
    }
}