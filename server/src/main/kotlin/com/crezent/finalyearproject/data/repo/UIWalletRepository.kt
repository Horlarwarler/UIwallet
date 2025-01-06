package com.crezent.finalyearproject.data.repo

import com.crezent.finalyearproject.data.database.entity.TokenEntity
import com.crezent.finalyearproject.data.database.entity.UserEntity
import com.crezent.finalyearproject.domain.util.DatabaseError
import com.crezent.finalyearproject.domain.util.Result
import com.crezent.finalyearproject.models.User
import org.bson.BsonValue
import org.bson.types.ObjectId

interface UIWalletRepository {

    suspend fun getAllUser(): Result<List<User>, DatabaseError>

    suspend fun getUserById(
        matricNumber: String?,
        objectId: ObjectId?,
        emailAddress: String?
    ): Result<UserEntity, DatabaseError>

    suspend fun addUser(user: UserEntity): Result<BsonValue, DatabaseError>

    suspend fun deleteUser(objectId: ObjectId): Result<Unit, DatabaseError>

    suspend fun addToken(token: TokenEntity): Result<BsonValue, DatabaseError>

    suspend fun removeToken(objectId: ObjectId): Result<Unit, DatabaseError>

    suspend fun getTokenById(token: String, userId: String): Result<TokenEntity, DatabaseError>

    suspend fun updateUserEmailVerify(userId: ObjectId, isVerified: Boolean) :  Result<UserEntity, DatabaseError>


}