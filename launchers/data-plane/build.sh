#!/bin/bash
# Build script for the EDC Data Plane launcher
set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$SCRIPT_DIR/../.."
cd "$PROJECT_ROOT"

# Build the data plane JAR
./gradlew :launchers:data-plane:build

# Build the Docker image with the JAR as build argument
# Use relative path from the launcher directory
cd launchers/data-plane
docker build \
  --build-arg JAR=../build/libs/data-plane.jar \
  -t nuruldhamar/edc-dataplane:latest .

echo "Data plane Docker image built: nuruldhamar/edc-dataplane:latest" 