import de.undercouch.gradle.tasks.download.Download
import io.github.fvarrui.javapackager.model.FileAssociation

plugins {
    id("java")
    id("application")
    id("io.github.fvarrui.javapackager.plugin")
    id("de.undercouch.download") version "5.6.0"
}

group = "org.processing"
version = "4.4"

application {
    mainClass = "processing.app.Base"
}

// This can be removed by moving the asset management to native Java resources
sourceSets{
    main{
        resources {
            srcDirs("../shared")
        }
    }
}

javapackager {
    mainClass("processing.app.ui.Splash")
    bundleJre(true)
    displayName("Processing")
    name("Processing")
    url("https://processing.org")
    //    licenseFile("todo")
    // TODO: Add after resources folder
    additionalResources( file("build/resources/main").list()?.map { t->  file("build/resources/main/${t}") }?.toMutableList() ?: mutableListOf())
    fileAssociations(mutableListOf(
        FileAssociation().apply {
            extension = "pde"
            description = "Processing Source Code"
        }
    ))
    macConfig.apply {
        isGeneratePkg = false
        appId = "org.processing.app"
        entitlements = file("assets/mac/processing.entitlements")
        backgroundImage = file("assets/mac/background.png")
        windowWidth = 750
        windowHeight = 450
        iconX = 50
        iconSize = 250
        iconY = (windowHeight - iconSize) / 2 + 35
        appsLinkIconX = (windowWidth - iconSize)
        appsLinkIconY = iconY
        windowX = 200
        windowY = 200
    }
    winConfig.apply {
        isGenerateMsi = false
        isGenerateMsm = false
        fileVersion = project.version as String
        productVersion = project.version as String
        isDisableRunAfterInstall = false
    }
}

repositories {
    mavenCentral()
}



dependencies {
    implementation(project(":app"))
    implementation(project(":core"))
    implementation(project(":java"))
}

// This could be removed if the internal build system within processing was moved to gradle
tasks.register<Copy>("coreJar") {
    group = "build"
    dependsOn(project(":core").tasks.jar)
    dependsOn(tasks.processResources)
    from(project(":core").layout.buildDirectory.dir("libs"))
    into(layout.buildDirectory.file("resources/main/core/library"))
    include("*.jar")
}
tasks.compileJava { dependsOn("coreJar") }

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