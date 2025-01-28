package com.crezent.finalyearproject.data.database.entity

import com.crezent.finalyearproject.data.dto.WalletDto
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class WalletEntity(
    @BsonId
    val id: ObjectId = ObjectId(),
    val accountBalance: Double = 0.0,
    val transactions: List<TransactionEntity> = emptyList(),
) {
    fun toWallet(): WalletDto {
        return WalletDto(
            walletId = id.toString(),
            accountBalance = accountBalance,
            transactionDtos = transactions.map { it.toTransaction() },
            //connectedCards = connectedCards.map { it.toCard() }
        )
    }
}
