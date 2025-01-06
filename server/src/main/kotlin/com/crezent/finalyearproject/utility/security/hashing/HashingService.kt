package com.crezent.finalyearproject.utility.security.hashing

import org.apache.commons.codec.binary.Hex
import org.apache.commons.codec.digest.DigestUtils
import java.security.SecureRandom
import java.security.Security
import java.util.Base64

interface HashingService {

    fun hashValue(value: String, saltLength: Int = 32): SaltedHash

    fun inputIsCorrect(value: String, saltedHash: SaltedHash): Boolean
}