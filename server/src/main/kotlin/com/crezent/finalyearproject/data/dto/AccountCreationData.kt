package com.crezent.finalyearproject.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class AccountCreationData(
    val depositCode :String,
    val name :String,
    val refId :String,
    val emailOrPhone:String,
    val accountType:String,

)
