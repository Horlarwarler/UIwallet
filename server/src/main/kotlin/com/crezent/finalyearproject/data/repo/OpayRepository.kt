package com.crezent.finalyearproject.data.repo

import org.apache.commons.codec.binary.Hex
import java.nio.charset.StandardCharsets
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec


class OpayRepository {

    @Throws(Exception::class)
    fun hmacSHA512(data: String, secureKey: String): String {
        val bytesKey = secureKey.toByteArray()
        val secretKey = SecretKeySpec(bytesKey, "HmacSHA512")
        val mac: Mac = Mac.getInstance("HmacSHA512")
        mac.init(secretKey)
        val macData: ByteArray = mac.doFinal(data.toByteArray())
        val hex: ByteArray = Hex().encode(macData)
        return String(hex, StandardCharsets.UTF_8)
    }
}