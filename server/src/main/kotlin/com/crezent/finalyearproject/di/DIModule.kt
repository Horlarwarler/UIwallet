package com.crezent.finalyearproject.di

import com.crezent.finalyearproject.data.repo.KMongoPaymentRepository
import com.crezent.finalyearproject.data.repo.KMongoUIWalletImpl
import com.crezent.finalyearproject.data.repo.PaymentRepository
import com.crezent.finalyearproject.data.repo.UIWalletRepository
import com.crezent.finalyearproject.utility.security.hashing.BcryptHash
import com.crezent.finalyearproject.utility.security.hashing.HashingService
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoDatabase

import org.koin.dsl.module

val serverModule = module {

    single<MongoDatabase> {
        MongoClient.create("mongodb://localhost:27017/").getDatabase("UIWallet")
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