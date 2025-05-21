plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    id("com.google.devtools.ksp") version "1.7.20-1.0.8" apply false
}

buildscript {

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-serialization:1.7.20")
        classpath ("com.google.dagger:hilt-android-gradle-plugin:2.49")
    }
}