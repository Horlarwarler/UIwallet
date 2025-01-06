package com.crezent.finalyearproject.utility.security.hashing

import org.apache.commons.codec.binary.Hex
import org.apache.commons.codec.digest.DigestUtils
import java.security.SecureRandom

class Sha256Hash : HashingService {

   override fun hashValue(value: String, saltLength: Int): SaltedHash {
        val randomByteArray = SecureRandom.getInstance("SHA1PRNG").generateSeed(saltLength)
        val salt = Hex.encodeHexString(randomByteArray)
        val hashedValue = DigestUtils.sha256Hex("$salt$value")
        return SaltedHash(
            salt = salt,
            hashedValue = hashedValue
        )
    }

   override fun inputIsCorrect(value: String, saltedHash: SaltedHash): Boolean {
        return DigestUtils.sha256Hex(
            "${saltedHash.salt}$value"
        ) == saltedHash.hashedValue
    }
}