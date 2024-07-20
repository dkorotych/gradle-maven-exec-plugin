import java.util.*

/*
 * Copyright 2022 Dmitry Korotych.
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
plugins {
    id("ru.vyarus.quality") version "5.0.0"
    id("com.github.hierynomus.license") version "0.16.1"
}

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
}

license {
    header = file("../gradle/config/license-header.txt")
    ext.set("year", Calendar.getInstance().get(Calendar.YEAR))
    ext.set("developers", "Dmitry Korotych")
    skipExistingHeaders = true
    strictCheck = true
}
