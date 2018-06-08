#!/usr/bin/env bash

set -e

# Send information to https://codacy.com
./gradlew jacocoTestReportCodacyUpload --codacy-token="$codacy_project_token"
# Send information to https://sonarqube.com
#    ./gradlew sonarqube -Dsonar.host.url=$sonar_host -Dsonar.login=$sonar_login
(curl -s https://codecov.io/bash) | bash
