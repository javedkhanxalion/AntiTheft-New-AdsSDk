plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}


android {
    signingConfigs {
        create("release") {
            storeFile = file("$rootDir\\antitheifkeyNew.jks")
            storePassword = "antitheifkeyNew"
            keyAlias = "antitheifkeyNew"
            keyPassword = "antitheifkeyNew"
        }
    }
    namespace = "com.antitheftalarm.dont.touch.phone.finder.phonesecurity"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.antitheftalarm.dont.touch.phone.finder.phonesecurity"
        minSdk = 24
        targetSdk = 34
        versionCode = 24
        versionName = "3.4"
        multiDexEnabled = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        setProperty("archivesBaseName", "AntiThief_v$versionName($versionCode)")


    }


    buildTypes {
        debug {
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            resValue( "string", "id_application_id", "ca-app-pub-3940256099942544~3347511713")
            resValue ("string", "id_native", "")
            resValue ("string", "id_fullscreen_splash", "")
            resValue ("string", "app_open_splash1", "")
            resValue ("string", "app_open_splash", "")
        }
        release {
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            resValue( "string", "id_application_id", "ca-app-pub-3940256099942544~3347511713")
            resValue ("string", "id_native", "")
            resValue ("string", "id_fullscreen_splash", "")
            resValue ("string", "app_open_splash1", "")
            resValue ("string", "app_open_splash", "")
            signingConfig = signingConfigs.getByName("release")
        }
        buildFeatures {
            viewBinding = true
            buildConfig = true
        }
    }

    bundle {
        language {
            enableSplit = false
        }

    }
    lintOptions {
        isCheckReleaseBuilds = false
        isAbortOnError = false
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
// The default implementations
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.5")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.5")
// The Kotlin ones
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.5")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.5")
    implementation("com.intuit.sdp:sdp-android:1.1.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("com.airbnb.android:lottie:6.1.0")
    implementation("org.tensorflow:tensorflow-lite-task-audio:0.4.0")
// Ads Integration
//    implementation("com.android.billingclient:billing:6.1.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-common:2.6.2")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation("com.google.android.gms:play-services-ads:23.2.0")
    implementation(platform("com.google.firebase:firebase-bom:30.3.1"))
    implementation("com.google.firebase:firebase-config-ktx:21.6.0")
    implementation("com.google.firebase:firebase-analytics-ktx:21.5.0")
    implementation("com.google.firebase:firebase-messaging:23.4.1")
//    implementation("com.github.hypersoftdev:inappbilling:3.0.0-alpha-02")
    implementation("com.tbuonomo:dotsindicator:4.3")
    //shimmer
    implementation("com.facebook.shimmer:shimmer:0.5.0")
//    implementation("com.ikame.android-sdk:ikm-sdk-debug:2.6.530")
//    implementation ("com.ikame.android-sdk:pub-microzone-antithef-release:2.6.750-beta")
//    implementation("com.google.ads.interactivemedia.v3:interactivemedia:3.33.0")
    implementation("androidx.multidex:multidex:2.0.1")
    implementation ("com.google.code.gson:gson:2.8.8")
//    implementation ("com.applovin:applovin-sdk:12.1.0")
//    implementation ("net.pubnative:hybid.sdk:3.0.0")
//    implementation ("com.facebook.android:audience-network-sdk:6.+")
//    implementation ("com.unity3d.ads:unity-ads:4.7.0")
}


googleServices.disableVersionCheck = true