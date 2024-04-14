plugins {
    id("java")
}

repositories {
    mavenCentral()
}
allprojects {
    group = "org.processing"
    version = "4.4"
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