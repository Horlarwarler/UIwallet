package com.crezent.finalyearproject.di

import com.crezent.finalyearproject.data.repo.KMongoPaymentRepository
import com.crezent.finalyearproject.data.repo.KMongoUIWalletImpl
import com.crezent.finalyearproject.data.repo.PaymentRepository
import com.crezent.finalyearproject.data.repo.UIWalletRepository
import com.crezent.finalyearproject.utility.security.hashing.BcryptHash
import com.crezent.finalyearproject.utility.security.hashing.HashingService
import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.ServerApi
import com.mongodb.ServerApiVersion
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.bson.Document
import org.koin.dsl.module

val serverModule = module {
    //kO45SH7EdfJfczJT

    single<MongoDatabase> {
        MongoClient.create("mongodb://localhost:27017/").getDatabase("UIWallet")
//        val password = System.getenv("db_password")
//
//        val connectionString =
//            "mongodb+srv://Horlarwarler:$password@cluster0.za1kgzq.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0"
//        val serverApi = ServerApi.builder()
//            .version(ServerApiVersion.V1)
//            .build()
//        val mongoClientSettings = MongoClientSettings.builder()
//            .applyConnectionString(ConnectionString(connectionString))
//            .serverApi(serverApi)
//            .build()
//        // Create a new client and connect to the server
//        val client = MongoClient.create(mongoClientSettings)
//        val database = client.getDatabase("Wallet")
//
//        database



    }

    single<HttpClient> {
        HttpClient(CIO) {
            install(Logging) {
                level = LogLevel.ALL
            }
            install(ContentNegotiation) {
                json(
                    Json {
                        isLenient = true
                        prettyPrint = true
                        ignoreUnknownKeys = true
                    },
                    contentType = ContentType.Application.Json
                )
            }
            engine {
                maxConnectionsCount = 1000
                endpoint {
                    // this: EndpointConfig
                    maxConnectionsPerRoute = 100
                    pipelineMaxSize = 20
                    keepAliveTime = 5000
                    connectTimeout = 5000
                    connectAttempts = 5
                }
            }
        }

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