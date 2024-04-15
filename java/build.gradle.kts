

plugins {
    id("java")
    id("application")
    id("antlr")
}

repositories {
    mavenCentral()
    maven { url = uri("https://jogamp.org/deployment/maven") }
}


dependencies {
    implementation("com.google.classpath-explorer:classpath-explorer:1.0")
//    implementation("org.antlr:antlr4-runtime:4.13.1")
    implementation("org.netbeans.api:org-netbeans-swing-outline:RELEASE210")
    implementation("org.apache.ant:ant:1.10.14")
    implementation("org.eclipse.lsp4j:org.eclipse.lsp4j:0.22.0")
    implementation("org.jsoup:jsoup:1.17.2")
    implementation("org.eclipse.jdt:org.eclipse.jdt.core:3.37.0")

    compileOnly(project(":app"))
    implementation(project(":core"))

    testImplementation("junit:junit:4.13.2")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.8.2")
    testImplementation("org.mockito:mockito-core:5.11.0")


    antlr("org.antlr:antlr4:4.13.1")
}


tasks.compileJava{
    options.encoding = "UTF-8"
}
tasks.compileTestJava {
    options.encoding = "UTF-8"
}

tasks.test {
    useJUnitPlatform()
}