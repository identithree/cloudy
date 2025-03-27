plugins {
    id("java")
    id("application")
    id("org.openjfx.javafxplugin") version "0.1.0"
}

group = "dev.quinnlane"
version = "1.0-SNAPSHOT"

application {
    mainClass = "dev.quinnlane.cloudy.Main"
}

repositories {
	// Maven Central Repository
    maven {
        url = uri("https://packages.code.var.blue/repository/maven-central/")
    }
}

javafx {
    version = "24"
    modules = listOf("javafx.controls", "javafx.fxml")
}

dependencies {
	// Playwright
	implementation("com.microsoft.playwright:playwright:1.51.0")
	// JetBrains Annotations
	implementation("org.jetbrains:annotations:26.0.2")
	// JUnit (in test module only)
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}
