package com.crezent.finalyearproject.data.database.entity

import com.crezent.finalyearproject.models.Card
import com.crezent.finalyearproject.models.Wallet
import com.crezent.finalyearproject.transaction.Transaction
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class WalletEntity(
    @BsonId
    val id: ObjectId,
    val accountBalance: Double,
    val transactions: List<TransactionEntity> = emptyList(),
    val connectedCards: List<CardEntity> = emptyList(),
) {
    fun toWallet(): Wallet {
        return Wallet(
            walletId = id.toString(),
            accountBalance = accountBalance,
            transactions = transactions.map { it.toTransaction() },
            connectedCards = connectedCards.map { it.toCard() }
        )
    }
}
