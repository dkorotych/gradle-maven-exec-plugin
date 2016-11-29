#!/usr/bin/env bash

set -e

./gradlew groovydoc
cd build/docs/groovydoc

git init
git config user.name "Travis"
git config user.email "travis@travis-ci.org"
git add .
git commit -m "Deployed to Github Pages"
git push --force --quiet "https://${GH_TOKEN}@github.com/${TRAVIS_REPO_SLUG}" master:gh-pages > /dev/null 2>&1
