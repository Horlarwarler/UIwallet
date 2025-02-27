package com.crezent.finalyearproject.data.repo

import com.crezent.finalyearproject.data.database.entity.CardEntity
import com.crezent.finalyearproject.data.database.entity.TransactionEntity
import com.crezent.finalyearproject.data.database.entity.UserEntity
import com.crezent.finalyearproject.data.repo.UIWalletRepository.Companion.USER_COLLECTION
import com.crezent.finalyearproject.domain.util.DatabaseError
import com.crezent.finalyearproject.domain.util.Result
import com.crezent.finalyearproject.models.Card
import com.crezent.finalyearproject.transaction.FundingSourceDto
import com.crezent.finalyearproject.transaction.TransactionDto
import com.crezent.finalyearproject.transaction.TransactionStatus
import com.mongodb.MongoException
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.bson.types.ObjectId

class KMongoPaymentRepository(
    db: MongoDatabase

) : PaymentRepository {

    private val cardCollection = db.getCollection<CardEntity>(CARD_COLLECTION)
    private val userCollection = db.getCollection<UserEntity>(USER_COLLECTION)

    override suspend fun getCardById(cardId: String, email: String): Result<Card, DatabaseError> {
        return try {
            // Define the filter to find the user by email and card ID
            val filter = Filters.and(
                Filters.eq(UserEntity::emailAddress.name, email),
                Filters.elemMatch(
                    UserEntity::connectedCards.name,
                    Filters.eq(CardEntity::id.name, ObjectId(cardId))
                )
            )

            // Find the user document that matches the filter
            val user = userCollection.find(filter).firstOrNull()
                ?: return Result.Error(DatabaseError.ItemNotFound)

            // Extract the card from the user's connectedCards
            val card = user.connectedCards.firstOrNull {
                val hex = it.id.toHexString()
                println("Hex is $hex")
                hex == cardId
            }?.toCard()
                ?: return Result.Error(DatabaseError.ItemNotFound)

            Result.Success(card)
        } catch (e: MongoException) {
            Result.Error(DatabaseError.OtherError(error = e.message ?: "MongoDB Exception: Unknown Error"))
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(DatabaseError.OtherError(error = e.message ?: "Unknown Error"))
        }
    }


    override suspend fun addCard(email: String, aesEncryptedString: String, iv: String): Result<String, DatabaseError> {
        val cardEntity = CardEntity(
            id = ObjectId(),
            aesEncryptedString = aesEncryptedString,
            createdDate = System.currentTimeMillis().toString(),
            encodedIv = iv
        )

        return try {
            // Check if the user exists
            val filter = Filters.eq(UserEntity::emailAddress.name, email)

            // Append the new card to the connectedCards array
            val updateResult = userCollection.updateOne(
                filter = filter,
                update = Updates.push(UserEntity::connectedCards.name, cardEntity)
            )

            // Check if the update succeeded
            if (updateResult.modifiedCount == 1L) {
                Result.Success("Card added successfully")
            } else {
                Result.Error(DatabaseError.InsertingError)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(DatabaseError.OtherError("An error occurred: ${e.message}"))
        }
    }

    override suspend fun addTransaction(
        transactionDto: TransactionDto,
        email: String
    ): Result<String, DatabaseError> {


        val transactionEntity = TransactionEntity(
            id = ObjectId(),
            transactionTitle = transactionDto.transactionTitle,
            transactionDescription = transactionDto.transactionDescription,
            transactionAmount = transactionDto.transactionAmount,
            transactionStatus = transactionDto.transactionStatus.toString(),
            transactionType = transactionDto.transactionType.toString(),
            transactionDate = transactionDto.paidAt,
            fundingSource = Json.encodeToString(transactionDto.fundingSourceDto),
            createdDate = transactionDto.createdDate,
            reference = transactionDto.reference!!

        )

        return try {
            // Check if the user exists
            val filter = Filters.eq(UserEntity::emailAddress.name, email)
            val user = userCollection.find(filter).firstOrNull()
                ?: return Result.Error(DatabaseError.ItemNotFound)


            // Ensure user has a wallet, otherwise return an error
            val existingWallet =
                user.wallet ?: return Result.Error(DatabaseError.OtherError("User does not have a wallet"))

            val existingTransaction = existingWallet.transactions.firstOrNull {
                it.reference == transactionDto.reference
            }

            existingTransaction?.let {
                return Result.Success(it.id.toString())
            }

            val paymentSuccessful = transactionDto.transactionStatus == TransactionStatus.Success

            val updatedWallet = existingWallet.copy(
                transactions = existingWallet.transactions + transactionEntity,
                accountBalance = if (paymentSuccessful) existingWallet.accountBalance + transactionDto.transactionAmount else existingWallet.accountBalance
            )
            println("-------------------")

            println(updatedWallet)
            println(user)

            println("-------------------")

            // Update the user's wallet in the database
            val updateResult = userCollection.updateOne(
                filter = filter,
                update = Updates.set(UserEntity::wallet.name, updatedWallet)
            )

            if (updateResult.modifiedCount == 1L) {
                Result.Success(transactionEntity.id.toString())
            } else {
                Result.Error(DatabaseError.InsertingError)
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(DatabaseError.OtherError("An error occurred: ${e.message}"))
        }


    }


    companion object {
        const val CARD_COLLECTION = "CARD_COLLECTION"
    }


}