package com.crezent.finalyearproject

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

val x = 2