buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.2.0")
    }
}

plugins {
    kotlin("jvm") version "2.2.21"
    kotlin("android") version "2.2.21" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.2.21" apply false
    id("com.android.application") version "8.2.0" apply false
}

// add dependencies for the root JVM module

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.exposed:exposed-core:0.41.1")
    implementation("org.jetbrains.exposed:exposed-dao:0.41.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.41.1")
    implementation("org.jetbrains.exposed:exposed-jodatime:0.41.1")
    implementation("com.h2database:h2:2.1.214")
    implementation("joda-time:joda-time:2.10.14")

    testImplementation(kotlin("test"))
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}