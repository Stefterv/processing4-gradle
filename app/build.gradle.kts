import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    id("java")
//    id("application")
//    id("io.github.fvarrui.javapackager.plugin")
    kotlin("jvm") version "1.9.23"
    id("org.jetbrains.compose") version "1.6.2"
}

group = "org.example"
version = "1.0-SNAPSHOT"

kotlin {
    jvmToolchain(21)
}

repositories {
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
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

//application {
//    mainClass = "processing.app.ui.Splash"
//}

//javapackager {
//    mainClass("processing.app.ui.Splash")
//    bundleJre(true)
//    additionalResources(files("../shared").toMutableList())
//}

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
compose.desktop {
    application {
        mainClass = "processing.app.ui.Splash"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "org.example.project"
            packageVersion = "1.0.0"
        }
    }
}


tasks.test {
    useJUnitPlatform()
}