import io.github.fvarrui.javapackager.model.FileAssociation

plugins {
    id("java")
    id("io.github.fvarrui.javapackager.plugin")
}

group = "org.processing"
version = "unspecified"

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
