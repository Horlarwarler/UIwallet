plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlin.plugin.serialization)
    application
}

group = "com.crezent.finalyearproject"
version = "1.0.0"
application {
    mainClass.set("com.crezent.finalyearproject.ApplicationKt")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=true")
}

dependencies {
    implementation(projects.shared)
    implementation(libs.logback)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.client.core)
    implementation("io.ktor:ktor-client-cio-jvm:3.0.2")
    implementation("io.ktor:ktor-client-okhttp-jvm:3.0.2")
    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.kotlin.test.junit)

    implementation(libs.ktor.server.rate.limiting)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.server.sessions)
    implementation(libs.ktor.server.csrf)
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.auth.jwt)
    implementation(libs.ktor.server.websockets)
    implementation(libs.koin.ktor)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.core.jvm)
    implementation(libs.koin.logger.slf4j)
    implementation(libs.mongodb.driver.core)
    implementation(libs.mongodb.driver.sync)
    implementation("org.mongodb:mongodb-driver-kotlin-coroutine:4.10.1")

    implementation(libs.bson)
    implementation(libs.ktor.server.call.logging)
    implementation(libs.ktor.server.cors)
    implementation(libs.ktor.server.config.yaml)
    implementation(libs.common.codec)
    implementation(libs.bcrypt)

    implementation(libs.ktor.serialization.kotlinx.json)


    implementation(libs.bcprov.jdk15on)


    //Authentication
    // implementation(libs.authent)
}