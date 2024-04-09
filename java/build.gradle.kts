plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

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
}

tasks.test {
    useJUnitPlatform()
}