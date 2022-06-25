/*
 * KBGL - Kotlin Block Game Library
 * Copyright (C) 2022  KBGL authors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

import com.github.jengelman.gradle.plugins.shadow.ShadowExtension
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.apache.tools.ant.filters.ReplaceTokens
import org.gradle.api.JavaVersion
import java.net.URI
import org.apache.tools.ant.filters.*
import org.gradle.plugins.ide.idea.model.IdeaLanguageLevel

plugins {
	`java-gradle-plugin`
	kotlin("jvm") version "1.6.20"
	id("com.github.johnrengelman.shadow") version "7.1.2"
	id("io.papermc.paperweight.userdev") version "1.3.7"
	idea
}

group = "com.fablesfantasyrp.kbgl"
version = "1.0-SNAPSHOT"
description = "KBGL"

apply {
	plugin("java")
	plugin("kotlin")
	plugin("com.github.johnrengelman.shadow")
	plugin("idea")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks {
	withType<ProcessResources> {
		filter(mapOf(Pair("tokens", mapOf(Pair("version", version)))), ReplaceTokens::class.java)
	}

	withType<ShadowJar> {
		this.classifier = null
		this.configurations = listOf(project.configurations.shadow.get())
	}

	assemble {
		dependsOn(reobfJar)
	}
}

defaultTasks = mutableListOf("shadowJar")

repositories {
	maven("https://papermc.io/repo/repository/maven-public/")
	maven { url = URI("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") }
	maven { url = URI("https://repo.kryptonmc.org/releases") }
	maven { url = URI("https://jitpack.io") }

	mavenCentral()
	mavenLocal()
}

idea {
	project {
		languageLevel = IdeaLanguageLevel("1.8")
	}
	module {
		isDownloadJavadoc = true
		isDownloadSources = true
	}
}

dependencies {
	paperDevBundle("1.18.2-R0.1-SNAPSHOT")
	//implementation("org.spigotmc:spigot-api:1.18.1-R0.1-SNAPSHOT") { isChanging = true }
	implementation("com.gitlab.martijn-heil:NinCommands:-SNAPSHOT") { isChanging = true }
	//compileOnly(fileTree("lib") { include("*.jar") })
	shadow(kotlin("stdlib"))
}
