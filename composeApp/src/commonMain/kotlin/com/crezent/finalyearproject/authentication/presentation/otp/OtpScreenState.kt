package com.crezent.finalyearproject.authentication.presentation.otp

data class OtpScreenState(
    val otp: String = "",
    val recoveredEmail :String = "mikailramadan999@gmail.com",
    val wrongOtp :Boolean = false

)