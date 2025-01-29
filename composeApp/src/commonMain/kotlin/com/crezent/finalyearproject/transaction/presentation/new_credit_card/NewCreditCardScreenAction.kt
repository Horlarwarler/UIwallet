package com.crezent.finalyearproject.transaction.presentation.new_credit_card

sealed interface NewCreditCardScreenAction {
    data class OnHolderNameChange(val name: String) : NewCreditCardScreenAction
    data class OnCardNumberChange(val cardNumber: String) : NewCreditCardScreenAction
    data class OnExpirationDateChange(val expiration: String) : NewCreditCardScreenAction
    data class Cvv(val cvv: String) : NewCreditCardScreenAction
    data object Save : NewCreditCardScreenAction
}
