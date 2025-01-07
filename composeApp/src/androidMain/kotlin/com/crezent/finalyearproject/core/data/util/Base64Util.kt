package com.crezent.finalyearproject.core.data.util

interface Base64Util  {

    fun ByteArray.encodeToString()

    fun String.toByteArray()
}