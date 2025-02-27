package com.crezent.finalyearproject.utility.security.encryption

import org.apache.commons.codec.binary.Base64
import java.io.ByteArrayOutputStream
import java.nio.charset.Charset
import java.security.*
import java.security.spec.InvalidKeySpecException
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*
import javax.crypto.Cipher


object OpayEncryptionService {

    private const val OPAY_RSA_TRANSFORMATION = "RSA/ECB/PKCS1Padding"
    private const val MAX_ENCRYPT_BYTE = 117
    private const val MAX_DECRYPT_TYPE = 128
    private const val RSA_KEY = "key"


    fun encryptWithOpayPublicKey(input: String, rsaPublicKey: String): String {
        var result = ""
        try {
            val buffer = Base64().decode(rsaPublicKey)
            val keyFactory: KeyFactory = KeyFactory.getInstance(RSA_KEY)
            val keySpec = X509EncodedKeySpec(buffer)
            val publicKey: PublicKey = keyFactory.generatePublic(keySpec)
            val cipher = Cipher.getInstance(OPAY_RSA_TRANSFORMATION)
            cipher.init(Cipher.ENCRYPT_MODE, publicKey)
            val inputArray = input.toByteArray()
            val inputLength = inputArray.size

            var offSet = 0
            var resultBytes = byteArrayOf()
            var cache: ByteArray
            while (inputLength - offSet > 0) {
                if (inputLength - offSet > MAX_ENCRYPT_BYTE) {
                    cache = cipher.doFinal(inputArray, offSet, MAX_ENCRYPT_BYTE)
                    offSet += MAX_ENCRYPT_BYTE
                } else {
                    cache = cipher.doFinal(inputArray, offSet, inputLength - offSet)
                    offSet = inputLength
                }
                resultBytes = Arrays.copyOf(resultBytes, resultBytes.size + cache.size)
                System.arraycopy(cache, 0, resultBytes, resultBytes.size - cache.size, cache.size)
            }
            println("ENCRYPT OPAY $result")
            result = Base64().encodeToString(resultBytes)
        } catch (e: Exception) {
            println("rsaEncrypt error:" + e.message)
        }
        println("encryptData：$result")
        return result
    }

    @Throws(
        NoSuchAlgorithmException::class,
        InvalidKeySpecException::class,
        InvalidKeyException::class,
        SignatureException::class
    )
    fun signBySHA256withRSA(data: String, privateKey: String): String {
        val signature: Signature = Signature.getInstance("SHA256withRSA")
        val timestamp = System.currentTimeMillis().toString()
        val signOrder = data + timestamp
        val keyBytes = privateKey.toByteArray(Charset.defaultCharset())
        val encodedKey: ByteArray = java.util.Base64.getDecoder().decode(keyBytes)
        val keySpec = PKCS8EncodedKeySpec(encodedKey)
        val priKey = KeyFactory.getInstance("RSA").generatePrivate(keySpec)
        signature.initSign(priKey)
        signature.update(signOrder.toByteArray(Charset.defaultCharset()))
        val signed: ByteArray = signature.sign()
        return java.util.Base64.getEncoder().encodeToString(signed)
    }

    @Throws(
        NoSuchAlgorithmException::class,
        InvalidKeySpecException::class,
        InvalidKeyException::class,
        SignatureException::class
    )
    fun verifySHA256withRSA(
        data: String,
        signatureToBeVerified: String,
        publicKey: String
    ): Boolean {
        val signature = Signature.getInstance("SHA256withRSA")
        val encodedKey = Base64.decodeBase64(publicKey.toByteArray(Charset.defaultCharset()))
        val keySpec = X509EncodedKeySpec(encodedKey)
        val pubKey = KeyFactory.getInstance("RSA").generatePublic(keySpec)

        signature.initVerify(pubKey)
        signature.update(data.toByteArray(Charset.defaultCharset()))

        return signature.verify(Base64.decodeBase64(signatureToBeVerified))
    }

    @Throws(java.lang.Exception::class)
    fun bigDecryptByPrivateKey(text: String, privateKey: String): String {
        val keyBytes = Base64().decode(privateKey)
        val keySpec = PKCS8EncodedKeySpec(keyBytes)
        val keyFactory = KeyFactory.getInstance(RSA_KEY)
        val privateK = keyFactory.generatePrivate(keySpec)
        val cipher = Cipher.getInstance(OPAY_RSA_TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE, privateK)
        val encryptedData = Base64().decode(text.toByteArray())
        val inputLen = encryptedData.size
        println(inputLen)
        val out = ByteArrayOutputStream()
        var offSet = 0
        var cache: ByteArray
        var i = 0
        while (inputLen - offSet > 0) {
            cache = if (inputLen - offSet > MAX_DECRYPT_TYPE) {
                cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_TYPE)
            } else {
                cipher.doFinal(encryptedData, offSet, inputLen - offSet)
            }
            out.write(cache, 0, cache.size)
            i++
            offSet = i * MAX_DECRYPT_TYPE
        }
        val decryptedData = out.toByteArray()
        out.close()
        return String(decryptedData)
    }

}