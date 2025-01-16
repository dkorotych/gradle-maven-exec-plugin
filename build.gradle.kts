/*
 * Copyright 2016 Dmitry Korotych.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import com.github.dkorotych.gradle.maven.exec.test.MavenTestHelperPlugin
import java.util.*

plugins {
    id("java-gradle-plugin")
    alias(libs.plugins.publish)
    id("idea")
    id("jacoco")
    alias(libs.plugins.release)
    alias(libs.plugins.license)
    alias(libs.plugins.versions)
    alias(libs.plugins.quality)
    alias(libs.plugins.info)
    alias(libs.plugins.rewrite)
    alias(libs.plugins.sonarqube)
}

apply {
    plugin(MavenTestHelperPlugin::class)
}

group = "io.github.dkorotych.gradle-maven-exec"
description = "Gradle Maven Exec Plugin"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    api(libs.commons.lang3)

    testImplementation(libs.junit.jupiter)
    testImplementation(libs.assertj.core)
    testImplementation(libs.mockito.core)
    testImplementation(libs.bean.matchers)

    rewrite(platform(libs.rewrite))
    rewrite("org.openrewrite:rewrite-gradle")
    rewrite("org.openrewrite.recipe:rewrite-github-actions")
    rewrite("org.openrewrite.recipe:rewrite-testing-frameworks")
    rewrite("org.openrewrite.recipe:rewrite-logging-frameworks")
    rewrite("org.openrewrite.recipe:rewrite-migrate-java")
    rewrite("org.openrewrite.recipe:rewrite-static-analysis")
}

github {
    user = "dkorotych"
    license = "Apache"
}

gradlePlugin {
    plugins {
        val mavenExecPlugin by plugins.creating {
            id = "com.github.dkorotych.gradle-maven-exec"
            implementationClass = "com.github.dkorotych.gradle.maven.exec.MavenExecPlugin"
            displayName = project.description
            description = "Gradle plugin which provides an Maven exec task"
            tags.set(setOf("maven", "exec", "cross-platform"))
        }
    }
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter()
        }
        val functionalTest by registering(JvmTestSuite::class) {
            dependencies {
                implementation(libs.assertj.core)
            }
            targets {
                all {
                    testTask.configure {
                        shouldRunAfter(test)
                        dependsOn(tasks["prepareTestEnvironments"])
                    }
                }
            }
        }
    }
}

gradlePlugin.testSourceSets.add(sourceSets["functionalTest"])

tasks.named<Task>("check") {
    dependsOn(testing.suites.named("functionalTest"))
}

develocity {
    buildScan {
        termsOfUseUrl = "https://gradle.com/terms-of-service"
        termsOfUseAgree = "yes"
        if (System.getenv("CI") != null) {
            tag("GitHub")
            tag(project.version.toString())
            uploadInBackground = false
            link("VCS", github.vcsUrl + "/tree/${System.getenv("GITHUB_REF_NAME")}")
        } else {
            tag("local")
        }
        tag(System.getProperty("os.name"))
    }
}

license {
    header = file("gradle/config/license-header.txt")
    ext.set("year", Calendar.getInstance().get(Calendar.YEAR))
    ext.set("developers", "Dmitry Korotych")
    skipExistingHeaders = true
    strictCheck = true
    exclude("**/fixtures/**")
}

tasks.named<Javadoc>("javadoc").configure {
    (options as StandardJavadocDocletOptions).links("https://docs.gradle.org/${gradle.gradleVersion}/javadoc/")
    options {
        showFromPublic()
    }
}

idea {
    project {
        jdkName = java.sourceCompatibility.toString()
        module {
            isDownloadJavadoc = false
            isDownloadSources = true
        }
    }
}

tasks.named<Task>("cleanIdea") {
    doLast {
        delete(file(".idea"))
    }
}

tasks.named<JacocoReport>("jacocoTestReport").configure {
    reports {
        xml.required = true
    }
}

sonar {
    properties {
        property("sonar.projectKey", "${github.user}_${rootProject.name}")
        property("sonar.organization", github.user)
        property("sonar.host.url", "https://sonarcloud.io")
        val functionalTestSources = sourceSets["functionalTest"]

        @Suppress("UNCHECKED_CAST")
        val sonarTestSources = properties["sonar.tests"] as MutableCollection<File>
        sonarTestSources.addAll(functionalTestSources.allJava.srcDirs)
        sonarTestSources.add(file("buildSrc/src/main/java"))
        @Suppress("UNCHECKED_CAST")
        properties["sonar.java.binaries"] as MutableCollection<File> += functionalTestSources.java.classesDirectory.get().asFile
    }
}

tasks.named<Wrapper>("wrapper") {
    gradleVersion = "8.5"
}

release {
    git {
        requireBranch.set("master")
    }
}
tasks.named<Task>("afterReleaseBuild") {
    dependsOn(tasks.publishPlugins)
    doLast {
        logger.warn("RELEASED $project.group:$project.name:$project.version")
    }
}

tasks.named<DependencyUpdatesTask>("dependencyUpdates").configure {
    checkForGradleUpdate = false
    revision = "release"
    rejectVersionIf {
        val version = candidate.version
        version.contains("alpha")
                || version.contains("beta")
                || ".+-M\\d+$".toRegex().matches(version)
                || ".+-RC\\d+$".toRegex().matches(version)
    }
}

tasks.named("updateVersion").configure {
    doFirst {
        val versionPattern = "\\d+(?:\\.\\d+)+"
        val file = file("README.md")
        var content = file.readText()
        content = content.replace("id \"com.github.dkorotych.gradle-maven-exec\" version \"${versionPattern}\"".toRegex(),
                "id \"com.github.dkorotych.gradle-maven-exec\" version \"${version}\"")
        content = content.replace("${rootProject.name}:${versionPattern}".toRegex(),
                "${rootProject.name}:${version}")
        file.writeText(content)
    }
}

rewrite {
    configFile = project.rootProject.file("gradle/config/rewrite.yml")
    activeRecipe("com.github.dkorotych.gradle.maven.exec.MavenExecPlugin")
    exclusion("**/*.gradle")
}
