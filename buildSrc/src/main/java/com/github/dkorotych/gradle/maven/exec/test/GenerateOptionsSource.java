package com.github.dkorotych.gradle.maven.exec.test;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Set;
import java.util.stream.Collectors;

public class GenerateOptionsSource extends DefaultTask {
    @TaskAction
    public void generate() throws Exception {
        final Set<String> allOptions = getDependsOn().stream()
                .filter(GenerateHelpTask.class::isInstance)
                .map(GenerateHelpTask.class::cast)
                .map(GenerateHelpTask::getOptionsFile)
                .map(File::toPath)
                .flatMap(path -> {
                    try {
                        return Files.lines(path, StandardCharsets.UTF_8);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toSet());
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = documentBuilder.parse(getClass().getResourceAsStream("/options.xml"));
        NodeList elements = document.getElementsByTagName("option");
        int length = elements.getLength();
        Node optionNode;
        for (int i = 0; i < length; i++) {
            optionNode = elements.item(i);
            String name = "--" + optionNode.getAttributes().getNamedItem("name").getNodeValue();
            if (!allOptions.contains(name)) {
                throw new RuntimeException(name + " not found in options descriptor file");
            } else {
                allOptions.remove(name);
            }
        }
        if (!allOptions.isEmpty()) {
            throw new RuntimeException("Some elements not processed - " + allOptions);
        }
    }
}
