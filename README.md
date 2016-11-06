# gradle-maven-exec-plugin

Gradle plugin which provides an Maven exec task

## Installing

Releases of this plugin are hosted on [Gradle's Plugin Repository](https://login.gradle.org/plugin/com.github.dkorotych.gradle-maven-exec).
Apply the plugin to your project using one of the two methods below.

### Gradle 2.0 and older

```groovy
buildscript {
	repositories {
		maven {
			url "https://plugins.gradle.org/m2/"
		}
	}
	dependencies {
		classpath 'gradle.plugin.com.github.dkorotych.gradle.maven.exec:gradle-maven-exec-plugin:0.1'
	}
}

apply plugin: 'com.github.dkorotych.gradle-maven-exec'
```

### Gradle 2.1 and newer

```groovy
plugins {
	id 'com.github.dkorotych.gradle-maven-exec' version '0.1'
}
```

## Usage

This plugin enables a `MavenExec` task type in your buildscript which behaves exactly like a typical Gradle
`Exec` task, except that it normalizes calls to work across operating systems. It does this by:

1. Automatic generation of correct command line
2. Prepending `mvn.cmd` command with `cmd /c` on Windows
3. It is forbidden to direct control of the command line arguments
4. Removing unsupported command-line options when you call different Maven versions. (Not implemented yet)

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