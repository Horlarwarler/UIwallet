package com.crezent.finalyearproject.data.repo

import com.crezent.finalyearproject.data.database.entity.TokenEntity
import com.crezent.finalyearproject.data.database.entity.UserEntity
import com.crezent.finalyearproject.data.database.entity.WalletEntity
import com.crezent.finalyearproject.data.repo.UIWalletRepository.Companion.TOKEN_COLLECTION
import com.crezent.finalyearproject.data.repo.UIWalletRepository.Companion.USER_COLLECTION
import com.crezent.finalyearproject.data.util.insert
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

    override suspend fun addUser(user: UserEntity): Result<String, DatabaseError> {

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

    override suspend fun addToken(token: TokenEntity): Result<String, DatabaseError> {

        return insert(data = token, collection = tokenCollection)

    }

    override suspend fun removeToken(objectId: ObjectId): Result<Unit, DatabaseError> {
        return deleteData(
            filters = Filters.eq("_id", objectId),
            collection = tokenCollection
        )
    }

    override suspend fun getTokenById(
        token: String,
        userId: String?,
        emailAddress: String?
    ): Result<TokenEntity, DatabaseError> {
        try {
            val tokenFilter = Filters.eq(TokenEntity::hashedToken.name, token)
            val userFilter = Filters.eq(TokenEntity::userId.name, userId)
            val emailFilter = Filters.eq(TokenEntity::emailAddress.name, emailAddress)
            val mergedFilter = Filters.and(
                tokenFilter, Filters.or(userFilter, emailFilter)
            )
            val tokenEntity =
                tokenCollection.find(mergedFilter).firstOrNull() ?: return Result.Error(
                    error = DatabaseError.ItemNotFound
                )
            return Result.Success(data = tokenEntity)
        } catch (e: MongoException) {

            return Result.Error(DatabaseError.OtherError(error = e.message ?: "Mongo Exception Unknown Errro"))

        } catch (e: Exception) {
            return Result.Error(DatabaseError.OtherError(error = e.message ?: "Unknown Errro"))

        }

    }

    override suspend fun getTokenByEmail(emailAddress: String, purpose: String): Result<TokenEntity, DatabaseError> {
        try {
            val emailFilter = Filters.eq(TokenEntity::emailAddress.name, emailAddress)
            val purposeFilter = Filters.eq(TokenEntity::purpose.name, purpose)

            val mergedFilter = Filters.and(emailFilter, purposeFilter)

            val tokenEntity =
                tokenCollection.find(mergedFilter).firstOrNull() ?: return Result.Error(
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
        emailAddress: String,
        isVerified: Boolean
    ): Result<UserEntity, DatabaseError> {
        try {

            val bson = Filters.eq(UserEntity::emailAddress.name, emailAddress)

            val verifiedString = "true"
            val walletEntity = WalletEntity()
            val userEntity = userCollection.findOneAndUpdate(
                filter = bson,
                update = Updates.combine(
                    Updates.set(UserEntity::emailAddressVerified.name, verifiedString),
                    Updates.set(UserEntity::accountStatus.name, "active"),
                    Updates.set(UserEntity::wallet.name, walletEntity)

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

    override suspend fun deleteExistingToken(email: String): Result<Boolean, DatabaseError> {
        try {
            val tokenResult = tokenCollection.deleteMany(
                filter = Filters.eq(UserEntity::emailAddress.name, email)
            )
            return Result.Success(tokenResult.wasAcknowledged())
        } catch (e: MongoException) {

            return Result.Error(DatabaseError.OtherError(error = e.message ?: "Mongo Exception Unknown Errro"))

        } catch (e: Exception) {
            return Result.Error(DatabaseError.OtherError(error = e.message ?: "Unknown Errro"))

        }
    }

    override suspend fun updateUser(user: UserEntity): Result<UserEntity, DatabaseError> {
        try {

            val userUpdateResult = userCollection.findOneAndUpdate(
                filter = Filters.eq("_id", user.id),
                update = Updates.combine(
                    Updates.set(UserEntity::accountStatus.name, user.accountStatus),
                    Updates.set(UserEntity::userName.name, user.emailAddress),
                    Updates.set(UserEntity::hashedPassword.name, user.hashedPassword),
                    Updates.set(UserEntity::lastUsedPasswords.name, user.lastUsedPasswords),
                    Updates.set(UserEntity::emailAddressVerified.name, user.emailAddressVerified)
                )
            ) ?: kotlin.run {
                return Result.Error(error = DatabaseError.ItemNotFound)
            }
            return Result.Success(data = userUpdateResult)

        } catch (e: MongoException) {

            return Result.Error(DatabaseError.OtherError(error = e.message ?: "Mongo Exception Unknown Errro"))

        } catch (e: Exception) {
            return Result.Error(DatabaseError.OtherError(error = e.message ?: "Unknown Errro"))

        }


    }
}