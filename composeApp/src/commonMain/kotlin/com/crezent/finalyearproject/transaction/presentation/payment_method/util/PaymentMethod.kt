package com.crezent.finalyearproject.transaction.presentation.payment_method.util

import com.crezent.finalyearproject.core.domain.model.Card


interface PaymentMethod {

    data class CardPayment(val card: Card) : PaymentMethod
    data object UssdPayment : PaymentMethod
    data object BankTransfer : PaymentMethod

}