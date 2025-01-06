package com.crezent.finalyearproject.core.data.security.encryption

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import com.crezent.finalyearproject.ANDROID_KEY_STORE
import com.crezent.finalyearproject.EC_ALIAS
import com.crezent.finalyearproject.ELLIPTIC_CURVE_STANDARD_NAME
import com.crezent.finalyearproject.RSA_ALIAS
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.KeyStore.PrivateKeyEntry
import java.security.spec.ECGenParameterSpec

actual object KeyPairGenerator {


    private val keyStore = KeyStore.getInstance(ANDROID_KEY_STORE).apply {
        load(null)
    }


    actual fun generateKeyStore() {

        generateRsaKey()
        generateEcKey()


    }

    private fun generateRsaKey() {
        if (keyStore != null && keyStore.containsAlias(RSA_ALIAS)) {
//            keyStore.deleteEntry(RSA_ALIAS)
            return
        }
        val keyPairGenerator = KeyPairGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_RSA, ANDROID_KEY_STORE
        )

        val keySpec = KeyGenParameterSpec.Builder(
            RSA_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or
                    KeyProperties.PURPOSE_DECRYPT
        )
            .setDigests(KeyProperties.DIGEST_SHA256)
            .setSignaturePaddings(KeyProperties.SIGNATURE_PADDING_RSA_PKCS1)
            .setBlockModes(KeyProperties.BLOCK_MODE_ECB)
            .setUserAuthenticationRequired(false)
            .build()

        keyPairGenerator.initialize(keySpec)
        keyPairGenerator.genKeyPair()

    }


    private fun generateEcKey() {
        if (keyStore != null && keyStore.containsAlias(EC_ALIAS)) {

            println("EC KEY ALREADY EXIST")
            return
        }

        val kpgGenerator = KeyPairGenerator
            .getInstance(KeyProperties.KEY_ALGORITHM_EC, ANDROID_KEY_STORE)

        val keyPairGenerator: KeyGenParameterSpec = KeyGenParameterSpec.Builder(
            EC_ALIAS,
            KeyProperties.PURPOSE_SIGN or KeyProperties.PURPOSE_VERIFY
        )
            .setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
            .setKeySize(256)
            .setAlgorithmParameterSpec(ECGenParameterSpec(ELLIPTIC_CURVE_STANDARD_NAME))
            .build()

        kpgGenerator.initialize(keyPairGenerator)
        kpgGenerator.genKeyPair()

    }


    actual fun getClientKeyPair(alias: String): String {
        val keyPair = keyStore!!.getEntry(alias, null) as? PrivateKeyEntry


        if (keyPair == null) {
            println("ALIAS $alias null ")
        } else {
            println("ALIAS not null ")

        }
        val encodedArray = keyPair?.certificate?.publicKey?.encoded

        if (encodedArray == null) {
            println("Encoded is  n null ")

        }
        val publicKey = Base64.encodeToString(keyPair?.certificate?.encoded, Base64.DEFAULT)
        return publicKey
    }

}