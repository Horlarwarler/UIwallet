package com.crezent.finalyearproject.data.database.entity

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class TokenEntity(
    @BsonId
    val id: ObjectId = ObjectId(),
    val userId: String,
    val hashedToken: String,
    val purpose: String,
    val expiresAt: Long
)
