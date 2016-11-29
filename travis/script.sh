#!/usr/bin/env bash

set -e

cd ..

./gradlew check
if [ -z "$ONLY_CHECK" ]; then
    ./gradlew cobertura
    ./gradlew sonarqube -Dsonar.host.url=$sonar_host -Dsonar.login=$sonar_login
    ./codecov.sh
fi
