import com.vanniktech.maven.publish.SonatypeHost

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.vannitktech.maven.publish)
}

group = "io.github.kdroidfilter.storekit.apkpure.core"
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
    wasmJs { browser() }

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
            implementation(libs.kotlinx.serialization.json)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }

    targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget> {
        compilations["main"].compilerOptions.options.freeCompilerArgs.add("-Xexport-kdoc")
    }
}

android {
    namespace = "io.github.kdroidfilter.storekit.apkpure.core"
    compileSdk = 35

    defaultConfig {
        minSdk = 21
    }
}

mavenPublishing {
    coordinates(
        groupId = "io.github.kdroidfilter",
        artifactId = "storekit-apkpure-core",
        version = version.toString()
    )

    pom {
        name.set("APKPure Core Library")
        description.set("Core module for APKPure Library containing model classes")
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

