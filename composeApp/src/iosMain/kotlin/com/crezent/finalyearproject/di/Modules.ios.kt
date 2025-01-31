package com.crezent.finalyearproject.di

import com.crezent.finalyearproject.core.data.preference.RusshwolfEncryPrefImpl
import com.crezent.finalyearproject.core.data.preference.RusshwolfSharedPrefImpl
import com.crezent.finalyearproject.core.data.security.encryption.CryptographicOperation
import com.crezent.finalyearproject.core.data.security.encryption.KeyPairGenerator
import com.crezent.finalyearproject.core.domain.preference.EncryptedSharePreference
import com.crezent.finalyearproject.core.domain.preference.SharedPreference
import com.crezent.finalyearproject.platform.IosApplicationComponent
import com.russhwolf.settings.ExperimentalSettingsImplementation
import com.russhwolf.settings.KeychainSettings
import com.russhwolf.settings.NSUserDefaultsSettings
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.Foundation.NSBundle
import platform.Foundation.NSUserDefaults

@OptIn(ExperimentalSettingsImplementation::class)
actual val platformModule: Module
    get() = module {
        single<SharedPreference> {
            val settings = NSUserDefaultsSettings(NSUserDefaults.standardUserDefaults)
            RusshwolfSharedPrefImpl(settings)
        }
        single<KeyPairGenerator> {
            get<IosApplicationComponent>().keyPairGenerator
        }
        single<CryptographicOperation> {
            get<IosApplicationComponent>().cryptographicOperation
        }
        single<EncryptedSharePreference> {
            val settings = KeychainSettings("${NSBundle.mainBundle.bundleIdentifier}.AUTH")
            RusshwolfEncryPrefImpl(settings)

        }

        single<HttpClientEngine> {
            Darwin.create()
        }
    }