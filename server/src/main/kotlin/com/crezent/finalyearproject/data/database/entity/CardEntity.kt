package com.crezent.finalyearproject.data.database.entity

import com.crezent.finalyearproject.data.dto.CardResponse
import com.crezent.finalyearproject.data.dto.EncryptedCard
import com.crezent.finalyearproject.models.Card
import com.crezent.finalyearproject.utility.security.encryption.EncryptService
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class CardEntity(

    @BsonId
    val id: ObjectId,
    val aesEncryptedString: String,
    val encodedIv: String,
    val createdDate: String,
) {
    fun toCard(): Card {
        return Card(
            cardId = id.toString(),
            aesEncryptedString = aesEncryptedString,
        )
    }

    fun toCardResponse(
        encryptService: EncryptService,
        key: String

    ): CardResponse {
        val decrypted = decryptCardDetails(
            encryptService = encryptService,
            data = aesEncryptedString,
            key = key,
            encodedIv = encodedIv
        )
        return CardResponse(
            id = id.toString(),
            lastFourCharacter = decrypted.first,
            holderName = decrypted.second
        )
    }
}

private fun decryptCardDetails(
    encryptService: EncryptService,
    data: String,
    key: String,
    encodedIv: String
): Pair<String, String> {


    val decryptCardDetails = encryptService.decryptCardDetails(
        data = data,
        key = key,
        encodedIV = encodedIv
    ) ?: return Pair("", "")
    val decryptJson = Json.decodeFromString<EncryptedCard>(decryptCardDetails)
    return Pair(
        decryptJson.cardNumber.takeLast(4), decryptJson.holderName
    )
}
