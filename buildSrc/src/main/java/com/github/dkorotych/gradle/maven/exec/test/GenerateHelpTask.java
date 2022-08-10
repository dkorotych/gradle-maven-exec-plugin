/**
 * Copyright 2022 Dmitry Korotych
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.dkorotych.gradle.maven.exec.test;

import org.gradle.api.GradleException;
import org.gradle.api.tasks.OutputFile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("MissingJavadocType")
public class GenerateHelpTask extends AbstractGenerationTask {
    private File optionsFile;

    public GenerateHelpTask() {
        super("help.txt", Collections.singletonList("--help"));
    }

    @Override
    public void setVersion(final String version) {
        super.setVersion(version);
        optionsFile = outputFile.getParentFile().toPath().resolve("options.txt").toFile();
        getOutputs().file(optionsFile);
    }

    @OutputFile
    public File getOptionsFile() {
        return optionsFile;
    }

    @Override
    public void generate() {
        super.generate();
        generateOptions();
    }

    private void generateOptions() {
        final Pattern pattern = Pattern.compile("^.+(--\\S+).+$");
        try (BufferedReader reader = Files.newBufferedReader(outputFile.toPath());
             BufferedWriter writer = Files.newBufferedWriter(optionsFile.toPath())) {
            reader.lines()
                    .map(pattern::matcher)
                    .filter(Matcher::find)
                    .map(matcher -> matcher.group(1))
                    .forEach(line -> {
                        try {
                            writer.write(line);
                            writer.write(System.lineSeparator());
                        } catch (IOException e) {
                            throw new GradleException("Can't write line to options file", e);
                        }
                    });
        } catch (IOException e) {
            throw new GradleException("Can't create options file", e);
        }
    }
}
