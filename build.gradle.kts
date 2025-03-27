// Cloudy - build.gradle.kts
// Written by Quinn Lane - https://quinnlane.dev

// Imports (for development versioning)
import java.net.InetAddress
import java.net.UnknownHostException
import java.text.SimpleDateFormat
import java.util.*

// Plugin Definition
// Plugins, like dependencies, are defined in gradle/libs.versions.toml
plugins {
    alias(libs.plugins.java) // Java
	alias(libs.plugins.javafx) // JavaFX
	alias(libs.plugins.application) // Application
	alias(libs.plugins.shadow) // Shadow
}

// Version String Creator
fun createVersionString(): String {
	// Create a StringBuilder for the version
	val versionString = StringBuilder()

	// Get the root version string from gradle.properties (should be in SemVer)
	val versionRoot = (project.properties["version"]!! as String)
	// Check if this is a release build
	val isRelease = (project.properties["buildForRelease"]!! as String).toBoolean()
	// Check if this is a CI build
	val isCI = System.getenv("CI")

	// Add version root to output
	versionString.append(versionRoot.substringBefore("-"))

	// Check if the build is a release build
	if (!isRelease) {
		// Append snapshot tag
		versionString.append("-SNAPSHOT")

		// Check if this is a CI build
		if (isCI != null && isCI.toBoolean()) {
			// If it is, then append shortened commit SHA to the version
			val buildSha = System.getenv("GITHUB_SHA").substring(0, 7)
			versionString.append("+rev.").append(buildSha)
		} else {
			// If it is not, then append the building computer's hostname
			var hostname: String

			try {
				hostname = InetAddress.getLocalHost().hostName.substringBefore(".")
			} catch (_: UnknownHostException) {
				hostname = "unknown"
			}

			versionString.append("+local.").append(hostname)
		}
	} else {
		// If this is a release build, put back the removed data
		versionString.append("-").append(versionRoot.substringAfter("-", ""))
	}

	// Return final version string
	return versionString.toString()
}

// Variable Definition
version = createVersionString() // Define Version
group = project.properties["mavenGroup"]!! as String // Define Group
val archivesBaseName = project.properties["archivesBaseName"]!! as String // Define archive base name (project name)
val licenseFile = rootProject.properties["licenseFile"]!! as String // Define LICENSE file
val javaTargetVersion = (rootProject.properties["javaTargetVersion"]!! as String).toInt() // Define Java target version

// Application Configuration
application {
    mainClass = "${group}.${archivesBaseName}.Cloudy"
}

// Maven Repositories (for dependency resolution, not publishing)
repositories {
	// Maven Central Repository
    maven {
        url = uri("https://packages.code.var.blue/repository/maven-central/")
    }
}

// Java Configuration
java {
	// Required for IDEs like Eclipse and Visual Studio Code
	sourceCompatibility = JavaVersion.toVersion(javaTargetVersion)
	targetCompatibility = JavaVersion.toVersion(javaTargetVersion)

	// Include sources in the build
	withSourcesJar()

	// Do not infer a module path
	modularity.inferModulePath.set(false)
}

// JavaFX Plugin
// As JavaFX is considered a library, it's version is defined in gradle/libs.version.toml
javafx {
    version = libs.versions.javafx.asProvider().get()
    modules = listOf("javafx.controls", "javafx.fxml")
}

// Dependency Definitions
// All the dependencies are declared at gradle/libs.version.toml and referenced with "libs.<id>"
// See https://docs.gradle.org/current/userguide/platforms.html for information on how version catalogs work.
dependencies {
	// Playwright
	implementation(libs.playwright)
	// JetBrains Annotations
	implementation(libs.jetbrains.annotations)
	// Log4J (at runtime only)
	implementation(libs.bundles.log4j)
	// JUnit (in test module only)
    testImplementation(libs.junit.jupiter)
	testRuntimeOnly(libs.junit.platform.launcher)
}

// Gradle Task Configuration
tasks {
	processResources {
		inputs.property("version", project.version)
		filteringCharset = "UTF-8"
		filesMatching("gradle/version.properties") {
			expand("version" to project.version)
		}
	}

	// Set Java compiler options
	withType(JavaCompile::class.java).configureEach {
		options.release.set(javaTargetVersion)
		options.encoding = "UTF-8"
	}

	// Configure JAR building
	jar {
		// Configure MANIFEST.MF
		manifest {
			attributes["Main-Class"] = application.mainClass
			attributes["Implementation-Title"] = "Cloudy"
			attributes["Implementation-Version"] = project.version
			attributes["Implementation-Build-Date"] = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US).format(Calendar.getInstance().time)
		}

		// Rename the license file to include the archive name
		from(licenseFile) {
			rename { "${it}_${archivesBaseName}" }
		}
	}

	named("shadowJar", com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar::class) {
		archiveClassifier = "shadow"
		minimize()
	}

	// Unit Testing
	test {
		// Use JUnit
		useJUnitPlatform()
		// Logging Options
		testLogging {
			events("passed", "skipped", "failed")
		}
	}
}
