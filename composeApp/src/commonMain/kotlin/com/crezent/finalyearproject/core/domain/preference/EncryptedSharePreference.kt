package com.crezent.finalyearproject.core.domain.preference

interface EncryptedSharePreference {

    val userPin: String?

    fun setUserPin(pin: String)

    companion object {
        const val PIN = "pin"
    }

    fun deleteKey(key:String)

}