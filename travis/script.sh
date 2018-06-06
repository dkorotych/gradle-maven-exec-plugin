#!/usr/bin/env bash

set -e

./gradlew install check realUseCaseTest
if [ -z "$ONLY_CHECK" ]; then
    ./gradlew cobertura
    # Send information to https://codacy.com
    java -cp ~/codacy-coverage-reporter-assembly-latest.jar com.codacy.CodacyCoverageReporter --language Java --coverageReport build/reports/cobertura/coverage.xml --projectToken "$codacy_project_token"
    # Send information to https://sonarqube.com
#    ./gradlew sonarqube -Dsonar.host.url=$sonar_host -Dsonar.login=$sonar_login
    ./codecov.sh
fi

if [ -z "$ADDITIONAL_CHECK" ]; then
    for jdk in jdk9 jdk10;
    do
        jdk_switcher use $jdk
        gradle additionalUseCaseTest
    done
fi
