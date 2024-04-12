plugins {
    id("java")
    id("application")
    id("io.github.fvarrui.javapackager.plugin")
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

application {
    mainClass = "processing.app.ui.Splash"
}

javapackager {
    mainClass("processing.app.ui.Splash")
    bundleJre(true)
    additionalResources(files("../shared").toMutableList())
}

dependencies {
    implementation("com.formdev:flatlaf:3.4.1")

    implementation("net.java.dev.jna:jna:5.12.1")
    implementation("net.java.dev.jna:jna-platform:5.12.1")


    implementation(project(":core"))

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}