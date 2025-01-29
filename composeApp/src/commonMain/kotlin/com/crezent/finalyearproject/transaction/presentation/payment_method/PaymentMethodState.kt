package com.crezent.finalyearproject.transaction.presentation.payment_method

import com.crezent.finalyearproject.core.domain.model.Card
import com.crezent.finalyearproject.transaction.presentation.payment_method.util.PaymentMethod

data class PaymentMethodState(
    val isLoading: Boolean = false,
    val cards: List<Card> = emptyList(),
    val selectedPayment: PaymentMethod? = null,
    val currentSelectedCard: Card? = null,
    val verifiedCard: HashMap<String, Boolean> = hashMapOf(),
    val cvvText: String? = null

)
