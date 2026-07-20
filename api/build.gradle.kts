import net.thebugmc.gradle.sonatypepublisher.PublishingType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("net.fabricmc.fabric-loom") version "1.17-SNAPSHOT"
    id("net.thebugmc.gradle.sonatype-central-portal-publisher") version "1.2.4"
}

group = "net.dungeon-hub.prompt-overlay"
val artifactId = "api"
version = "${project.property("minecraft_version")}-${project.property("api_version")}"
description = "API for the Prompt Overlay mod - allows other mods to create custom overlays"

val targetJavaVersion = 25
java {
    toolchain.languageVersion = JavaLanguageVersion.of(targetJavaVersion)
    withSourcesJar()
    withJavadocJar()
}

repositories {
    maven("https://maven.fabricmc.net/") {
        name = "Fabric"
    }
    mavenCentral()
}

dependencies {
    minecraft("com.mojang:minecraft:${project.property("minecraft_version")}")

    compileOnly("net.fabricmc:fabric-loader:${project.property("loader_version")}")
    compileOnly("net.fabricmc:fabric-language-kotlin:${project.property("kotlin_loader_version")}")
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release.set(targetJavaVersion)
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions.jvmTarget.set(JvmTarget.fromTarget(targetJavaVersion.toString()))
}

kotlin {
    jvmToolchain(targetJavaVersion)
    compilerOptions {
        freeCompilerArgs.add("-Xjvm-default=all")
    }
}

centralPortal {
    name = artifactId
    publishingType = PublishingType.USER_MANAGED

    pom {
        name = artifactId
        description = project.description
        url = "https://github.com/dungeon-hub/prompt-overlay"

        organization {
            name = "Dungeon Hub"
            url = "https://dungeon-hub.net/"
        }

        scm {
            url = "https://github.com/dungeon-hub/prompt-overlay"
            connection = "scm:git://github.com:dungeon-hub/prompt-overlay.git"
            developerConnection = "scm:git://github.com:dungeon-hub/prompt-overlay.git"
        }

        developers {
            developer {
                id = "taubsie"
                name = "Taubsie"
                email = "taubsie@dungeon-hub.net"
                url = "https://github.com/Taubsie/"
                organizationUrl = "https://dungeon-hub.net/"
            }
        }

        licenses {
            license {
                name = "EUPL-1.2"
                url = "https://eupl.eu/1.2/en/"
            }
        }
    }
}
