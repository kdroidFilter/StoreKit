// apkpure/api/build.gradle.kts
import com.vanniktech.maven.publish.SonatypeHost
import kotlin.toString

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.vannitktech.maven.publish)
}

group = "io.github.kdroidfilter.storekit.apkpure.api"
val ref = System.getenv("GITHUB_REF") ?: ""
val version = if (ref.startsWith("refs/tags/")) {
    val tag = ref.removePrefix("refs/tags/")
    if (tag.startsWith("v")) tag.substring(1) else tag
} else "dev"

kotlin {
    jvmToolchain(17)
    androidTarget {
        publishLibraryVariants("release")
    }

    jvm()

    sourceSets {
        commonMain.dependencies {
            api(project(":apkpure:core"))
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.kotlinx.serialization.json)
            compileOnly(libs.ktor.client.core)
            compileOnly(libs.ktor.client.content.negotiation)
            compileOnly(libs.ktor.client.serialization)
            compileOnly(libs.ktor.client.logging)
            compileOnly(libs.ktor.client.cio)
            implementation(libs.kotlin.logging)
            implementation(libs.ksoup.html)
            implementation(libs.ksoup.entities)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.serialization)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.cio)
        }

        androidMain.dependencies {
            implementation(libs.kotlinx.coroutines.android)
        }

        jvmMain.dependencies {
            implementation(libs.kotlinx.coroutines.swing)
            implementation(libs.slf4j.simple)
        }
    }

    targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget> {
        compilations["main"].compilerOptions.options.freeCompilerArgs.add("-Xexport-kdoc")
    }
}

android {
    namespace = "io.github.kdroidfilter.storekit.apkpure.api"
    compileSdk = 35

    defaultConfig {
        minSdk = 21
    }
}

mavenPublishing {
    coordinates(
        groupId = "io.github.kdroidfilter",
        artifactId = "storekit-apkpure-api",
        version = version.toString()
    )

    pom {
        name.set("APKPure API Library")
        description.set("APKPure Library is a Kotlin library for extracting comprehensive app data from APKPure.")
        inceptionYear.set("2024")
        url.set("https://github.com/kdroidFilter/StoreKit/")

        licenses {
            license {
                name.set("MIT License")
                url.set("https://opensource.org/licenses/MIT")
            }
        }

        developers {
            developer {
                id.set("kdroidFilter")
                name.set("Elie Gambache")
                email.set("elyahou.hadass@gmail.com")
            }
        }

        scm {
            connection.set("scm:git:git://github.com/kdroidFilter/StoreKit.git")
            developerConnection.set("scm:git:ssh://git@github.com:kdroidFilter/StoreKit.git")
            url.set("https://github.com/kdroidFilter/StoreKit/")
        }
    }

    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    signAllPublications()
}