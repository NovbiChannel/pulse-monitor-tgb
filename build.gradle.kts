import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val jvmTargetVersion = JavaVersion.VERSION_11
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.ksp)
}

group = "org.novbicreate"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.ws)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.client.logging)
    implementation(libs.koin.core)
    implementation(libs.eu.vendeli.telegramBot)
    ksp(libs.eu.vendeli.ksp)
}

tasks {
    compileJava {
        targetCompatibility = jvmTargetVersion.majorVersion
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
            javaParameters = true
        }
    }
}