/*
 * Copyright 2020 Dmitry Korotych.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.github.dkorotych.gradle.maven.exec.MavenExec
import com.github.dkorotych.gradle.maven.exec.MavenExecSpec
import org.gradle.kotlin.dsl.accessors.runtime.extensionOf
import java.io.FileOutputStream
import java.nio.charset.StandardCharsets.UTF_8

buildscript {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        mavenCentral()
    }
    dependencies {
        classpath("com.github.dkorotych.gradle.maven.exec:gradle-maven-exec-plugin:${project.version}")
    }
}

apply(plugin = "com.github.dkorotych.gradle-maven-exec")

val groupId = "com.mycompany.app"
val artifactId = "my-app"
val outputDir = file("$buildDir/$artifactId")

println("Plugin version: ${project.version}")

tasks.register<MavenExec>("generateMavenApplication") {
    buildDir.mkdirs()
    outputs.dir(outputDir)
    delete(outputDir)
    workingDir(buildDir)
    goals("archetype:generate")
    define(mapOf(
            "groupId" to groupId,
            "artifactId" to artifactId,
            "archetypeArtifactId" to "maven-archetype-quickstart",
            "archetypeVersion" to "1.4",
            "interactiveMode" to "false"
    ))
}

tasks.register<MavenExec>("packageMavenApplication") {
    dependsOn("generateMavenApplication")
    outputs.dir(file("${outputDir}/target"))
    workingDir(outputDir)
    goals(listOf("package"))
}

tasks.register<JavaExec>("checkMavenApplication") {
    dependsOn("packageMavenApplication")
    val outputFile = file("$buildDir/output.txt")
    doFirst {
        workingDir(outputDir)
        classpath(file("${outputDir}/target/${artifactId}-1.0-SNAPSHOT.jar"))
        main = "${groupId}.App"
        standardOutput = FileOutputStream(outputFile)
    }
    doLast {
        assert(outputFile.readText(UTF_8).trim() == "Hello World!")
    }
}

task("checkMavenApplication2") {
    doLast {
        buildDir.mkdirs()
        val application = "$artifactId-second"
        val applicationDir = file("$buildDir/$application")
        delete(applicationDir)
        val outputFile = file("$buildDir/output.txt")
//        val options = configure<Closure<MavenCli>>(
//                define(mapOf(
//                        "groupId" to groupId,
//                        "artifactId" to application,
//                        "archetypeArtifactId" to "maven-archetype-quickstart",
//                        "archetypeVersion" to "1.4",
//                        "interactiveMode" to "false"
//                ))
//                        quiet(true)
//        )
        configure<MavenExec> {
            workingDir(buildDir)
            goals("archetype:generate")
//            options(options)
        }
        configure<MavenExec> {
            workingDir = applicationDir
            goals("package")
//            options {
//                threads("2C")
//                quiet(true)
//            }
        }
        javaexec {
            workingDir(applicationDir)
            classpath(file("$applicationDir/target/$application-1.0-SNAPSHOT.jar"))
            main = "${groupId}.App"
            standardOutput = FileOutputStream(outputFile)
        }
        assert(outputFile.readText(UTF_8).trim() == "Hello World!")
    }
}

task("checkMavenApplication3") {
    doLast {
        buildDir.mkdirs()
        val application = "$artifactId-third"
        val applicationDir = file("$buildDir/$application")
        delete(applicationDir)
        configure<MavenExecSpec> {
            workingDir(buildDir)
            goals("archetype:generate")
//            define(mapOf(
//                    "groupId" to groupId,
//                    "artifactId" to application,
//                    "archetypeArtifactId" to "maven-archetype-j2ee-simple",
//                    "archetypeVersion" to "1.4",
//                    "interactiveMode" to "false"
//            ))
//            quiet(true)
        }
        val file = file("${applicationDir}/pom.xml")
        var content = file.readText(UTF_8)
        content = "2.20.1".toRegex().replace(content, "2.22.0")
        content = "2.10.1".toRegex().replace(content, "3.0.1")
        file.writeText(content, UTF_8)
        configure<MavenExecSpec> {
            workingDir(applicationDir)
            goals("package")
//            threads("1C")
//            quiet(true)
        }
    }
}
