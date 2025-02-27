package com.crezent.finalyearproject.core.data.mapper

import com.crezent.finalyearproject.core.domain.model.FundingSource
import com.crezent.finalyearproject.core.domain.model.Transaction
import com.crezent.finalyearproject.transaction.FundingSourceDto
import com.crezent.finalyearproject.transaction.FundingSourceDto.BankTransfer
import com.crezent.finalyearproject.transaction.FundingSourceDto.CardPayment
import com.crezent.finalyearproject.transaction.FundingSourceDto.UssdPayment
import com.crezent.finalyearproject.transaction.TransactionDto

fun TransactionDto.toTransaction(): Transaction {

    return Transaction(
        transactionId = transactionId!!,
        transactionTitle = transactionTitle,
        transactionDescription = transactionDescription,
        transactionAmount = transactionAmount,
        transactionStatus = transactionStatus,
        transactionType = transactionType,
        paidAt = paidAt,
        createdDate = createdDate,
        fundingSource = fundingSourceDto.toFunding(),
    )

}

private fun FundingSourceDto.toFunding(): FundingSource {
    return when (this) {
        is BankTransfer -> FundingSource.BankTransfer(
            accountName = accountName,
            accountNumber = accountNumber
        )

        is CardPayment -> FundingSource.CardPayment(
            cardNumber = lastFourDigit
        )

        is UssdPayment -> FundingSource.UssdPayment(
            bank = bank
        )

        is FundingSourceDto.Bank -> FundingSource.Bank(
            bank = bank,
            lastFourDigit = lastFourDigit
        )
    }
}
