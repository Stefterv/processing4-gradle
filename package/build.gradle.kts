import de.undercouch.gradle.tasks.download.Download
import org.gradle.nativeplatform.platform.internal.DefaultNativePlatform
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    id("java")
    id("de.undercouch.download") version "5.6.0"
    kotlin("jvm") version "1.9.23"
    id("org.jetbrains.compose") version "1.6.11"
}

group = rootProject.group
version = "4.4"


compose.desktop {
    application {
        mainClass = "processing.app.ui.Splash"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "Processing"
            packageVersion = rootProject.version as String

            appResourcesRootDir.set(project.layout.projectDirectory.dir("resources"))
            macOS{
                bundleID = "org.processingfoundation.processing.app"
//                entitlementsFile = project.file("resources/mac-entitlements.plist")
                iconFile = project.file("assets/mac/processing.icns")
            }
            windows{
                iconFile = project.file("assets/windows/processing.ico")
            }
            linux {
                iconFile = project.file("assets/linux/processing.png")
            }
            buildTypes.release.proguard{
                optimize = false
            }
        }
    }
}

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

tasks.jar{ finalizedBy("unzipExamples") }

val os: OperatingSystem = DefaultNativePlatform.getCurrentOperatingSystem()
val arch = System.getProperty("os.arch")
var platform = "linux"
if (os.isWindows) {
    platform = "windows"
} else if (os.isMacOsX) {
    platform = "mac"
}
tasks.register<Download>("downloadJDK"){
    src("https://api.adoptium.net/v3/binary/latest/17/ga/${platform}/${arch}/jdk/hotspot/normal/eclipse?project=jdk")
    dest(layout.buildDirectory.file("jdk-${platform}-${arch}.tar.gz"))
    overwrite(false)
}
tasks.register<Copy>("unzipJDK"){
    val dl = tasks.findByPath("downloadJDK") as Download
    dependsOn(dl)
    from(tarTree(dl.dest))
    eachFile{
        path = Regex("jdk-[\\d.+]+").replaceFirst(path, "jdk")
    }
    into(layout.buildDirectory.dir("resources/${platform}-${arch}/"))
}
tasks.jar { dependsOn("unzipJDK") }
tasks.processResources{ finalizedBy("unzipJDK") }