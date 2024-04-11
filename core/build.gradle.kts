plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "org.example"
version = "1.0-SNAPSHOT"

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
}


tasks.test {
    useJUnitPlatform()
}