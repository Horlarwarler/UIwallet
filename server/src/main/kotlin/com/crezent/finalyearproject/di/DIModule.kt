package com.crezent.finalyearproject.di

import com.crezent.finalyearproject.data.repo.KMongoPaymentRepository
import com.crezent.finalyearproject.data.repo.KMongoUIWalletImpl
import com.crezent.finalyearproject.data.repo.PaymentRepository
import com.crezent.finalyearproject.data.repo.UIWalletRepository
import com.crezent.finalyearproject.utility.security.hashing.BcryptHash
import com.crezent.finalyearproject.utility.security.hashing.HashingService
import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import com.mongodb.ServerApi
import com.mongodb.ServerApiVersion
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.bson.Document
import org.koin.dsl.module

val serverModule = module {

    val password = System.getenv("db_password")
    single<MongoDatabase> {
        val connectionString =
            "mongodb+srv://Horlarwarler:$password@cluster0.za1kgzq.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0"
        val serverApi = ServerApi.builder()
            .version(ServerApiVersion.V1)
            .build()
        val mongoClientSettings = MongoClientSettings.builder()
            .applyConnectionString(ConnectionString(connectionString))
            .serverApi(serverApi)
            .build()
        // Create a new client and connect to the server
        val client = MongoClient.create(mongoClientSettings)
        val database = client.getDatabase("wallet")
        database
//        database.runCommand(Document("ping", 1))
//        println("Pinged your deployment. You successfully connected to MongoDB!")
//        MongoClient.create(mongoClientSettings).use { mongoClient ->
//            val database = mongoClient.getDatabase("admin")
//
//        }
        //  MongoClient.create("mongodb://localhost:27017/").getDatabase("UIWallet")
    }

    single<UIWalletRepository> {
        KMongoUIWalletImpl(get())
    }
    single<PaymentRepository> {
        KMongoPaymentRepository(get())
    }

    single<HashingService> {
        BcryptHash()
    }

}