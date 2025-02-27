package com.crezent.finalyearproject.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InitiateTransactionBody(
    val amount: String,
    val email: String,
   // val reference: String,
    @SerialName("callback_url")
    val callBackUrl: String,// = "http://192.168.81.175:8080/callback",
    @SerialName("metadata")
    val metaData: InitiateMetaData// = InitiateMetaData(cancelAction = "http://192.168.81.175:8080/cancel-payment")
)

@Serializable
data class InitiateMetaData(
    @SerialName("cancel_action")
    val cancelAction: String,
)