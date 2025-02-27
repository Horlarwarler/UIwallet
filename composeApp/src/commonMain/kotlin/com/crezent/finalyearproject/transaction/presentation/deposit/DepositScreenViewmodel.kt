package com.crezent.finalyearproject.transaction.presentation.deposit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crezent.finalyearproject.core.domain.util.ApiRoute
import com.crezent.finalyearproject.core.presentation.SharedData
import com.crezent.finalyearproject.core.presentation.component.NumberInputType
import com.crezent.finalyearproject.data.dto.InitiateMetaData
import com.crezent.finalyearproject.data.dto.InitiateTransactionBody
import com.crezent.finalyearproject.domain.util.Result
import com.crezent.finalyearproject.domain.util.toErrorMessage
import com.crezent.finalyearproject.transaction.FundingSourceDto.UssdPayment
import com.crezent.finalyearproject.transaction.TransactionDto
import com.crezent.finalyearproject.transaction.TransactionStatus
import com.crezent.finalyearproject.transaction.TransactionType
import com.crezent.finalyearproject.transaction.domain.TransactionRepo
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DepositScreenViewmodel(
    private val transactionRepo: TransactionRepo,
) : ViewModel() {
    private val _depositScreenState = MutableStateFlow(DepositScreenState())

    val depositScreenState = _depositScreenState.asStateFlow()

    private val _depositEventChannel = Channel<DepositScreenEvent>()
    val depositEventChannel = _depositEventChannel.receiveAsFlow()
    val loggedInUserMail = SharedData.loggedInUser.value?.emailAddress ?: "newuser@gmail.com"


    fun handleScreenAction(action: DepositScreenAction) {
        when (action) {
            is DepositScreenAction.EditInput -> {
                editScreenAction(action.numberInputType)
            }

            is DepositScreenAction.EditCurrentIndex -> editCurrentIndex(action.index)
            DepositScreenAction.OpenPaymentPage -> openPaymentPage()
        }
    }

    private fun editScreenAction(input: NumberInputType) {
        val state = depositScreenState.value
        val existingInput = state.amount.orEmpty()
        val currentIndex = state.currentIndex

        val newInput: String = when (input) {
            NumberInputType.Dot -> handleDotInput(existingInput, currentIndex)
            NumberInputType.BackSpace -> handleBackspaceInput(existingInput, currentIndex)
            is NumberInputType.Number -> handleNumberInput(
                existingInput,
                currentIndex,
                input.number
            )
        } ?: return

        _depositScreenState.update {
            it.copy(
                amount = newInput,
                currentIndex = currentIndex + when (input) {
                    NumberInputType.Dot, is NumberInputType.Number -> 1
                    NumberInputType.BackSpace -> if (currentIndex > 0) -1 else 0
                }
            )
        }
    }


    private fun handleDotInput(existingInput: String, currentIndex: Int): String? {
        // Allow adding a dot only if it doesn't exist
        if (existingInput.contains(".")) return null
        return "$existingInput."
    }

    private fun handleBackspaceInput(existingInput: String, currentIndex: Int): String? {
        // Ensure there's something to delete
        println("Current deleted index $currentIndex")
        if (existingInput.isBlank()) return null
        if (existingInput.length == 1 || existingInput.length == currentIndex) {
            return existingInput.dropLast(1)
        }

        return existingInput.removeRange(currentIndex, currentIndex + 1)
    }

    private fun handleNumberInput(existingInput: String, currentIndex: Int, number: Int): String? {
        val decimalIndex = existingInput.indexOf(".")
        val isAfterDecimal = decimalIndex != -1 && currentIndex > decimalIndex
        val isDecimalFull = isAfterDecimal && existingInput.substringAfter(".").length >= 2

        if (isDecimalFull) return null // Limit to two numbers after the decimal
        println("Added to inout $currentIndex")

        return if (currentIndex == existingInput.length) {
            "$existingInput$number"
        } else {
            buildString {
                append(existingInput.substring(0, currentIndex + 1))
                append(number)
                append(existingInput.substring(currentIndex + 1))
            }
        }
    }

    private fun openPaymentPage() {
        val amount = depositScreenState.value.amount ?: return
        _depositScreenState.value = depositScreenState.value.copy(isLoading = true)
        viewModelScope.launch {
            val result = transactionRepo.initiatePayment(
                initiateTransactionBody = InitiateTransactionBody(
                    amount = "${amount}00",
                    email = loggedInUserMail,
                    callBackUrl = "${ApiRoute.BASE_URL}callback",
                    metaData = InitiateMetaData(cancelAction = "${ApiRoute.BASE_URL}cancel-payment")
                )
            )

            if (result is Result.Error) {
                val message = result.error.toErrorMessage()
                _depositScreenState.value = depositScreenState.value.copy(isLoading = false)
                println("UNABLE TO INITIATE PAYMENT $message")
                return@launch
            }
            val data = (result as Result.Success).data
            _depositScreenState.value = depositScreenState.value.copy(isLoading = false)
            _depositEventChannel.send(
                DepositScreenEvent.NavigateToPayStack(
                    authorizationUrl = data.authorizationUrl,
                    reference = data.referenceCode
                )
            )

//            withContext(Dispatchers.Main) {
//
//                println("Success code is $message")
//                paystack.initiatePaymentScreen()
//                paystack.makePayment("xkt2b6gk0vzerfa")
//            }
        }

    }

    private fun editCurrentIndex(
        index: Int
    ) {
        _depositScreenState.update {
            it.copy(
                currentIndex = index
            )
        }
    }


}