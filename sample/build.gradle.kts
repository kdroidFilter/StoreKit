plugins {
    alias(libs.plugins.multiplatform)
}

group = "io.github.kdroidfilter.androidappstorekit.sample"
version = "1.0-SNAPSHOT"

kotlin {
    jvmToolchain(17)
    jvm()
    
    sourceSets {
        jvmMain.dependencies {
            // Google Play dependencies
            implementation(project(":gplay:scrapper"))

            // Aptoide dependencies
            implementation(project(":aptoide:api"))

            // Ktor dependencies
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.serialization)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.cio)
            
            // Kotlinx dependencies
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
            
            // Logging
            implementation(libs.kotlin.logging)
            implementation(libs.slf4j.simple)
        }
    }
}
