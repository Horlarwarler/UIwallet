package com.crezent.finalyearproject.transaction.presentation.transaction_details

import androidx.lifecycle.ViewModel
import com.crezent.finalyearproject.core.presentation.SharedData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart

class TransactionDetailsViewmodel : ViewModel() {

    private val transaction = SharedData.currentTransaction.value!!
    private val loggedInUser = SharedData.loggedInUser.value!!
    private val _state = MutableStateFlow(
        TransactionDetailsScreenState(
            transaction = transaction,
            name = "${loggedInUser.firstName} ${loggedInUser.lastName}",
            email = loggedInUser.emailAddress
        )

    )

    init {
        println("Transaction $transaction")
    }

    val state = _state.asStateFlow()

    fun handleScreenAction(
        action: TransactionDetailsAction
    ) {

        when (action) {
            TransactionDetailsAction.PrintReceipt -> printReceipt()
        }
    }

    private fun printReceipt() {
        //
    }
}