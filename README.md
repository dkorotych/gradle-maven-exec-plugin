# [gradle-maven-exec-plugin](https://plugins.gradle.org/plugin/com.github.dkorotych.gradle-maven-exec)

Gradle plugin which provides an Maven exec task

[![Build Status](https://travis-ci.org/dkorotych/gradle-maven-exec-plugin.svg?branch=master)](https://travis-ci.org/dkorotych/gradle-maven-exec-plugin)
[![codecov](https://codecov.io/gh/dkorotych/gradle-maven-exec-plugin/branch/master/graph/badge.svg)](https://codecov.io/gh/dkorotych/gradle-maven-exec-plugin)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/7c2907ecd1d749deb5c3765bd86cbf72)](https://www.codacy.com/app/dkorotych/gradle-maven-exec-plugin?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=dkorotych/gradle-maven-exec-plugin&amp;utm_campaign=Badge_Grade)
[![Codacy Badge](https://api.codacy.com/project/badge/Coverage/7c2907ecd1d749deb5c3765bd86cbf72)](https://www.codacy.com/app/dkorotych/gradle-maven-exec-plugin?utm_source=github.com&utm_medium=referral&utm_content=dkorotych/gradle-maven-exec-plugin&utm_campaign=Badge_Coverage)
[![license](https://img.shields.io/github/license/dkorotych/gradle-maven-exec-plugin.svg)](https://github.com/dkorotych/gradle-maven-exec-plugin.git)

## Installing

Releases of this plugin are hosted on [Gradle's Plugin Repository](https://login.gradle.org/plugin/com.github.dkorotych.gradle-maven-exec).
Apply the plugin to your project using one of the two methods below.

#### Build script snippet for use in all Gradle versions:

```groovy
buildscript {
    repositories {
        maven {
            url "https://plugins.gradle.org/m2/"
        }
    }
    dependencies {
        classpath "gradle.plugin.com.github.dkorotych.gradle.maven.exec:gradle-maven-exec-plugin:1.2.2"
    }
}

apply plugin: "com.github.dkorotych.gradle-maven-exec"
```

#### Gradle 2.1 and newer
Build script snippet for new, incubating, plugin mechanism introduced in Gradle 2.1:

```groovy
plugins {
    id "com.github.dkorotych.gradle-maven-exec" version "1.2.2"
}
```

## Usage

This plugin enables a `MavenExec` task type in your buildscript which behaves exactly like a typical Gradle
`Exec` task, except that it normalizes calls to work across operating systems. It does this by:

1. Automatic generation of correct command line
2. Prepending `mvn.cmd` command with `cmd /c` on Windows
3. Used Maven Wrapper if it exists in project
3. It is forbidden to direct control of the command line arguments
4. Removing unsupported command-line options when you call different Maven versions

To define a `MavenExec` task, simply specify the task `type`:

```groovy
task rebuild(type: MavenExec) {
    goals 'clean', 'package'
}
```

or passing the advanced command-line options:

```groovy
task generateSite(type: MavenExec) {
    goals 'clean', 'package', 'site'
    options {
        threads = '2C'
        activateProfiles = ['development', 'site']
        define = [
            'groupId'   : 'com.github.application',
            'artifactId': 'parent'
        ]
    }
}
```

or used it without options closure:

```groovy
task generateSite(type: MavenExec) {
    goals 'archetype:generate'
    define([
        'groupId'            : 'com.mycompany.app',
        'artifactId'         : 'my-app',
        'archetypeArtifactId': 'maven-archetype-quickstart',
        'interactiveMode'    : 'false'
    ])
    quiet true
}
```

or even a direct call to control Maven from any task

```groovy
task generateAndExecuteApplication {
    doLast {
        buildDir.mkdirs()
        def groupId = 'com.mycompany.app'
        def application = 'my-app'
        def applicationDir = file("$buildDir/$application")
        delete applicationDir
        mavenexec {
            workingDir buildDir
            goals 'archetype:generate'
            define([
                    'groupId'            : groupId,
                    'artifactId'         : application,
                    'archetypeArtifactId': 'maven-archetype-quickstart',
                    'interactiveMode'    : 'false'
            ])
            quiet true
        }
        mavenexec {
            workingDir applicationDir
            goals 'package'
            threads '2C'
            quiet true
        }
        javaexec {
            workingDir applicationDir
            classpath file("$applicationDir/target/$application-1.0-SNAPSHOT.jar")
            main "${groupId}.App"
        }
    }
}
```
