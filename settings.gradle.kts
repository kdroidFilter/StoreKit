rootProject.name = "StoreKit"

pluginManagement {
    repositories {
        google {
            content { 
              	includeGroupByRegex("com\\.android.*")
              	includeGroupByRegex("com\\.google.*")
              	includeGroupByRegex("androidx.*")
              	includeGroupByRegex("android.*")
            }
        }
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            content { 
              	includeGroupByRegex("com\\.android.*")
              	includeGroupByRegex("com\\.google.*")
              	includeGroupByRegex("androidx.*")
              	includeGroupByRegex("android.*")
            }
        }
        mavenCentral()
    }
}
include(":gplay:scrapper")
include(":gplay:core")
include(":aptoide:core")
include(":aptoide:api")
include(":fdroid:core")
include(":fdroid:api")
include(":apkcombo:core")
include(":apkcombo:scraper")
include(":apklinkresolver:core")
include(":authenticity")
include(":sample")
