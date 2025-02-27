package com.crezent.finalyearproject.data.database.entity

import com.crezent.finalyearproject.transaction.*
import kotlinx.serialization.json.Json
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class TransactionEntity(
    @BsonId
    val id: ObjectId,
    val reference: String,
    val transactionTitle: String,
    val transactionDescription: String,
    val transactionAmount: Double,
    val transactionStatus: String,
    val transactionType: String,
    val transactionDate: String,
    val fundingSource: String,
    val createdDate: String
) {
    fun toTransaction(): TransactionDto {
        return TransactionDto(
            transactionId = id.toString(),
            reference = reference,
            transactionTitle = transactionTitle,
            transactionDescription = transactionDescription,
            transactionAmount = transactionAmount,
            transactionStatus = TransactionStatus.valueOf(transactionStatus),
            transactionType = TransactionType.valueOf(transactionType),
            paidAt = transactionDate,
            fundingSourceDto = fundingSource.toFundingSource(),
            createdDate = createdDate,
        )
    }

    private fun String.toFundingSource(): FundingSourceDto {


        val endIndex = indexOf("\"", 9)

        //   val stringBefore = substring(startIndex = 9, endIndex = endIndex)

        val json = Json {
            ignoreUnknownKeys = true
        }
        return json.decodeFromString<FundingSourceDto>(this)


//        println(stringBefore)
//
//        return when (stringBefore) {
//            "Bank Transfer" -> json.decodeFromString<FundingSourceDto.BankTransfer>(this)
//            "Ussd Payment" -> json.decodeFromString<FundingSourceDto.UssdPayment>(this)
//            else -> json.decodeFromString<FundingSourceDto.CardPayment>(this)
//        }
    }


}
