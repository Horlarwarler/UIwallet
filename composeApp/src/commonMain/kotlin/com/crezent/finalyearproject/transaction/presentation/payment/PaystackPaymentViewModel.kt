package com.crezent.finalyearproject.transaction.presentation.payment

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crezent.finalyearproject.core.presentation.SharedData
import com.crezent.finalyearproject.domain.util.Result
import com.crezent.finalyearproject.transaction.domain.TransactionRepo
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.log

class PayStackPaymentViewModel(
    private val transactionRepo: TransactionRepo,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(PayStackPaymentScreenState())
    val state = _state.asStateFlow()

    private val _channel = Channel<PaymentScreenEvent>()

    val channel = _channel.receiveAsFlow()
    val loggedInUserMail = SharedData.loggedInUser.value?.emailAddress ?: "newuser@gmail.com"


    private val reference = savedStateHandle.get<String>("reference")

    fun handleScreenEvent(
        event: PayStackScreenAction
    ) {
        when (event) {
            is PayStackScreenAction.VerifyPayment -> verifyPayment()
        }
    }

    private fun verifyPayment() {
        if (reference == null) {
            return
        }
        _state.update {
            it.copy(isLoading = true)
        }
        println("Payment reference is $reference , logged in email is $loggedInUserMail")
        viewModelScope.launch {
            val result = transactionRepo.verifyPayment(
                emailAddress = loggedInUserMail,
                reference = reference
            )
//            reference = reference,
//            emailAddress = loggedInUserMail


            _state.update {
                it.copy(isLoading = false)
            }
            if (result is Result.Error) {
                _channel.send(PaymentScreenEvent.PaymentError(result.error))
                return@launch
            }
            val data = result as Result.Success

            _channel.send(PaymentScreenEvent.PaymentSuccessful(data.data))


        }
    }
}