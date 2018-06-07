#!/usr/bin/env bash

set -e

./gradlew clean install check realUseCaseTest
