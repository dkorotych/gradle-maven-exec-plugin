#!/usr/bin/env bash

set -e

./gradlew check realUseCaseTest
if [ -z "$ONLY_CHECK" ]; then
    ./gradlew cobertura
    ~/jpm/bin/codacy-coverage-reporter --language Java --coverageReport build/reports/cobertura/coverage.xml --projectToken "$codacy_project_token"
    ./gradlew clean sonarqube -Dsonar.host.url=$sonar_host -Dsonar.login=$sonar_login
    ./codecov.sh
fi
