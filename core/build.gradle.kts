plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
    kotlin("jvm") version "1.9.23"
    id("maven-publish")
}

group = "org.processing.core"
version = "4.4.0"

repositories {
    mavenCentral()
    maven { url = uri("https://jogamp.org/deployment/maven") }
}

sourceSets{
    main{
        resources{
            srcDirs("src/main/java/")
        }
    }
}

dependencies {
    implementation("org.jogamp.gluegen:gluegen-rt-main:2.5.0")
    implementation("org.jogamp.jogl:jogl-all-main:2.5.0")


    testImplementation("junit:junit:4.13.2")
    implementation(kotlin("stdlib-jdk8"))
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "org.processing"
            artifactId = "core"

            from(components["java"])
        }
    }
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}