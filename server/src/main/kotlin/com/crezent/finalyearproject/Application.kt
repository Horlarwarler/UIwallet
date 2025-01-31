package com.crezent.finalyearproject

import com.crezent.finalyearproject.plugins.configureAuthentication
import com.crezent.finalyearproject.plugins.configureContentNegotiation
import com.crezent.finalyearproject.plugins.configureKoin
import com.crezent.finalyearproject.plugins.configureRoute
import com.crezent.finalyearproject.utility.security.encryption.KeyPairGenerator.exportPublicKeyToBase64
import com.crezent.finalyearproject.utility.security.encryption.KeyPairGenerator.generateKeyPair
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.client.plugins.contentnegotiation.*
import kotlinx.serialization.json.Json
import java.security.interfaces.ECPrivateKey
import java.security.interfaces.ECPublicKey


const val samsungHost = "192.168.47.175"
const val iphoneHost = "172.20.10.12"

//fun main() {
//    embeddedServer(Netty, port = SERVER_PORT, host = samsungHost, module = Application::module)
//        .start(wait = true)
//}
fun main(args: Array<String>): Unit = EngineMain.main(args)


fun Application.module() {
    configureKoin()
    configureAuthentication()


    configureContentNegotiation()
    configureRoute()

}


