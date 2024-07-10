pluginManagement {
    repositories {
        google()
        mavenCentral()
        jcenter()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        jcenter()
        maven("https://artifacts.applovin.com/android")
        maven("https://jitpack.io")
        maven("https://jcenter.bintray.com")

    }

}

rootProject.name = "AntiTheif-Xalion"
include(":app")
