package com.crezent.finalyearproject.transaction.presentation.transaction_details

sealed interface TransactionDetailsAction {
    data object PrintReceipt : TransactionDetailsAction
}