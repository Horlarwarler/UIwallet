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
        matricNumber: String? = null,
        objectId: ObjectId? = null,
        emailAddress: String?
    ): Result<UserEntity, DatabaseError>

    suspend fun addUser(user: UserEntity): Result<String, DatabaseError>

    suspend fun deleteUser(objectId: ObjectId): Result<Unit, DatabaseError>

    suspend fun addToken(token: TokenEntity): Result<String, DatabaseError>

    suspend fun removeToken(objectId: ObjectId): Result<Unit, DatabaseError>

    suspend fun getTokenById(token: String, userId: String?, emailAddress: String?): Result<TokenEntity, DatabaseError>

    suspend fun getTokenByEmail(emailAddress: String, purpose: String): Result<TokenEntity, DatabaseError>

    suspend fun updateUserEmailVerify(emailAddress: String, isVerified: Boolean): Result<UserEntity, DatabaseError>

    suspend fun deleteExistingToken(email: String): Result<Boolean, DatabaseError>

    suspend fun updateUser(user: UserEntity): Result<UserEntity, DatabaseError>

    companion object {
        const val USER_COLLECTION = "USERS"
        const val WALLET_COLLECTION = "WALLET"
        const val TRANSACTION_COLLECTION = "TRANSACTIONS"
        const val CARD_COLLECTION = "CARDS"
        const val TOKEN_COLLECTION = "TOKEN"
    }


}