import de.undercouch.gradle.tasks.download.Download

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("application")
    id("antlr")
    id("de.undercouch.download") version "5.6.0"

}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

application {
    mainClass = "processing.app.ui.Splash"
}

sourceSets{
    main{
        resources {
            srcDirs("../shared", "src/main/resources")
        }
    }
}
tasks.register<Download>("openJDK"){
//    src("https://github.com/adoptium/temurin17-binaries/releases/download/jdk-${jdk.detail}%2B${jdk.build}/OpenJDK17U-${jdk.download.dist}_${jdk.download.arch}_${jdk.download.os}_hotspot_${jdk.detail}_${jdk.build}.${jdk.download.ext}")
    src("https://github.com/adoptium/temurin17-binaries/releases/download/jdk-17.0.1%2B12/OpenJDK17U-jre_aarch64_mac_hotspot_17.0.1_12.tar.gz")
    dest(layout.buildDirectory.file("resources/main/java/openjdk.tar.gz"))
}
tasks.register<Copy>("unzipOpenJDK"){
    dependsOn("openJDK")
    dependsOn(tasks.processResources)
    from(tarTree(layout.buildDirectory.file("resources/main/java/openjdk.tar.gz")))
    into(layout.buildDirectory.file("resources/PlugIns/"))
}
tasks.compileJava { dependsOn("unzipOpenJDK")}

tasks.register<Copy>("coreJar") {
    group = "build"
    dependsOn(project(":core").tasks.jar)
    from("../core/build/libs")
    into("build/resources/main/core/library")
    include("*.jar")
}
tasks.compileJava { dependsOn("coreJar") }

dependencies {
    implementation("com.google.classpath-explorer:classpath-explorer:1.0")
    implementation("org.antlr:antlr4-runtime:4.13.1")
    implementation("org.netbeans.api:org-netbeans-swing-outline:RELEASE210")
    implementation("org.apache.ant:ant:1.10.14")
    implementation("org.eclipse.lsp4j:org.eclipse.lsp4j:0.22.0")
    implementation("org.jsoup:jsoup:1.17.2")

    implementation("org.eclipse.jdt:org.eclipse.jdt.core:3.37.0")

    implementation(project(":app"))
    implementation(project(":core"))

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    antlr("org.antlr:antlr4:4.13.1")
}

tasks.test {
    useJUnitPlatform()
}