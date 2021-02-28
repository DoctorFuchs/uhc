import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.konan.properties.Properties

plugins {
    java
    `maven-publish`
    kotlin("jvm") version "1.4.30"
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

val localProps = Properties()
localProps.load(file("local.properties").reader())

repositories {
    mavenLocal()
    mavenCentral()
    jcenter()
    maven { url = uri("https://papermc.io/repo/repository/maven-public/") }
    maven { url = uri("https://oss.sonatype.org/content/groups/public/") }
    maven { url = uri("https://jcenter.bintray.com") }
    maven { url = uri("https://jitpack.io") }
    maven { url = uri("https://repo.maven.apache.org/maven2/") }
    // COMMAND API
    maven { url = uri("https://raw.githubusercontent.com/JorelAli/CommandAPI/mvn-repo/") }
    maven { url = uri("https://repo.codemc.org/repository/maven-public/") }
    // COMMAND API
    maven {
        credentials {
            username = localProps.getProperty("gpr.user") ?: System.getenv("USERNAME")
            password = localProps.getProperty("gpr.key") ?: System.getenv("TOKEN")
        }
        url = uri("https://maven.pkg.github.com/HGLabor/HGLaborUtils")
    }
    maven {
        credentials {
            username = localProps.getProperty("gpr.user") ?: System.getenv("USERNAME")
            password = localProps.getProperty("gpr.key") ?: System.getenv("TOKEN")
        }
        url = uri("https://maven.pkg.github.com/HGLabor/Localization")
    }

    maven { url = uri("https://mvn.intellectualsites.com/content/repositories/releases/") }  //FAWE
}

dependencies {
    compileOnly("com.destroystokyo.paper:paper-api:1.16.5-R0.1-SNAPSHOT")
    compileOnly("org.bukkit:craftbukkit:1.16.5-R0.1-SNAPSHOT")
    compileOnly("com.intellectualsites.fawe:FAWE-Bukkit:1.16-583")
    implementation("dev.jorel:commandapi-shade:5.8")
    implementation("de.hglabor:hglabor-utils:0.0.4")
    implementation("de.hglabor:localization:0.0.5")
    implementation("net.axay:KSpigot:v1.16.5_R24")
}

group = "de.hglabor.plugins"
version = "0.0.1"
description = "Minecraft UHC in 1.16+"

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}

tasks {
    shadowJar {
        relocate("net.axay.kspigot", "${project.group}.${project.name.toLowerCase()}.shadow.net.axay.kspigot")
        minimize()
    }
}

val jvmVersion = JavaVersion.VERSION_11
val jvmVersionString = "11"

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = jvmVersionString
}
