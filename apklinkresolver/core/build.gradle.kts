import com.vanniktech.maven.publish.SonatypeHost

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.vannitktech.maven.publish)
}

group = "io.github.kdroidfilter.storekit.apklinkresolver.core"
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
            implementation(libs.kotlinx.serialization.json)
            implementation(project(":apkcombo:core"))
            implementation(project(":aptoide:core"))
            implementation(project(":aptoide:api"))
            implementation(project(":apkcombo:scraper"))
            implementation(libs.kotlin.logging)
            compileOnly(libs.ktor.client.core)
            compileOnly(libs.ktor.client.content.negotiation)
            compileOnly(libs.ktor.client.serialization)
            compileOnly(libs.ktor.client.cio)
            compileOnly(libs.ktor.client.logging)

        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.serialization)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.cio)
        }

        androidUnitTest.dependencies {
            implementation(libs.kotlinx.coroutines.android)
        }

        jvmTest.dependencies {
            implementation(libs.kotlinx.coroutines.swing)
        }
    }

    //https://kotlinlang.org/docs/native-objc-interop.html#export-of-kdoc-comments-to-generated-objective-c-headers
    targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget> {
        compilations["main"].compilerOptions.options.freeCompilerArgs.add("-Xexport-kdoc")
    }
}

android {
    namespace = "io.github.kdroidfilter.storekit.apklinkresolver.core"
    compileSdk = 35

    defaultConfig {
        minSdk = 21
    }
}

mavenPublishing {
    coordinates(
        groupId = "io.github.kdroidfilter",
        artifactId = "storekit-apklinkresolver-core",
        version = version.toString()
    )

    pom {
        name.set("APK Link Resolver Library")
        description.set("Core module for APK Link Resolver Library containing model classes and services")
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
