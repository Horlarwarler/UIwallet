import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlin.plugin.serialization)
   //  alias(libs.plugins.swiftklib)
    id("org.jetbrains.kotlin.native.cocoapods") version "2.1.0"




}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "composeApp"
            isStatic = true
        }
    }


    cocoapods {
        version = "2.0"
        name = "MyCocoaPod"
        summary = "Some description for the Kotlin Multiplatform Module"
        ios.deploymentTarget = "17.5"
        homepage = "Link to the project homepage"

        framework {
            baseName = "composeApp"  // ⚠️ THIS LINE IS VERY IMPORTANT //
            isStatic = true
        }



   //   xcodeConfigurationToNativeBuildType["CUSTOM_RELEASE"] = NativeBuildType.RELEASE
    }

    sourceSets {

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.ktor.client.android)
            implementation(libs.androidx.security.crypto)
            implementation(libs.koin.android)
            // implementation(libs.bcprov.jdk15on)

            implementation(libs.bcprov.jdk15to18)


        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            //implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.material3)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(compose.components.resources)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.jetbrains.compose.navigation)
            //  implementation(libs.androidx.navigation.compose)
            implementation(projects.shared)

            //Ktor
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.auth)

            //Koin
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            // implementation(libs.koin.compose.viemodel.navigation)

            //Others
            implementation(libs.napier)
            //  implementation(libs.connectivity.status)
            implementation(libs.k.vault)
            implementation(libs.kottie)
            implementation(libs.multiplatform.settings)
            implementation(libs.kotlinx.date.time)
            implementation(libs.kotlin.coroutines)

            implementation("com.plusmobileapps:konnectivity:0.1-alpha01")


            // implementation(libs.koin.compose.viemodel.navigaation)


        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
    }
}

android {
    namespace = "com.crezent.finalyearproject"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.crezent.finalyearproject"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    // implementation(libs.androidx.navigation.compose)
    debugImplementation(compose.uiTooling)
}
