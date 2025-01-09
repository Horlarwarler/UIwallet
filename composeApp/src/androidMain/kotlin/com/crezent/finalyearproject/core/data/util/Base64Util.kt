package com.crezent.finalyearproject.core.data.util

interface Base64Util  {

    fun encodeToString(byteArray: ByteArray) :String

    fun decodeToByteArray(enCodedString: String) :ByteArray
}