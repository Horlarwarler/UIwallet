package com.crezent.finalyearproject.authentication.presentation.otp

data class OtpScreenState(
    val otp: String = "",
    val emailAddress: String = "mikailramadan999@gmail.com",
    val wrongOtp: Boolean = false,
    val isLoading: Boolean = false,
    val requestNewButtonEnable: Boolean = false,
    val remainingTime: String = "02:00"

)