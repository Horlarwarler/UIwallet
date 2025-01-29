package com.crezent.finalyearproject.transaction.presentation.new_credit_card

import com.crezent.finalyearproject.domain.util.RemoteError

sealed interface NewCreditCardEvent {

    data class CardCreationError(val error: RemoteError) : NewCreditCardEvent

    data object CardCreationSuccessful : NewCreditCardEvent
}