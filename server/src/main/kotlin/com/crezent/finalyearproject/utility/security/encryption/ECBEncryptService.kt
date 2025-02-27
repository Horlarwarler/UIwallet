package com.crezent.finalyearproject.utility.security.encryption

import com.crezent.finalyearproject.AES_TRANSFORMATION
import com.crezent.finalyearproject.CARD_AES_TRANSFORMATION
import com.crezent.finalyearproject.RSA_TRANSFORMATION
import com.crezent.finalyearproject.models.EncryptionKeyValue
import kotlinx.io.bytestring.decode
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.InvalidKeyException
import java.security.KeyFactory
import java.security.SecureRandom
import java.security.Security
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*
import javax.crypto.BadPaddingException
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
    override fun decryptData(
        aesEncryptedString: String,
        rsaPrivateKeyString: String,
        rsaEncryptedKey: String,
    ): String? {
        return try {
            //val ecPrivateKey =

            //Try to decrypt the aes key with the rsa private key
            // if it fails then we try to decrypt the aes key with the ec private key
            // if it fails then we return null

            decodeWithRsaPublicKey(
                rsaEncryptedKey = rsaEncryptedKey,
                rsaPrivateKeyString = rsaPrivateKeyString,
                aesEncryptedString = aesEncryptedString
            )
            //decryptedData


        } catch (e: Exception) {
            println(e.message)
            println(e.cause)
            e.printStackTrace()
            null
        }
    }

    override fun encryptData(value: String, clientRsapublicKey: String): EncryptionKeyValue? {
        return try {

            val rsaPublicKeyBytes = org.bouncycastle.util.encoders.Base64.decode(clientRsapublicKey)
            val rsaPublicKeySpec = X509EncodedKeySpec(rsaPublicKeyBytes)

            val keyFactory = KeyFactory.getInstance("RSA")


            val rsaPublicKey = keyFactory.generatePublic(rsaPublicKeySpec) /// THE ERROR OCCURS HERE
            //THIS IS THE ERROR MESSAGE java.security.InvalidKeyException: Missing key encoding

            //  val cipher = Cipher.getInstance(TRANSFORMATION)
            val cipher = Cipher.getInstance(RSA_TRANSFORMATION)

            cipher.init(Cipher.ENCRYPT_MODE, rsaPublicKey)

            val aesPublicKey = generateAesPublicKey()
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

    private fun decodeWithRsaPublicKey(
        rsaPrivateKeyString: String,
        aesEncryptedString: String,
        rsaEncryptedKey: String
    ): String? {
        try {
            val privateKeyBytes = org.bouncycastle.util.encoders.Base64.decode(rsaPrivateKeyString)


            val privateKeySpec = PKCS8EncodedKeySpec(privateKeyBytes)

            val keyFactory = KeyFactory.getInstance("RSA")
            val privateKey = keyFactory.generatePrivate(privateKeySpec)
            val cipher = Cipher.getInstance(RSA_TRANSFORMATION)

            println("CIPHER ${cipher.algorithm} c${cipher} and padding${cipher.blockSize}")
            cipher.init(Cipher.DECRYPT_MODE, privateKey!!)
            val keyToDecode = org.bouncycastle.util.encoders.Base64.decode(rsaEncryptedKey)
            println("Key to decode byte is $keyToDecode")
            val decodedAesKey = cipher.doFinal(keyToDecode) //// Here the code is the throwing error
            return getMessageFromByteArray(
                byteArray = decodedAesKey,
                aesEncryptedString = aesEncryptedString
            )

        } catch (error: InvalidKeyException) {
            error.printStackTrace()
            return null
//            println("Error while decrypting with RSA key $error")
//            println("Will try to decrypt with EC key")
//            val ecPrivateKeyBytes = org.bouncycastle.util.encoders.Base64.decode(ecPrivateKeyString)
//            val ecPrivateKeySpec = PKCS8EncodedKeySpec(ecPrivateKeyBytes)
//            val keyFactory = KeyFactory.getInstance("EC")
//            val ecPrivateKey = keyFactory.generatePrivate(ecPrivateKeySpec)
//            val decryptedData = decryptIos(
//                ciphertext = aesEncryptedString,
//                privateKeyBytes = ecPrivateKeyBytes
//            )
//            return String(decryptedData)
        } catch (error: BadPaddingException) {
            error.printStackTrace()
            return null
        } catch (error: Exception) {
            error.printStackTrace()
            return null
        }
    }

    private fun getMessageFromByteArray(
        byteArray: ByteArray,
        aesEncryptedString: String
    ): String {
        val originalKey: SecretKey = SecretKeySpec(byteArray, "AES")
        val splitText = aesEncryptedString.split(":")
        val partOne = splitText[0]
        val partTwo = splitText[1]
        println("Part one is $partOne")
        println("Part two is $partTwo")

        val iv = Base64.getDecoder().decode(splitText[1])

        val gcmParameterSpec = GCMParameterSpec(128, iv)
        val aesCipher = Cipher.getInstance(AES_TRANSFORMATION)
        aesCipher.init(Cipher.DECRYPT_MODE, originalKey, gcmParameterSpec)

        val byte = Base64.getDecoder().decode(partOne)
        println(byte)
        val messageByte = aesCipher.doFinal(byte)
        val message = String(messageByte, Charsets.UTF_8)

        return message
    }

    override fun decryptCardDetails(data: String, key: String, encodedIV: String): String? {
        val iv = Base64.getDecoder().decode(encodedIV)
        val encryptedBytes = Base64.getDecoder().decode(data)
        val decodedKey = org.bouncycastle.util.encoders.Base64.decode(key)

        // Create the secret key spec
        val secretKey = SecretKeySpec(decodedKey, "AES")

        // Initialize the cipher in decryption mode with the key and IV
        val cipher = Cipher.getInstance(AES_TRANSFORMATION)
        val gcmSpec = GCMParameterSpec(128, iv) // 128-bit authentication tag
        cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmSpec)

        // Decrypt the data
        val decryptedBytes = cipher.doFinal(encryptedBytes)

        // Convert the decrypted bytes to a string
        return String(decryptedBytes)

    }


    override fun encryptCardDetails(data: String, key: String): Pair<String, String> {
        val iv = ByteArray(12)
        SecureRandom().nextBytes(iv)
        println("Key is \"$key\"")
        val base64Decoded = org.bouncycastle.util.encoders.Base64.decode(key)
        println("Key length is ${base64Decoded.size}")
        val secretKey = SecretKeySpec(base64Decoded, "AES")
        val gcmSpec = GCMParameterSpec(128, iv)
        val cipher = Cipher.getInstance(AES_TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, gcmSpec)
        // Encrypt the data
        val encryptedBytes = cipher.doFinal(data.toByteArray())

        // Encode the encrypted data and IV to Base64 for storage
        val encryptedData = Base64.getEncoder().encodeToString(encryptedBytes)
        val encodedIV = Base64.getEncoder().encodeToString(iv)

        // Return the encrypted data and IV as a Pair
        return Pair(encryptedData, encodedIV)

    }




}