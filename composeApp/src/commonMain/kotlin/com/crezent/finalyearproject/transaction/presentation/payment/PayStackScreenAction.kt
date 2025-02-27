package com.crezent.finalyearproject.transaction.presentation.payment

sealed interface PayStackScreenAction {
    data object VerifyPayment : PayStackScreenAction
}