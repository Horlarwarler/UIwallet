package com.crezent.uiwallet

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform