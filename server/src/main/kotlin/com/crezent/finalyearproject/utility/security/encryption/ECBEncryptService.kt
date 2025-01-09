package com.crezent.finalyearproject.utility.security.encryption

import com.crezent.finalyearproject.AES_TRANSFORMATION
import com.crezent.finalyearproject.TRANSFORMATION
import com.crezent.finalyearproject.models.EncryptionKeyValue
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.KeyFactory
import java.security.Security
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.io.encoding.ExperimentalEncodingApi


class ECBEncryptService : EncryptService {

    init {
        Security.addProvider(BouncyCastleProvider())
    }

    @OptIn(ExperimentalEncodingApi::class)
    override fun decryptData(
        aesEncryptedString: String,
        privateKeyString: String,
        rsaEncryptedKey: String
    ): String? {
        return try {

            // We decrypt the AES Key

            // We use the private key from the server to  decrypt rsa encrypted
            println(privateKeyString)

            val privateKeyBytes = org.bouncycastle.util.encoders.Base64.decode(privateKeyString)
            val privateKeySpec = PKCS8EncodedKeySpec(privateKeyBytes)

            val keyFactory = KeyFactory.getInstance("RSA")
            val privateKey = keyFactory.generatePrivate(privateKeySpec)

            val cipher = Cipher.getInstance(TRANSFORMATION)

            println("CIPHER ${cipher.algorithm} c${cipher} and padding${cipher.blockSize}")
            cipher.init(Cipher.DECRYPT_MODE, privateKey!!)

            val keyToDecode = org.bouncycastle.util.encoders.Base64.decode(rsaEncryptedKey)
            println("Key to code is ${keyToDecode.size}")

            val decodedAesKey = cipher.doFinal(keyToDecode)

            val decodedAesAsString = org.bouncycastle.util.encoders.Base64.toBase64String(decodedAesKey)

            println("Decoded aes $decodedAesAsString")

            val originalKey: SecretKey = SecretKeySpec(decodedAesKey, "AES")
            val splitText = aesEncryptedString.split(":")
            val partOne = splitText[0]
            val partTwo = splitText[1]
            println("Part one is $partOne")
            println("Part two is $partTwo")
            val mainPart = Base64.getDecoder().decode(splitText[0])


            val iv = Base64.getDecoder().decode(splitText[1])

            val gcmParameterSpec = GCMParameterSpec(128, iv)
            val aesCipher = Cipher.getInstance(AES_TRANSFORMATION)
            aesCipher.init(Cipher.DECRYPT_MODE, originalKey, gcmParameterSpec)

            val byte = Base64.getDecoder().decode(partOne)
            println(byte)
            val messageByte = aesCipher.doFinal(byte)
            val message = String(messageByte, Charsets.UTF_8)
            println(message)
            message


        } catch (e: Exception) {
            println(e.message)
            println(e.cause)
            e.printStackTrace()
            null
        }
    }


    override fun encryptData(value: String, clientRsapublicKey: String): EncryptionKeyValue? {
        return try {

            val aesPublicKey = generateAesPublicKey()

            val rsaPublicKeyBytes = org.bouncycastle.util.encoders.Base64.decode(clientRsapublicKey)


            val testString =   org.bouncycastle.util.encoders.Base64.toBase64String(aesPublicKey.encoded)
            println("Test $testString")
            val rsaPublicKeySpec = X509EncodedKeySpec(rsaPublicKeyBytes)
            val keyFactory = KeyFactory.getInstance("RSA")


            val rsaPublicKey = keyFactory.generatePublic(rsaPublicKeySpec)

            //  val cipher = Cipher.getInstance(TRANSFORMATION)
            val cipher = Cipher.getInstance(TRANSFORMATION)

            cipher.init(Cipher.ENCRYPT_MODE, rsaPublicKey)

            val aesEncryptedKeyByte = cipher.doFinal(aesPublicKey.encoded)


            val aesCipher = Cipher.getInstance(AES_TRANSFORMATION)


            //Encrypt the generated AES key with the RSA public key

            aesCipher.init(Cipher.ENCRYPT_MODE, aesPublicKey)
            val iv = aesCipher.iv
            val aesEncryptedValue = aesCipher.doFinal(value.toByteArray())


            val aesEncrypted =
                org.bouncycastle.util.encoders.Base64.toBase64String(aesEncryptedValue) + ":" + org.bouncycastle.util.encoders.Base64.toBase64String(
                    iv
                )

            val encryptedKey = org.bouncycastle.util.encoders.Base64.toBase64String(aesEncryptedKeyByte)
            EncryptionKeyValue(
                aesEncryptedString = aesEncrypted,
                rsaEncryptedKey = encryptedKey
            )

        } catch (e: Exception) {
            println("Exception while deciphering $e")
            null
        }

    }

    private fun generateAesPublicKey(): SecretKey {

        val keyPairGenerator = KeyGenerator.getInstance("AES")
        keyPairGenerator.init(128)
        val keyPair = keyPairGenerator.generateKey()
        return keyPair

    }
}