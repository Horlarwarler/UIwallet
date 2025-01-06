package com.crezent.finalyearproject.data.repo

import com.crezent.finalyearproject.data.database.entity.TokenEntity
import com.crezent.finalyearproject.data.database.entity.UserEntity
import com.crezent.finalyearproject.domain.util.DatabaseError
import com.crezent.finalyearproject.domain.util.Result
import com.crezent.finalyearproject.models.User
import com.mongodb.MongoException
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.firstOrNull

import org.bson.BsonValue
import org.bson.conversions.Bson
import org.bson.types.ObjectId
import javax.management.Query.and
import javax.management.Query.or

class KMongoUIWalletImpl(
    private val db: MongoDatabase
) : UIWalletRepository {

    companion object {
        const val USER_COLLECTION = "USERS"
        const val WALLET_COLLECTION = "WALLET"
        const val TRANSACTION_COLLECTION = "TRANSACTIONS"
        const val CARD_COLLECTION = "CARDS"
        const val TOKEN_COLLECTION = "TOKEN"
    }

    private val userCollection = db.getCollection<UserEntity>(USER_COLLECTION)

    private val tokenCollection = db.getCollection<TokenEntity>(TOKEN_COLLECTION)


    override suspend fun getAllUser(): Result<List<User>, DatabaseError> {
        TODO("I don't see a reason to get al user")
    }

    override suspend fun getUserById(
        matricNumber: String?,
        objectId: ObjectId?,
        emailAddress: String?
    ): Result<UserEntity, DatabaseError> {
        try {
            val objectIdFilter = Filters.eq("_id", objectId)

            val matricNumberFilter = Filters.eq(UserEntity::matricNumber.name, matricNumber)
            val emailAddressFilter = Filters.eq(UserEntity::emailAddress.name, emailAddress)
            val user =
                userCollection.find(Filters.or(objectIdFilter, matricNumberFilter, emailAddressFilter)).firstOrNull()
                    ?: return Result.Error(error = DatabaseError.ItemNotFound)
            return Result.Success(data = user)
        } catch (e: MongoException) {

            return Result.Error(DatabaseError.OtherError(error = e.message ?: "Mongo Exception Unknown Errro"))

        } catch (e: Exception) {
            e.printStackTrace()
            return Result.Error(DatabaseError.OtherError(error = e.message ?: "Unknown Errro"))

        }
    }

    override suspend fun addUser(user: UserEntity): Result<BsonValue, DatabaseError> {

        return insert(data = user, collection = userCollection)

    }

    override suspend fun deleteUser(objectId: ObjectId): Result<Unit, DatabaseError> {
        try {
            userCollection.deleteOne(Filters.eq("_id", objectId))
            return Result.Success(Unit)
        } catch (e: MongoException) {

            return Result.Error(DatabaseError.OtherError(error = e.message ?: "Mongo Exception Unknown Errro"))

        } catch (e: Exception) {
            return Result.Error(DatabaseError.OtherError(error = e.message ?: "Unknown Errro"))

        }
    }

    override suspend fun addToken(token: TokenEntity): Result<BsonValue, DatabaseError> {

        return insert(data = token, collection = tokenCollection)

    }

    override suspend fun removeToken(objectId: ObjectId): Result<Unit, DatabaseError> {
        return deleteData(
            filters = Filters.eq("_id", objectId),
            collection = tokenCollection
        )
    }

    override suspend fun getTokenById(token: String, userId: String): Result<TokenEntity, DatabaseError> {
        try {
            val tokenFilter = Filters.eq(TokenEntity::hashedToken.name, token)
            val userFilter = Filters.eq(TokenEntity::userId.name, userId)
            val tokenEntity =
                tokenCollection.find(Filters.and(tokenFilter, userFilter)).firstOrNull() ?: return Result.Error(
                    error = DatabaseError.ItemNotFound
                )
            return Result.Success(data = tokenEntity)
        } catch (e: MongoException) {

            return Result.Error(DatabaseError.OtherError(error = e.message ?: "Mongo Exception Unknown Errro"))

        } catch (e: Exception) {
            return Result.Error(DatabaseError.OtherError(error = e.message ?: "Unknown Errro"))

        }

    }

    override suspend fun updateUserEmailVerify(
        userId: ObjectId,
        isVerified: Boolean
    ): Result<UserEntity, DatabaseError> {
        try {

            val bson = Filters.eq("_id", userId)
            val verifiedString = if (isVerified) "true" else "false"
            val userEntity = userCollection.findOneAndUpdate(
                filter = bson,
                update = Updates.combine(
                    Updates.set(UserEntity::emailAddressVerified.name, verifiedString),
                    Updates.set(UserEntity::accountStatus.name, "active"),

                    )
            ) ?: return Result.Error(
                error = DatabaseError.ItemNotFound
            )

            return Result.Success(
                data = userEntity.copy(
                    emailAddressVerified = verifiedString,
                    accountStatus = "active"
                )
            )


        } catch (e: MongoException) {

            return Result.Error(DatabaseError.OtherError(error = e.message ?: "Mongo Exception Unknown Errro"))

        } catch (e: Exception) {
            return Result.Error(DatabaseError.OtherError(error = e.message ?: "Unknown Errro"))

        }
    }

    private suspend fun <Entity : Any> insert(
        data: Entity,
        collection: MongoCollection<Entity>
    ): Result<BsonValue, DatabaseError> {
        try {
            val id = collection.insertOne(data).insertedId ?: kotlin.run {
                return Result.Error(DatabaseError.InsertingError)
            }
            return Result.Success(data = id)
        } catch (e: MongoException) {

            return Result.Error(DatabaseError.OtherError(error = e.message ?: "Mongo Exception Unknown Errro"))

        } catch (e: Exception) {
            return Result.Error(DatabaseError.OtherError(error = e.message ?: "Unknown Errro"))

        }
    }

    private suspend fun <Entity : Any> deleteData(
        filters: Bson,
        collection: MongoCollection<Entity>

    ): Result<Unit, DatabaseError> {
        try {
            collection.deleteOne(filter = filters)
            return Result.Success(Unit)
        } catch (e: MongoException) {

            return Result.Error(DatabaseError.OtherError(error = e.message ?: "Mongo Exception Unknown Errro"))

        } catch (e: Exception) {
            return Result.Error(DatabaseError.OtherError(error = e.message ?: "Unknown Errro"))

        }
    }


}