#!/usr/bin/env bash

set -e

./gradlew check realUseCaseTest
if [ -z "$ONLY_CHECK" ]; then
    ./gradlew cobertura
    # Send information to https://codacy.com
    curl -sL https://github.com/jpm4j/jpm4j.installers/raw/master/dist/biz.aQute.jpm.run.jar > jpm4j.jar
    java -jar jpm4j.jar -u init
    ~/jpm/bin/jpm install com.codacy:codacy-coverage-reporter:assembly
    ~/jpm/bin/codacy-coverage-reporter --language Java --coverageReport build/reports/cobertura/coverage.xml --projectToken "$codacy_project_token"
    # Send information to https://sonarqube.com
    ./gradlew sonarqube -Dsonar.host.url=$sonar_host -Dsonar.login=$sonar_login
    ./codecov.sh
fi
