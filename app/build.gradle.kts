import de.undercouch.gradle.tasks.download.Download
import io.github.fvarrui.javapackager.model.FileAssociation

plugins {
    id("java")
    id("application")
    id("io.github.fvarrui.javapackager.plugin")
    id("de.undercouch.download") version "5.6.0"
}

group = "org.processing.app"
version = "4.4.0"

repositories {
    mavenCentral()
    maven { url = uri("https://jogamp.org/deployment/maven") }
}

sourceSets{
    main{
        resources{
            srcDirs("src/main/java/", "../shared")
        }
    }
}


dependencies {
    implementation("com.formdev:flatlaf:3.4.1")

    implementation("net.java.dev.jna:jna:5.12.1")
    implementation("net.java.dev.jna:jna-platform:5.12.1")


    implementation(project(":core"))
    runtimeOnly(project(":java"))
}


application {
    mainClass = "processing.app.Base"
}

javapackager {
    mainClass("processing.app.ui.Splash")

//    bundleJre(false)
    // Bundling the JRE is what is causing the missing classes for the autocomplete, it doesn't copy over the jmod files that we need
    bundleJre(true)
//    customizedJre(false) // Disabled to enabled debugging on the build package

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
        isGenerateDmg = false
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

tasks.jar{ dependsOn("unzipExamples") }