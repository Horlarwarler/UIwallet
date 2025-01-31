import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("org.jetbrains.kotlin.multiplatform") version "2.1.0"
    id("com.android.library") version "8.7.3"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.1.0"

    //alias(libs.plugins.kotlin.plugin.serialization)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    
    jvm()
    
    sourceSets {
        commonMain.dependencies {
          //  implementation(libs.ktor.serialization.kotlinx.json)
            implementation("io.ktor:ktor-serialization-kotlinx-json:3.0.2")

            // put your Multiplatform dependencies here
        }

    }
}

android {
    namespace = "com.crezent.finalyearproject.shared"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}
