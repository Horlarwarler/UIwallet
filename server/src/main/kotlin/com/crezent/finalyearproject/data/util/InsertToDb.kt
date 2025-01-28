package com.crezent.finalyearproject.data.util

import com.crezent.finalyearproject.domain.util.DatabaseError
import com.crezent.finalyearproject.domain.util.Result
import com.mongodb.MongoException
import com.mongodb.kotlin.client.coroutine.MongoCollection
import org.bson.BsonValue

suspend fun <Entity : Any> insert(
    data: Entity,
    collection: MongoCollection<Entity>
): Result<String, DatabaseError> {
    try {
        val bsonId = collection.insertOne(data).insertedId ?: kotlin.run {
            return Result.Error(DatabaseError.InsertingError)
        }

        val regex = Regex("\\w+}")
        val id = regex.find(bsonId.toString())!!.value.dropLast(1)
        return Result.Success(data = id)
    } catch (e: MongoException) {

        return Result.Error(DatabaseError.OtherError(error = e.message ?: "Mongo Exception Unknown Errro"))

    } catch (e: Exception) {
        return Result.Error(DatabaseError.OtherError(error = e.message ?: "Unknown Errro"))

    }
}