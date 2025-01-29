package com.crezent.finalyearproject.transaction.presentation.deposit

import androidx.lifecycle.ViewModel
import com.crezent.finalyearproject.core.presentation.component.NumberInputType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DepositScreenViewmodel : ViewModel() {
    private val _depositScreenState = MutableStateFlow(DepositScreenState())

    val depositScreenState = _depositScreenState.asStateFlow()


    fun handleScreenAction(action: DepositScreenAction) {
        when (action) {
            is DepositScreenAction.EditInput -> {
                editScreenAction(action.numberInputType)
            }

            is DepositScreenAction.EditCurrentIndex -> editCurrentIndex(action.index)
        }
    }

    private fun editScreenAction(input: NumberInputType) {
        val state = depositScreenState.value
        val existingInput = state.depositAmount.orEmpty()
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
                depositAmount = newInput,
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