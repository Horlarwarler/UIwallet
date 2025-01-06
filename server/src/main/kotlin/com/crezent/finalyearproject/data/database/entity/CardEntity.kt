package com.crezent.finalyearproject.data.database.entity

import com.crezent.finalyearproject.models.Card
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class CardEntity(

    @BsonId
    val id: ObjectId,
    val encryptedCardNumber: String,
    val encryptedExpirationMonth: String,
    val encryptedExpirationYear: String,
    val hashedCvv: String,
    val encryptedCardHolderName: String,
) {
    fun toCard(): Card {
        return Card(
            cardId = id.toString(),
            encryptedCardNumber = encryptedCardNumber,
            encryptedExpirationMonth = encryptedExpirationMonth,
            encryptedExpirationYear = encryptedExpirationYear,
            hashedCvv = hashedCvv,
            encryptedCardHolderName = encryptedCardHolderName
        )
    }
}
