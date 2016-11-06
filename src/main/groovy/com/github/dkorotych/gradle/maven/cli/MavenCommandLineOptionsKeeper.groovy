package com.github.dkorotych.gradle.maven.cli

import groovy.transform.PackageScope
import groovy.transform.ToString

/**
 * @author Dmitry Korotych (dkorotych at gmail dot com)
 */
@PackageScope
@ToString(includeNames = true, ignoreNulls = true)
class MavenCommandLineOptionsKeeper {
    private static final String DEFINE_OPTION = '--define'

    private final Map<String, String> options = [:]
    private final Map<String, String> systemProperties = [:]

    void addOption(String option, Boolean value) {
        if (isNotBlank(option)) {
            if (value != null) {
                if (Boolean.FALSE == value) {
                    options.remove(option)
                } else {
                    options[option] = ''
                }
            }
        }
    }

    void addOption(String option, String value) {
        if (isNotBlank(option)) {
            if (isNotBlank(value)) {
                options[option] = value
            }
        }
    }

    void addOption(String option, List<String> value) {
        if (value && !value.isEmpty()) {
            addOption(option, value.findAll { isNotBlank(it) }.join(','))
        }
    }

    void addOption(String option, String[] value) {
        if (value) {
            addOption(option, Arrays.asList(value))
        }
    }

    void addOption(String option, File value) {
        if (value) {
            addOption(option, value.absolutePath)
        }
    }

    void addOption(String option, Map<String, String> value) {
        if (value && !value.isEmpty()) {
            if (option == DEFINE_OPTION) {
                Map<String, String> properties = value.findAll { isNotBlank(it.key) && isNotBlank(it.value) }
                systemProperties.clear()
                systemProperties.putAll(properties)
            } else {
                throw new IllegalArgumentException("Only support '${DEFINE_OPTION}' option with multiple values")
            }
        }
    }

    @SuppressWarnings('UnusedMethodParameter')
    void addOption(String option, Object value) {
        throw new UnsupportedOperationException()
    }

    List<String> toCommandLine() {
        List<String> value = []
        systemProperties.each {
            value << "-D${it.key}=${it.value}"
        }
        options.each {
            value << it.key
            if (isNotBlank(it.value)) {
                value << it.value
            }
        }
        value
    }

    private boolean isNotBlank(String option) {
        option != null ? !option.trim().isEmpty() : false
    }
}
