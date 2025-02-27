package com.crezent.finalyearproject.transaction.presentation.deposit

sealed interface DepositScreenEvent {
    data class ShowError(val error: String) : DepositScreenEvent
    data class NavigateToPayStack(val authorizationUrl: String, val reference: String) :
        DepositScreenEvent
}