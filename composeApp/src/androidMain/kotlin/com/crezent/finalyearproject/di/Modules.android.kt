package com.crezent.finalyearproject.di

import android.content.Context
import android.security.keystore.KeyProperties
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import androidx.security.crypto.MasterKey.KeyScheme
import com.crezent.finalyearproject.core.data.preference.RusshwolfEncryPrefImpl
import com.crezent.finalyearproject.core.data.preference.RusshwolfSharedPrefImpl
import com.crezent.finalyearproject.core.domain.preference.EncryptedSharePreference
import com.crezent.finalyearproject.core.domain.preference.SharedPreference
import com.russhwolf.settings.SharedPreferencesSettings
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.android.AndroidClientEngine
import io.ktor.client.engine.android.AndroidEngineConfig
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {
        single<SharedPreference> {
            val context = androidContext()
            val settings = SharedPreferencesSettings(
                context.getSharedPreferences("UIWALLET", Context.MODE_PRIVATE)
            )
            KeyProperties.DIGEST_SHA256
            RusshwolfSharedPrefImpl(settings)
        }

        single<EncryptedSharePreference> {
            val context = androidContext()
            val masterKey = MasterKey
                .Builder(context)
                .setKeyScheme(KeyScheme.AES256_GCM)
                .build()

            val encryptedSharePreference = EncryptedSharedPreferences.create(
                context,
                context.packageName + "_encrypted_preference",
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )

            RusshwolfEncryPrefImpl(SharedPreferencesSettings(encryptedSharePreference))
        }

        single<HttpClientEngine> {
            val config = AndroidEngineConfig()
            AndroidClientEngine(
                config = config
            )
        }
    }