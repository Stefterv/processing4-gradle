import org.gradle.nativeplatform.platform.internal.DefaultNativePlatform
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import de.undercouch.gradle.tasks.download.Download

plugins {
    id("java")
    id("de.undercouch.download") version "5.6.0"
    kotlin("jvm") version "1.9.23"
    id("org.jetbrains.compose") version "1.6.11"
}

group = rootProject.name
version = rootProject.version

kotlin {
    jvmToolchain(21)
}

repositories {
    maven("https://plugins.gradle.org/m2/" )
    google()
    mavenCentral()
    maven { url = uri("https://jogamp.org/deployment/maven") }
}

sourceSets{
    main{
        resources{
            srcDirs("src/main/java/","../shared")
        }
    }
}

dependencies {
    implementation("com.formdev:flatlaf:3.4.1")

    implementation("net.java.dev.jna:jna:5.12.1")
    implementation("net.java.dev.jna:jna-platform:5.12.1")


    implementation(project(":core"))

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation(compose.runtime)
    implementation(compose.foundation)
    implementation(compose.material)
    implementation(compose.ui)
    implementation(compose.components.resources)
    implementation(compose.components.uiToolingPreview)

    implementation(compose.desktop.currentOs)
}

tasks.test {
    useJUnitPlatform()
}