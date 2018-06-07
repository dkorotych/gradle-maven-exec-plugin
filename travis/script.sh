#!/usr/bin/env bash

set -e

./gradlew install check realUseCaseTest
