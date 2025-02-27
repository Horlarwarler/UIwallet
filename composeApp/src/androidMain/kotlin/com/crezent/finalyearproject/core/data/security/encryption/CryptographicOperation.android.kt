package com.crezent.finalyearproject.core.data.security.encryption

import android.os.Build
import android.security.keystore.KeyProperties
import androidx.annotation.RequiresApi
import com.crezent.finalyearproject.AES_TRANSFORMATION
import com.crezent.finalyearproject.ANDROID_KEY_STORE
import com.crezent.finalyearproject.EC_ALIAS
import com.crezent.finalyearproject.RSA_ALIAS
import com.crezent.finalyearproject.SIGNATURE_ALGORITHM
import com.crezent.finalyearproject.RSA_TRANSFORMATION
import com.crezent.finalyearproject.core.data.util.JavaBase64Util
import com.crezent.finalyearproject.models.EncryptionKeyValue
import java.security.KeyFactory
import java.security.KeyStore
import java.security.KeyStore.PrivateKeyEntry
import java.security.PublicKey
import java.security.Signature
import java.security.spec.X509EncodedKeySpec
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.io.encoding.ExperimentalEncodingApi


class AndroidCryptographicOperation : CryptographicOperation {

    val base64Util = JavaBase64Util()

    private val keyStore = KeyStore.getInstance(ANDROID_KEY_STORE).apply {
        load(null)
    }

    private val rsaKeyEntry by lazy {
        keyStore.getEntry(RSA_ALIAS, null) as? PrivateKeyEntry

    }

    private val ecKeyEntry by lazy {
        keyStore.getEntry(EC_ALIAS, null) as? PrivateKeyEntry

    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun encryptData(serverPublicKey: String, data: String): EncryptionKeyValue {
        val publicKey = decodePublicKey(serverPublicKey, KeyProperties.KEY_ALGORITHM_RSA)
        val aesPublicKey = generateAesPublicKey()
        val aesCipher = Cipher.getInstance(AES_TRANSFORMATION)
        aesCipher.init(Cipher.ENCRYPT_MODE, aesPublicKey)
        val iv = aesCipher.iv
        val aesEncryptedValue = aesCipher.doFinal(data.toByteArray())
        println(aesEncryptedValue)
        val aesEncrypted =
            Base64.getEncoder().encodeToString(aesEncryptedValue) + ":" + Base64.getEncoder()
                .encodeToString(iv)
        val cipher = Cipher.getInstance(RSA_TRANSFORMATION)
        cipher.init(Cipher.PUBLIC_KEY, publicKey)
        val encrypted = cipher.doFinal(aesPublicKey.encoded)
        val rsaEncryptedKey = Base64.getEncoder().encodeToString(encrypted)
        return EncryptionKeyValue(
            aesEncryptedString = aesEncrypted,
            rsaEncryptedKey = rsaEncryptedKey
        )


    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun decryptData(encryptedData: String, encryptedAesKey: String): String {
        val cipher = Cipher.getInstance(RSA_TRANSFORMATION)
        val privateKey = rsaKeyEntry?.privateKey

        cipher.init(Cipher.DECRYPT_MODE, privateKey)
        val decodedAesByte = org.bouncycastle.util.encoders.Base64.decode(encryptedAesKey)
        val decryptedAesKey = cipher.doFinal(decodedAesByte)
        val originalKey: SecretKey = SecretKeySpec(decryptedAesKey, "AES")

        val parts = encryptedData.split(":")
        val mainPart = org.bouncycastle.util.encoders.Base64.decode(parts[0])
        val iv = org.bouncycastle.util.encoders.Base64.decode(parts[1])
        val gcmParameterSpec = GCMParameterSpec(128, iv)

        val aesCipher = Cipher.getInstance(AES_TRANSFORMATION)
        aesCipher.init(Cipher.DECRYPT_MODE, originalKey, gcmParameterSpec)

        val messageByte =
            aesCipher.doFinal(mainPart)
        val message = String(messageByte, Charsets.UTF_8)
        return message


    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun signData(dataToSign: String): String {
        try {

            val signature = Signature.getInstance(SIGNATURE_ALGORITHM)
            val privateKey = ecKeyEntry?.privateKey
            signature.initSign(privateKey)
            signature.update(dataToSign.toByteArray())
            return base64Util.encodeToString(signature.sign())
            //    return Base64.encodeToString(signature.sign(), Base64.DEFAULT)
        } catch (error: Exception) {
            error.printStackTrace()
            throw Exception()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun verifySignature(
        signature: String,
        dataToVerify: String,
        publicKey: String
    ): Boolean {
        val publicKeyInstance = decodePublicKey(publicKey, KeyProperties.KEY_ALGORITHM_EC)
        val signatureInstance = Signature.getInstance(SIGNATURE_ALGORITHM)
        signatureInstance.initVerify(publicKeyInstance)
        signatureInstance.update(dataToVerify.toByteArray())
        return signatureInstance.verify(org.bouncycastle.util.encoders.Base64.decode(signature))

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun decodePublicKey(publicKeyString: String, algorithm: String): PublicKey {
        val decodedByte = org.bouncycastle.util.encoders.Base64.decode(publicKeyString)
        val keyFactory = KeyFactory.getInstance(algorithm)
        val keySpec = X509EncodedKeySpec(decodedByte)
        return keyFactory.generatePublic(keySpec)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun generateAesPublicKey(): SecretKey {

        val keyPairGenerator = KeyGenerator.getInstance("AES")
        keyPairGenerator.init(128)
        val keyPair = keyPairGenerator.generateKey()
        return keyPair

    }

}