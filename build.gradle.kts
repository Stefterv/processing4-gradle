plugins {
    id("java")
    kotlin("jvm") version "1.9.23"
    id("org.jetbrains.compose") version "1.6.2" apply false
}

group = "org.processing"

repositories {
    mavenCentral()
}

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("io.github.fvarrui:javapackager:1.7.5")
    }
}
apply(plugin = "io.github.fvarrui.javapackager.plugin")