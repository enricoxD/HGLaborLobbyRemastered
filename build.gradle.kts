@file:Suppress("PropertyName")

import com.github.jengelman.gradle.plugins.shadow.tasks.ConfigureShadowRelocation
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/*
 * BUILD CONSTANTS
 */

val JVM_VERSION = JavaVersion.VERSION_1_8
val JVM_VERSION_STRING = JVM_VERSION.versionString

/*
 * PROJECT
 */

group = "net.axay"
version = "1.0.0"

plugins {

    kotlin("jvm") version "1.4.10"

    id("com.github.johnrengelman.shadow") version "6.0.0"

}

/*
 * DEPENDENCY MANAGEMENT
 */

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {

    // SPIGOT
    compileOnly("org.spigotmc", "spigot", "1.16.3-R0.1-SNAPSHOT")

    // KSPIGOT
    implementation("net.axay", "KSpigot", "1.16.3_R11")

    // BLUEUTILS
    implementation("net.axay", "BlueUtils", "1.0.0")

}

/*
 * BUILD
 */

// JVM VERSION

java.sourceCompatibility = JVM_VERSION

tasks {
    compileKotlin.configureJvmVersion()
    compileTestKotlin.configureJvmVersion()
}

// SHADOW

tasks {
    shadowJar {
        minimize()
    }
}

val relocateShadowJar by tasks.creating(ConfigureShadowRelocation::class) {
    target = tasks.shadowJar.get()
    prefix = "${project.group}.shadow"
}

tasks.shadowJar.get().dependsOn(relocateShadowJar)

/*
 * EXTENSIONS
 */

val JavaVersion.versionString get() = majorVersion.let {
    val version = it.toInt()
    if (version <= 10) "1.$it" else it
}

fun TaskProvider<KotlinCompile>.configureJvmVersion() { get().kotlinOptions.jvmTarget = JVM_VERSION_STRING }