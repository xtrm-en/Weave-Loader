pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://repo.weavemc.dev")
        mavenLocal()
    }
}

plugins {
	id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.+"
}

val projectName: String by settings
rootProject.name = projectName

includeBuild("build-logic")
include("loader", "api")
