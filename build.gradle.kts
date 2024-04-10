plugins {
    id("java")
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