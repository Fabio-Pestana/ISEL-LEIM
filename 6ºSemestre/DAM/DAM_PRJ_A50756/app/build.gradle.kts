plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    id("com.google.gms.google-services")
    id("org.jetbrains.kotlin.plugin.serialization") version "2.2.0-RC"
}

android {
    namespace = "dam_A50756.gymwiki"
    compileSdk = 35

    defaultConfig {
        applicationId = "dam_A50756.gymwiki"
        minSdk = 25
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    implementation(libs.androidx.navigation.runtime.android)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation("androidx.navigation:navigation-compose:2.9.0")
    implementation("androidx.compose.material3:material3:1.3.2")
    implementation(platform("com.google.firebase:firebase-bom:33.14.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth")

    implementation("com.google.firebase:firebase-storage-ktx:20.3.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("io.coil-kt:coil-compose:2.5.0")
    implementation("com.google.accompanist:accompanist-permissions:0.34.0")

    implementation("com.google.firebase:firebase-firestore:25.1.4")

    implementation("com.pierfrancescosoffritti.androidyoutubeplayer:core:12.0.0")

    implementation("com.google.code.gson:gson:2.10.1")

    implementation("androidx.compose.animation:animation:1.6.0")


    //GMS
    //implementation("com.google.android.gms:play-services-auth:21.3.0")
    implementation(libs.play.services.auth)

    //implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    //implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")

    implementation("io.coil-kt.coil3:coil-network-okhttp:3.2.0") // Only available on Android/JVM.
    implementation("io.coil-kt.coil3:coil-network-ktor3:3.2.0")
    implementation("io.ktor:ktor-client-core:3.1.0")
    implementation("io.ktor:ktor-client-plugins:3.1.1")//
}