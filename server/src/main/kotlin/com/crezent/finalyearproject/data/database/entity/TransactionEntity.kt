package com.crezent.finalyearproject.data.database.entity

import com.crezent.finalyearproject.transaction.*
import kotlinx.serialization.json.Json
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class TransactionEntity(
    @BsonId
    val id: ObjectId,
    val transactionTitle: String,
    val transactionDescription: String,
    val transactionAmount: Double,
    val transactionStatus: String,
    val transactionType: String,
    val transactionDate: String,
    val emailId: String,
    val fundingSource: String
) {
    fun toTransaction(): TransactionDto {
        return TransactionDto(
            transactionId = id.toString(),
            transactionTitle = transactionTitle,
            transactionDescription = transactionDescription,
            transactionAmount = transactionAmount,
            transactionStatus = TransactionStatus.valueOf(transactionStatus),
            transactionType = TransactionType.valueOf(transactionType),
            transactionDate = transactionDate,
            emailId = emailId,
            fundingSourceDto = fundingSource.toFundingSource(),
        )
    }

    private fun String.toFundingSource(): FundingSourceDto {
        val endIndex = indexOf("\"", 9)

        val stringBefore = substring(startIndex = 9, endIndex = endIndex)

        println(stringBefore)

        return when (stringBefore) {
            "Bank Transfer" -> Json.decodeFromString<BankTransfer>(this)
            "Ussd Payment" -> Json.decodeFromString<UssdPayment>(this)
            else -> Json.decodeFromString<CardPayment>(this)
        }
    }


}
