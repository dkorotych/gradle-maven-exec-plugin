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

public class GenerateHelpTask extends AbstractGenerationTask {
    private File optionsFile;

    public GenerateHelpTask() {
        super("help.txt", Collections.singletonList("--help"));
    }

    @Override
    public void setVersion(String version) {
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
        try (final BufferedReader reader = Files.newBufferedReader(outputFile.toPath());
             final BufferedWriter writer = Files.newBufferedWriter(optionsFile.toPath())) {
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
