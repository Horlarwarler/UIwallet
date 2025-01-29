package com.crezent.finalyearproject.transaction.presentation.payment_method

import com.crezent.finalyearproject.domain.util.RemoteError

sealed interface PaymentMethodEvent {

    data class CvvVerificationFailure(val error: RemoteError) : PaymentMethodEvent
}