plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.kotlinx.serialization)
}

group = "io.github.kdroidfilter.storekit.sample"
version = "1.0-SNAPSHOT"

kotlin {
    jvmToolchain(17)
    jvm {
        // Configure JVM target
        mainRun {
            mainClass.set("io.github.kdroidfilter.storekit.sample.MainKt")
        }
    }

    sourceSets {
        jvmMain.dependencies {
            implementation(project(":gplay:scrapper"))
            implementation(project(":aptoide:api"))
            implementation(project(":fdroid:api"))
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.coroutines.swing)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.serialization)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.cio)
        }
    }
}
