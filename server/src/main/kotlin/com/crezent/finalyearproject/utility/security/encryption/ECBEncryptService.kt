package com.crezent.finalyearproject.utility.security.encryption

import com.crezent.finalyearproject.TRANSFORMATION
import org.koin.core.logger.Logger
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.Security
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*
import javax.crypto.Cipher
import org.bouncycastle.jce.provider.BouncyCastleProvider


class ECBEncryptService : EncryptService {

    init {
        Security.addProvider(BouncyCastleProvider())
    }

    override fun decryptData(value: String, privateKeyString: String): String? {
        return try {

            val privateKeyBytes = Base64.getDecoder().decode(privateKeyString)
            val privateKeySpec = PKCS8EncodedKeySpec(privateKeyBytes)
            val keyFactory = KeyFactory.getInstance("EC")
            val privateKey = keyFactory.generatePrivate(privateKeySpec) //Here is error happern
            Security.getProviders().forEach {
                println("Provider ${it.name}")
            }
            val cipher = Cipher.getInstance(TRANSFORMATION,"AndroidKeyStore")

            //   val cipher = Cipher.getInstance(TRANSFORMATION)
            cipher.init(Cipher.DECRYPT_MODE, privateKey!!)
            val decoded = cipher.doFinal(Base64.getDecoder().decode(value))
            String(decoded, Charsets.UTF_8)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    //MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA7H6dMObJlo5nb3PuLpa9
    //A4G26Wc+NM1eD5WzAHzJgb0N+PGZ0mxB8SrgBc9xbapCkeWKtJCXBXrt6jPd/aUB
    //wxGBIbcxWXoYDDpjYNFwIUnAohU1pRo8uA9QJ/ZnJAkDhZjuX65YlJqPV1yyyK8f
    //yH5YwrCdkPulP7PHOp8EtGxDiVaqahZJSPMCe/U7H2IrnsHHyDBaOnSPvN8MXFbN
    //O+UmVm0HXufP54llGMpiBilx+WcBNo+8+vx2gsWA2NeZwvv7N71oCBKpbLswakqj
    //92aksAhD034tITq4BsL8aQbMmHTVMfaMjoea9IkkAELSHAZqlNwqQP7FaZ51Fv5n
    //6QIDAQAB

    override fun encryptData(value: String, publicKeyString: String): String? {
        return try {
            val publicKeyBytes = Base64.getDecoder().decode(publicKeyString)
            val publicKeySpec = X509EncodedKeySpec(publicKeyBytes)
            val keyFactory = KeyFactory.getInstance("EC")
            val publicKey = keyFactory.generatePublic(publicKeySpec)

            //  val cipher = Cipher.getInstance(TRANSFORMATION)
            val cipher = Cipher.getInstance(TRANSFORMATION)

            cipher.init(Cipher.ENCRYPT_MODE, publicKey)
            val decipher = cipher.doFinal(value.toByteArray(Charsets.UTF_8))
            Base64.getEncoder().encodeToString(decipher)

        } catch (e: Exception) {
            println("Exception while deciphering $e")
            null
        }

    }
}