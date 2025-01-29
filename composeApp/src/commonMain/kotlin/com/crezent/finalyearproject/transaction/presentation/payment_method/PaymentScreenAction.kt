package com.crezent.finalyearproject.transaction.presentation.payment_method

import com.crezent.finalyearproject.core.domain.model.Card
import com.crezent.finalyearproject.core.presentation.component.NumberInputType
import com.crezent.finalyearproject.transaction.presentation.payment_method.util.PaymentMethod

sealed interface PaymentScreenAction {

    data class OnSelectCurrentCard(val card: Card?) : PaymentScreenAction

    data class OnCvvEnter(val inputType: NumberInputType) : PaymentScreenAction

    data object VerifyCvv : PaymentScreenAction

    data class SelectPaymentMethod(val paymentMethod: PaymentMethod) : PaymentScreenAction


}