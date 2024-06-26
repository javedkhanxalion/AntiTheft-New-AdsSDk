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
        maven("https://jitpack.io")
        maven("https://jcenter.bintray.com")
        maven("https://artifact.bytedance.com/repository/pangle")
        maven("https://android-sdk.is.com/")
        maven("https://dl-maven-android.mintegral.com/repository/mbridge_android_sdk_oversea")
        maven("https://cboost.jfrog.io/artifactory/chartboost-ads/")
        maven("https://sdk.tapjoy.com")
        maven("https://s3.amazonaws.com/smaato-sdk-releases/")
        maven("https://artifactory.bidmachine.io/bidmachine")
        maven("https://verve.jfrog.io/artifactory/verve-gradle-release")
        maven("https://repository.ikameglobal.com/api/v4/projects/3/packages/maven")
        maven {
            url = uri("https://repository.ikameglobal.com/api/v4/projects/1/packages/maven")
            credentials(HttpHeaderCredentials::class) {
                name = "Private-Token"
                value = "ikame-zYxdkbXQBWe7p3vUy83U"
            }
            authentication {
                create<HttpHeaderAuthentication>("header")
            }
        }
        maven("https://maven.ogury.co")
    }

}

rootProject.name = "AntiTheif-Xalion"
include(":app")
