package com.crezent.finalyearproject.transaction.presentation.payment_method

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crezent.finalyearproject.core.domain.model.Card
import com.crezent.finalyearproject.core.presentation.SharedData
import com.crezent.finalyearproject.core.presentation.component.NumberInputType
import com.crezent.finalyearproject.domain.util.Result
import com.crezent.finalyearproject.domain.util.toErrorMessage
import com.crezent.finalyearproject.transaction.domain.TransactionRepo
import com.crezent.finalyearproject.transaction.presentation.payment_method.util.PaymentMethod
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PaymentScreenViewModel(
    private val transactionRepo: TransactionRepo
) : ViewModel() {

    private val _paymentEventChannel = Channel<PaymentMethodEvent>()
    val paymentEventChannel = _paymentEventChannel.receiveAsFlow()

    private val cards = SharedData.loggedInUser.value?.connectedCards ?: emptyList()

    private val _paymentMethodState = MutableStateFlow(
        PaymentMethodState(
            cards = SharedData.loggedInUser.value?.connectedCards ?: emptyList()
        )
    )

    val paymentMethodState = _paymentMethodState
        .onStart {
            println("Cards $cards")
            getUpdatedUser()
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed((5000)), _paymentMethodState.value)


    fun handlePaymentScreenAction(action: PaymentScreenAction) {
        when (action) {
            is PaymentScreenAction.OnCvvEnter -> handleCvvEnter(action.inputType)
            is PaymentScreenAction.OnSelectCurrentCard -> onSelectCurrentCard(action.card)
            is PaymentScreenAction.SelectPaymentMethod -> selectPaymentMethod(action.paymentMethod)
            PaymentScreenAction.VerifyCvv -> verifyCvv()
        }
    }

    private fun handleCvvEnter(
        inputType: NumberInputType
    ) {
        val cvvText = paymentMethodState.value.cvvText
        val newInput = when (inputType) {
            NumberInputType.BackSpace -> if (cvvText.isNullOrBlank()) return else {
                cvvText.dropLast(1)
            }

            NumberInputType.Dot -> return
            is NumberInputType.Number -> if (cvvText.isNullOrBlank()) "${inputType.number}" else if (cvvText.length >= 3) {
                return
            } else {
                cvvText + "${inputType.number}"
            }
        }
        _paymentMethodState.update {
            it.copy(
                cvvText = newInput
            )
        }
    }

    private fun onSelectCurrentCard(card: Card?) {
        _paymentMethodState.update {
            it.copy(
                cvvText = null,
                currentSelectedCard = card
            )
        }
    }

    private fun selectPaymentMethod(
        method: PaymentMethod
    ) {
        _paymentMethodState.update {
            it.copy(
                selectedPayment = method,
                currentSelectedCard = null
            )
        }
    }

    fun getUpdatedUser() {
        viewModelScope.launch {
            SharedData.loggedInUser.collectLatest { user ->
                if (user == null) {
                    return@collectLatest
                }
                val cards = user.connectedCards
                _paymentMethodState.value = paymentMethodState.value.copy(
                    cards = cards
                )
            }
        }
    }

    private fun verifyCvv() {
        val currentCard = paymentMethodState.value.currentSelectedCard

        if (currentCard == null || paymentMethodState.value.cvvText.isNullOrBlank() || paymentMethodState.value.cvvText!!.length != 3) {
            return
        }

        //will do network verification here

        viewModelScope.launch(Dispatchers.IO) {
            val cards =
                _paymentMethodState.value.verifiedCard
            _paymentMethodState.update {
                it.copy(isLoading = true)
            }
            val result = transactionRepo.verifyCvv(
                cvv = paymentMethodState.value.cvvText!!,
                id = paymentMethodState.value.currentSelectedCard!!.cardId
            )
            _paymentMethodState.update {
                it.copy(
                    isLoading = false, currentSelectedCard = null,
                    cvvText = null
                )
            }
            if (result is Result.Error) {
                val errorMessage = result.error
                _paymentEventChannel.send(PaymentMethodEvent.CvvVerificationFailure(errorMessage))
                return@launch
            }

            cards[currentCard.cardId] = true

            _paymentMethodState.update {
                it.copy(
                    verifiedCard = cards,
                    selectedPayment = PaymentMethod.CardPayment(card = currentCard),

                    )
            }
        }

    }
}
