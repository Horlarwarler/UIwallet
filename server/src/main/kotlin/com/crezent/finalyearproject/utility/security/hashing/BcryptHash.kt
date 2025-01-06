package com.crezent.finalyearproject.utility.security.hashing

import org.mindrot.jbcrypt.BCrypt

class BcryptHash : HashingService {
    override fun hashValue(value: String, saltLength: Int): SaltedHash {

        val salt = BCrypt.gensalt()
        val hashed = BCrypt.hashpw(value, salt)
        return SaltedHash(
            salt = "",
            hashedValue = hashed
        )
    }

    override fun inputIsCorrect(value: String, saltedHash: SaltedHash): Boolean {
        return BCrypt.checkpw(
            value, saltedHash.hashedValue
        )
    }

}