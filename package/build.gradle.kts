import de.undercouch.gradle.tasks.download.Download
import io.github.fvarrui.javapackager.model.FileAssociation
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    id("java")
//    id("application")
//    id("io.github.fvarrui.javapackager.plugin")
    id("de.undercouch.download") version "5.6.0"
    kotlin("jvm") version "1.9.23"
    id("org.jetbrains.compose") version "1.6.2"
}

group = "org.processing"
version = "4.4"

//application {
//    mainClass = "processing.app.Base"
//}

// This can be removed by moving the asset management to native Java resources
sourceSets{
    main{
        resources {
            srcDirs("../shared")
        }
    }
}

compose.desktop {
    application {
        mainClass = "processing.app.ui.Splash"

        nativeDistributions {
            includeAllModules = true

            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "org.example.project"
            packageVersion = "1.0.0"

            appResourcesRootDir.set(project.layout.buildDirectory.dir("resources/main"))

        }
    }
}

//javapackager {
//    mainClass("processing.app.ui.Splash")
//
////    bundleJre(false)
//    // Bundling the JRE is what is causing the missing classes for the autocomplete, it doesn't copy over the jmod files that we need
//    bundleJre(true)
////    customizedJre(false) // Disabled to enabled debugging on the build package
//
//    displayName("Processing")
//    name("Processing")
//    url("https://processing.org")
//    //    licenseFile("todo")
//    // TODO: Add after resources folder
//    additionalResources( file("build/resources/main").list()?.map { t->  file("build/resources/main/${t}") }?.toMutableList() ?: mutableListOf())
//    fileAssociations(mutableListOf(
//        FileAssociation().apply {
//            extension = "pde"
//            description = "Processing Source Code"
//        }
//    ))
//    macConfig.apply {
//        isGeneratePkg = false
//        isGenerateDmg = false
//        appId = "org.processing.app"
//        entitlements = file("assets/mac/processing.entitlements")
//        backgroundImage = file("assets/mac/background.png")
//        windowWidth = 750
//        windowHeight = 450
//        iconX = 50
//        iconSize = 250
//        iconY = (windowHeight - iconSize) / 2 + 35
//        appsLinkIconX = (windowWidth - iconSize)
//        appsLinkIconY = iconY
//        windowX = 200
//        windowY = 200
//    }
//    winConfig.apply {
//        isGenerateMsi = false
//        isGenerateMsm = false
//        fileVersion = project.version as String
//        productVersion = project.version as String
//        isDisableRunAfterInstall = false
//    }
//}

//kotlin {
//    jvmToolchain(21)
//}

repositories {
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
    mavenCentral()
    maven { url = uri("https://jogamp.org/deployment/maven") }
}



dependencies {
    implementation(project(":app"))
    implementation(project(":core"))
    implementation(project(":java"))

//    implementation(compose.desktop.currentOs)
//    implementation(compose.runtime)
//    implementation(compose.foundation)
//    implementation(compose.material)
//    implementation(compose.ui)
//    implementation(compose.components.resources)
//    implementation(compose.components.uiToolingPreview)
}

// This could be removed if the internal build system within processing was moved to gradle
tasks.register<Copy>("coreJar") {
    group = "build"
    dependsOn(project(":core").tasks.jar)
    dependsOn(project(":core").tasks.findByPath("shadowJar"))
    dependsOn(tasks.processResources)
    from(project(":core").layout.buildDirectory.dir("libs"))
    into(layout.buildDirectory.file("resources/main/core/library"))
    include("*-all.jar")
}
tasks.compileJava { dependsOn("coreJar") }

tasks.register<Copy>("extraResources"){

}

tasks.register<Download>("downloadExamples"){
    dependsOn(tasks.processResources)
    src("https://github.com/processing/processing-examples/archive/refs/heads/main.tar.gz")
    dest(layout.buildDirectory.file("examples.tar.gz"))
    overwrite(false)
}

tasks.register<Copy>("unzipExamples"){
    val dl = tasks.findByPath("downloadExamples") as Download
    dependsOn(dl)
    from(tarTree(dl.dest))
    eachFile{
        path = Regex("processing-examples-[^/]+/").replaceFirst(path, "/")
    }
    into(layout.buildDirectory.dir("resources/main/modes/java/examples"))
}

tasks.jar{ finalizedBy("unzipExamples") }