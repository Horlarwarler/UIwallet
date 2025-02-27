package com.crezent.finalyearproject.transaction.presentation.transaction_details

import com.crezent.finalyearproject.core.domain.model.Transaction

expect fun generateReceipt(
    transaction: Transaction
): ByteArray

expect fun saveInvoiceToFile(
    data: ByteArray,
    fileName: String
)