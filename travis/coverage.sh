#!/usr/bin/env bash

set -ex

readonly url=$(curl -s https://api.github.com/repos/codacy/codacy-coverage-reporter/releases/latest | jq -r .assets[0].browser_download_url)

wget -q -N -c -O "$CODACY_JAR" "$url"
(curl -s https://codecov.io/bash) > codecov.sh
chmod +x codecov.sh

./gradlew cobertura
# Send information to https://codacy.com
java -cp "$CODACY_JAR" com.codacy.CodacyCoverageReporter --language Java --coverageReport build/reports/cobertura/coverage.xml --projectToken "$codacy_project_token"
# Send information to https://sonarqube.com
#    ./gradlew sonarqube -Dsonar.host.url=$sonar_host -Dsonar.login=$sonar_login
./codecov.sh
