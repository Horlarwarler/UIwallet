package com.crezent.finalyearproject.transaction.presentation.new_credit_card

data class NewCreditCardScreenState(
    val isLoading: Boolean = false,
    val holderName: String? = null,
    val cardNumber: String? = null,
    val expirationDate: String? = null,
    val cvv: String? = null,
    val cardHolderNameError: List<String> = emptyList(),
    val cardNumberError: List<String> = emptyList(),
    val expirationDateError: List<String> = emptyList(),
    val cvvError: List<String> = emptyList()

)
