pluginManagement {
    plugins {
        kotlin("jvm") version "1.9.23"
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "processing"
include("core")
include("app")
include("java")

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("io.github.fvarrui:javapackager:1.7.5")
    }
}