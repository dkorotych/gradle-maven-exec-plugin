package com.github.dkorotych.gradle.maven.cli

import groovy.transform.PackageScope
import groovy.transform.ToString
import org.apache.commons.lang.StringUtils

/**
 * Storage for command line options.
 *
 * @author Dmitry Korotych (dkorotych at gmail dot com)
 */
@PackageScope
@ToString(includeNames = true, ignoreNulls = true)
class MavenCommandLineOptionsKeeper {
    private static final String DEFINE_OPTION = '--define'

    private final Map<String, String> options = [:]
    private final Map<String, String> systemProperties = [:]

    /**
     * Add command-line option. If the option is already exists in the storage and the new value is {@code false},
     * then the option will be deleted from the list
     *
     * @param option Command-line option. Should be not blank
     * @param value Option value
     */
    void addOption(String option, Boolean value) {
        if (StringUtils.isNotBlank(option)) {
            if (value != null) {
                if (value) {
                    options[option] = ''
                } else {
                    options.remove(option)
                }
            }
        }
    }

    /**
     * Add command-line option.
     *
     * @param option Command-line option. Should be not blank
     * @param value Option value. Should be not blank
     */
    void addOption(String option, String value) {
        if (StringUtils.isNotBlank(option)) {
            if (StringUtils.isNotBlank(value)) {
                options[option] = value
            }
        }
    }

    /**
     * Add command-line option.
     *
     * @param option Command-line option. Should be not blank
     * @param value Option value. Should be not empty
     * @see #addOption(java.lang.String, java.lang.String)
     */
    void addOption(String option, List<String> value) {
        if (value != null) {
            addOption(option, value.findAll { StringUtils.isNotBlank(it) }.join(','))
        }
    }

    /**
     * Add command-line option.
     *
     * @param option Command-line option. Should be not blank
     * @param value Option value. Should be not empty
     * @see #addOption(java.lang.String, java.util.List)
     */
    void addOption(String option, String[] value) {
        if (value != null) {
            addOption(option, Arrays.asList(value))
        }
    }

    /**
     * Add command-line option.
     *
     * @param option Command-line option. Should be not blank
     * @param value Option value. Should be not null
     */
    void addOption(String option, File value) {
        if (value != null) {
            addOption(option, value.absolutePath)
        }
    }

    /**
     * Add command-line option.
     *
     * @param option Command-line option. Should be not blank
     * @param value Option value. Should be not empty
     */
    void addOption(String option, Map<String, String> value) {
        if (StringUtils.isNotBlank(option)) {
            if (option == DEFINE_OPTION) {
                if (value != null) {
                    Map<String, String> properties = value.findAll {
                        StringUtils.isNotBlank(it.key) && StringUtils.isNotBlank(it.value)
                    }
                    systemProperties.clear()
                    systemProperties.putAll(properties)
                }
            } else {
                throw new IllegalArgumentException("Only support '${DEFINE_OPTION}' option with multiple values")
            }
        }
    }

    /**
     * Add command-line option. This method always generate {@code UnsupportedOperationException}
     *
     * @param option Command-line option
     * @param value Option value
     */
    @SuppressWarnings('UnusedMethodParameter')
    void addOption(String option, Object value) {
        throw new UnsupportedOperationException()
    }

    /**
     * Build command line options.
     *
     * @return Command line options list
     */
    List<String> toCommandLine() {
        List<String> value = []
        systemProperties.each {
            value << "-D${it.key}=${it.value}"
        }
        options.each {
            value << it.key
            if (StringUtils.isNotBlank(it.value)) {
                value << it.value
            }
        }
        value
    }
}
