package com.crezent.finalyearproject.transaction.presentation.new_credit_card

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crezent.finalyearproject.core.domain.BaseAppRepo
import com.crezent.finalyearproject.core.presentation.util.transformString
import com.crezent.finalyearproject.core.presentation.util.unTransformString
import com.crezent.finalyearproject.domain.util.Result
import com.crezent.finalyearproject.transaction.domain.TransactionRepo
import com.crezent.finalyearproject.transaction.presentation.payment_method.PaymentMethodEvent
import com.crezent.finalyearproject.transaction.presentation.payment_method.util.PaymentMethod
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.collections.set

class NewCreditCardViewmodel(
    private val transactionRepo: TransactionRepo,
    private val baseAppRepo: BaseAppRepo

) : ViewModel() {
    private val _state = MutableStateFlow(NewCreditCardScreenState())
    val state = _state.asStateFlow()

    private val _newCreditChannelEvent = Channel<NewCreditCardEvent>()
    val newCreditChannelEvent = _newCreditChannelEvent.receiveAsFlow()


    fun handleScreenAction(action: NewCreditCardScreenAction) {
        when (action) {
            is NewCreditCardScreenAction.Cvv -> handleCvvChange(action.cvv)
            is NewCreditCardScreenAction.OnCardNumberChange -> handleCardNumber(action.cardNumber)
            is NewCreditCardScreenAction.OnExpirationDateChange -> handleExpirationDate(action.expiration)
            is NewCreditCardScreenAction.OnHolderNameChange -> handleNameChange(action.name)
            NewCreditCardScreenAction.Save -> saveCardNumber()
        }
    }

    private fun handleCvvChange(cvv: String) {
        val errors = mutableListOf<String>()
        if (cvv.length > 3) {
            return
        }
        if (cvv.isBlank() || cvv.length != 3) {
            errors.add("CVV must be three digit length")
        }
        _state.update {
            it.copy(
                cvv = cvv,
                cvvError = errors
            )
        }
    }

    private fun handleNameChange(name: String) {
        val count = name.split(" ").size
        val errors = mutableListOf<String>()

        if (name.isBlank()) {
            errors.add("Name can not be blank")
        }
        if (count < 2 || count > 3) {
            errors.add("Only two or three different name is allow")
        }

        _state.update {
            it.copy(
                holderName = name,
                cardHolderNameError = errors
            )
        }

    }

    private fun handleCardNumber(number: String) {
        val numberContainsNonDigit = number.filterNot {
            it.isDigit()
        }
        if (number.length > 16) {
            return
        }
        val errors = mutableListOf<String>()
        if (numberContainsNonDigit.isNotEmpty()) {
            errors.add("Card number can contain only digit ")
        }
        if (number.length < 16) {
            errors.add("Card number should be of 16 min length")
        }

        _state.update {
            it.copy(
                cardNumber = number,
                cardNumberError = errors
            )
        }
    }

    private fun handleExpirationDate(number: String) {
        val numberContainsNonDigit = number.filterNot {
            it.isDigit()
        }
        val errors = mutableListOf<String>()
        if (number.length > 4) {
            return
        }
        if (numberContainsNonDigit.isNotEmpty()) {
            errors.add("Date number can contain only digit ")
        }
        if (number.length != 4) {
            errors.add("Invalid Expiration date")
        }

        _state.update {
            it.copy(
                expirationDate = number,
                expirationDateError = errors
            )
        }
    }


    private fun saveCardNumber() {
        val creditCardDetails = state.value
        val cardNumberHasError =
            creditCardDetails.cardNumber.isNullOrBlank() || creditCardDetails.cardNumberError.isNotEmpty()
        val cardHolderNameHasError =
            creditCardDetails.holderName.isNullOrBlank() || creditCardDetails.cardHolderNameError.isNotEmpty()

        val cvvHasError =
            creditCardDetails.cvv.isNullOrBlank() || creditCardDetails.cvvError.isNotEmpty()
        val expirationDateHasError =
            creditCardDetails.expirationDate.isNullOrBlank() || creditCardDetails.expirationDateError.isNotEmpty()
        if (cardNumberHasError || cardHolderNameHasError || cvvHasError || expirationDateHasError) {
            return
        }
        viewModelScope.launch(Dispatchers.IO) {

            _state.update {
                it.copy(
                    isLoading = true
                )
            }

            val result = transactionRepo.createCard(
                cardCvv = state.value.cvv!!,
                cardNumber = state.value.cardNumber!!,
                cardExpirationDate = state.value.expirationDate!!,
                cardHolderName = state.value.holderName!!,
            )

            if (result is Result.Error) {
                val errorMessage = result.error
                _newCreditChannelEvent.send(NewCreditCardEvent.CardCreationError(errorMessage))
                return@launch
            }
            baseAppRepo.getAndCacheAuthenticatedUser()
            _state.update {
                it.copy(
                    isLoading = false
                )
            }
            _newCreditChannelEvent.send(NewCreditCardEvent.CardCreationSuccessful)

        }

    }


}
