import com.android.build.api.variant.impl.VariantOutputImpl

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.android.dagger.hilt)
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.jetbrains.kotlin.serialization)
    alias(libs.plugins.google.gms.services)
}

val vMajor = 1
val vMinor = 1
val vPatch = 0
val isAlpha = false

android {
    namespace = "com.weberpackage.blackjack"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.weberpackage.blackjack"
        minSdk = 24
        targetSdk = libs.versions.compileSdk.get().toInt()
        versionCode = vMajor * 1000000 + vMinor * 10000 + vPatch * 100
        versionName = "${vMajor}.${vMinor}.${vPatch}"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            buildConfigField("String", "BUILD_TIME", "\"${getCurrentTime()}\"")
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            manifestPlaceholders.putAll(mapOf("appName" to "BlackJack"))
            buildConfigField("Boolean", "ALPHA_BUILD", isAlpha.toString())
        }
        debug {
            buildConfigField("String", "BUILD_TIME", "\"${getCurrentTime()}\"")
            applicationIdSuffix = ".debug"
            manifestPlaceholders.putAll(mapOf("appName" to "BlackJack Debug"))
            buildConfigField("Boolean", "ALPHA_BUILD", isAlpha.toString())
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

androidComponents {
    onVariants { variant ->
        val appName = rootProject.name.lowercase()
        val buildType = variant.buildType
        variant.outputs.forEach {
            val apkName = "${appName}-${buildType}-${it.versionName.get()}.apk"
            (it as VariantOutputImpl).outputFileName = apkName
        }
    }
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.foundation.layout)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.compose.fonts)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.navigation.runtime.ktx)

    // Navigation 3 (Experimental)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
    implementation(libs.androidx.material3.adaptive.navigation3)

    // Extended icons library
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.preference.ktx)

    // Serialization
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.serialization.json)

    // MaterialKolor
    implementation(libs.materialKolor)

    // Haze (Blur)
    implementation(libs.haze.android)
    implementation(libs.haze.materials)

    // Compose Unstyled
//    implementation(libs.composeunstyled)
//    implementation(libs.ui)

    // Compose Core
    implementation(libs.composables.core)

    //Hilt
    ksp(libs.hilt.compiler)
    ksp(libs.hilt.work.compiler)
    implementation(libs.androidx.hilt.navigation)
    implementation(libs.androidx.hilt.viewmodel)
    implementation(libs.hilt.android)
    implementation(libs.hilt.work)

    // Timber
    implementation(libs.timber)

    // WorkManager
    implementation(libs.androidx.work.runtime)

    //Material 3 Expressive
    implementation(libs.androidx.compose.material3.android)

    //Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.config)

    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}

fun getCurrentTime(): String {
    return System.currentTimeMillis().toString()
}