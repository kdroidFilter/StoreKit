import com.vanniktech.maven.publish.SonatypeHost

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.vannitktech.maven.publish)

}

group = "com.krdoid.gplayscrapper"
val ref = System.getenv("GITHUB_REF") ?: ""
val version = if (ref.startsWith("refs/tags/")) {
    val tag = ref.removePrefix("refs/tags/")
    if (tag.startsWith("v")) tag.substring(1) else tag
} else "dev"

kotlin {
    jvmToolchain(11)
    androidTarget {
        publishLibraryVariants("release")
    }

    jvm()


    linuxX64 {
        binaries.staticLib {
            baseName = "shared"
        }
    }


    mingwX64 {
        binaries.staticLib {
            baseName = "shared"
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "core"
            isStatic = true
        }
    }

    listOf(
        macosX64(),
        macosArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "core"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.serialization)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.cio)
            implementation(libs.ksoup.html)
            implementation(libs.ksoup.entities)
            implementation(libs.kotlin.logging)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
        }

        val jvmAndroidMain by creating {
            dependsOn(commonMain.get())
        }

        androidMain {
            dependsOn(jvmAndroidMain)
        }
        jvmMain {
            dependsOn(jvmAndroidMain)
        }

        androidMain.dependencies {
            implementation(libs.kotlinx.coroutines.android)
        }

        jvmMain.dependencies {
            implementation(libs.kotlinx.coroutines.swing)
            implementation(libs.slf4j.simple)

        }

    }

    //https://kotlinlang.org/docs/native-objc-interop.html#export-of-kdoc-comments-to-generated-objective-c-headers
    targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget> {
        compilations["main"].compilerOptions.options.freeCompilerArgs.add("-Xexport-kdoc")
    }

}

android {
    namespace = "com.krdoid.gplayscrapper"
    compileSdk = 35

    defaultConfig {
        minSdk = 21
    }
}

mavenPublishing {
    coordinates(
        groupId = "io.github.kdroidfilter",
        artifactId = "gplayscrapper",
        version = version.toString()
    )

    pom {
        name.set("GPlay Scrapper Library")
        description.set("GPlay Scrapper Library is a Kotlin library for extracting comprehensive app data from the Google Play Store.")
        inceptionYear.set("2024")
        url.set("https://github.com/kdroidFilter/GPlay-Scrapper")

        licenses {
            license {
                name.set("MIT")
                url.set("https://opensource.org/licenses/MIT")
            }
        }

        developers {
            developer {
                id.set("kdroid")
                name.set("Elyahou Hadass")
                email.set("elyahou.hadass@gmail.com")
            }
        }

        scm {
            url.set("https://github.com/kdroidFilter/GPlay-Scrapper")
        }
    }

    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    signAllPublications()
}
