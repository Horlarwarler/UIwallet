package com.crezent.finalyearproject.transaction.presentation.payment

import com.crezent.finalyearproject.core.domain.model.Transaction
import com.crezent.finalyearproject.domain.util.RemoteError

sealed interface PaymentScreenEvent {
    data class PaymentSuccessful(val transaction: Transaction) : PaymentScreenEvent

    data class PaymentError(val error: RemoteError) : PaymentScreenEvent
}