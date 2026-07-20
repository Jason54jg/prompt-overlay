import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.3.21"
    kotlin("plugin.serialization") version "2.3.21"
    id("net.fabricmc.fabric-loom") version "1.17-SNAPSHOT"
    id("com.gradleup.shadow") version "9.4.2"
    id("maven-publish")
}

version = project.property("mod_version") as String
group = project.property("maven_group") as String

base {
    archivesName.set(project.property("archives_base_name") as String)
}

val targetJavaVersion = 25
java {
    toolchain.languageVersion = JavaLanguageVersion.of(targetJavaVersion)
    // Loom will automatically attach sourcesJar to a RemapSourcesJar task and to the "build" task
    // if it is present.
    // If you remove this line, sources will not be generated.
    withSourcesJar()
}

loom {
    
}

fabricApi {
    configureDataGeneration {
        client = true
    }
}

repositories {
    maven ("https://pkgs.dev.azure.com/djtheredstoner/DevAuth/_packaging/public/maven/v1") {
        content {
            includeGroup("me.djtheredstoner")
        }
    }

    maven("https://maven.teamresourceful.com/repository/maven-public/") {
        name = "Team Resourceful Maven"
        content {
            includeGroup("com.teamresourceful.resourcefulconfig")
            includeGroup("com.teamresourceful.resourcefulconfigkt")
        }
    }
}

dependencies {
    minecraft("com.mojang:minecraft:${project.property("minecraft_version")}")

    implementation("net.fabricmc:fabric-loader:${project.property("loader_version")}")
    implementation("net.fabricmc:fabric-language-kotlin:${project.property("kotlin_loader_version")}")

    implementation("net.fabricmc.fabric-api:fabric-api:${project.property("fabric_version")}")

    implementation("com.teamresourceful.resourcefulconfig:resourcefulconfig-fabric-26.2:5.0.0")
    val resourcefulConfigKt = "com.teamresourceful.resourcefulconfigkt:resourcefulconfigkt-26.1-rc-1:4.0.0-beta.1"
    implementation(resourcefulConfigKt)
    shadow(resourcefulConfigKt) { // TODO just use the dependency on the mod once (if) it becomes available on Modrinth
        isTransitive = false
    }

    implementation(project(":api"))
    shadow(project(":api")) {
        isTransitive = false
    }

    runtimeOnly("me.djtheredstoner:DevAuth-fabric:1.2.2")

    //Testing
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.10.1")
    testImplementation(kotlin("test"))
}

tasks.processResources {
    inputs.property("version", project.version)
    inputs.property("minecraft_version", project.property("minecraft_version"))
    inputs.property("loader_version", project.property("loader_version"))
    filteringCharset = "UTF-8"

    filesMatching("fabric.mod.json") {
        expand(
            "version" to project.version,
            "minecraft_version" to project.property("minecraft_version")!!,
            "loader_version" to project.property("loader_version")!!,
            "kotlin_loader_version" to project.property("kotlin_loader_version")!!
        )
    }
}

tasks.withType<JavaCompile>().configureEach {
    // ensure that the encoding is set to UTF-8, no matter what the system default is
    // this fixes some edge cases with special characters not displaying correctly
    // see http://yodaconditions.net/blog/fix-for-java-file-encoding-problems-with-gradle.html
    // If Javadoc is generated, this must be specified in that task too.
    options.encoding = "UTF-8"
    options.release.set(targetJavaVersion)
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions.jvmTarget.set(JvmTarget.fromTarget(targetJavaVersion.toString()))
}

tasks.jar {
    from("LICENSE") {
        rename { "${it}_${project.base.archivesName.get()}" }
    }
}

// Configure shadow jar to bundle dependencies
tasks.shadowJar {
    archiveClassifier.set("") // No classifier so it becomes the main jar
    configurations = listOf(project.configurations.shadow.get())

    // Exclude ONLY libraries provided by fabric-language-kotlin
    exclude("org/jetbrains/kotlin/**")
    exclude("kotlin/**")
    exclude("kotlinx/coroutines/**")
    exclude("kotlinx/serialization/**")
    exclude("kotlinx/atomicfu/**")
    exclude("kotlinx/datetime/**")
    exclude("kotlinx/io/**")

    // Exclude unnecessary libraries
    exclude("io/swagger/**")
    exclude("org/jetbrains/annotations/**")
    exclude("org/intellij/**")
    exclude("com/google/errorprone/**")
    exclude("org/slf4j/**")

    // Relocate ALL other dependencies to avoid conflicts
    relocate("com.squareup.moshi", "net.dungeonhub.promptoverlay.libs.moshi")
    relocate("com.squareup.okio", "net.dungeonhub.promptoverlay.libs.okio")
    relocate("okio", "net.dungeonhub.promptoverlay.libs.okio")
    relocate("okhttp3", "net.dungeonhub.promptoverlay.libs.okhttp3")
    relocate("io.ktor", "net.dungeonhub.promptoverlay.libs.ktor")
    relocate("com.hypercubetools", "net.dungeonhub.promptoverlay.libs.hypercubetools")
    relocate("com.google.gson", "net.dungeonhub.promptoverlay.libs.gson")
    relocate("com.fasterxml.jackson", "net.dungeonhub.promptoverlay.libs.jackson")
    relocate("jakarta", "net.dungeonhub.promptoverlay.libs.jakarta")
    relocate("org.apache.commons", "net.dungeonhub.promptoverlay.libs.apache.commons")
    relocate("org.yaml.snakeyaml", "net.dungeonhub.promptoverlay.libs.snakeyaml")
    relocate("io.github.oshai.kotlinlogging", "net.dungeonhub.promptoverlay.libs.kotlinlogging")
    relocate("com.teamresourceful.resourcefulconfigkt", "net.dungeonhub.promptoverlay.libs.resourcefulconfigkt")

    from("LICENSE") {
        rename { "${it}_${project.base.archivesName.get()}" }
    }

    archiveClassifier = project.property("minecraft_version").toString()
}

// Make jar task depend on shadowJar
tasks.jar {
    dependsOn(tasks.shadowJar)
    enabled = false // Disable regular jar, use shadow jar instead
}

tasks.build {
    dependsOn(tasks.shadowJar)
}

// configure the maven publication
publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = project.property("archives_base_name") as String
            from(components["java"])
        }
    }

    // See https://docs.gradle.org/current/userguide/publishing_maven.html for information on how to set up publishing.
    repositories {
        // Add repositories to publish to here.
        // Notice: This block does NOT have the same function as the block in the top level.
        // The repositories here will be used for publishing your artifact, not for
        // retrieving dependencies.
    }
}
