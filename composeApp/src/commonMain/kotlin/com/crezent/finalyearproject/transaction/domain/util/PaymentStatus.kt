package com.crezent.finalyearproject.transaction.domain.util

sealed interface PaymentStatus {
    data class Successful(val reference: String) : PaymentStatus
    data object Cancel : PaymentStatus
    data class Failed(val error: String) : PaymentStatus
}