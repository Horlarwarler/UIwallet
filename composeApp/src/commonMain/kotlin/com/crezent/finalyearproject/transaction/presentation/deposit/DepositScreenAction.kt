package com.crezent.finalyearproject.transaction.presentation.deposit

import com.crezent.finalyearproject.core.presentation.component.NumberInputType

sealed interface DepositScreenAction {

    data class EditInput(val numberInputType: NumberInputType) : DepositScreenAction
    data class EditCurrentIndex(val index: Int) : DepositScreenAction

    data object OpenPaymentPage : DepositScreenAction
}