import com.vanniktech.maven.publish.SonatypeHost

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.vannitktech.maven.publish)
}

group = "io.github.kdroidfilter.storekit.signature"
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
        }

        androidMain.dependencies {
            // Android-specific dependencies for signature extraction
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}

android {
    namespace = "io.github.kdroidfilter.storekit.signature"
    compileSdk = 35

    defaultConfig {
        minSdk = 21
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    dependencies {
        androidTestImplementation("androidx.test:core:1.6.1")
        androidTestImplementation("androidx.test:runner:1.6.2")
        androidTestImplementation("androidx.test:rules:1.6.1")
        androidTestImplementation("androidx.test.ext:junit:1.2.1")
        androidTestImplementation(libs.junit.junit)
    }
}

mavenPublishing {
    coordinates(
        groupId = "io.github.kdroidfilter",
        artifactId = "storekit-signature",
        version = version.toString()
    )

    pom {
        name.set("App Signature Library")
        description.set("Module for extracting app signatures in SHA1 format from installed Android applications")
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
