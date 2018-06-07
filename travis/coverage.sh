#!/usr/bin/env bash

set -e

./gradlew cobertura
# Send information to https://codacy.com
java -cp ~/codacy-coverage-reporter-assembly-latest.jar com.codacy.CodacyCoverageReporter --language Java --coverageReport build/reports/cobertura/coverage.xml --projectToken "$codacy_project_token"
# Send information to https://sonarqube.com
#    ./gradlew sonarqube -Dsonar.host.url=$sonar_host -Dsonar.login=$sonar_login
./codecov.sh
