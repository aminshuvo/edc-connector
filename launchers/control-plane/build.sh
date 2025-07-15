#!/bin/bash
# Build script for the EDC Control Plane launcher
set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$SCRIPT_DIR/../.."
cd "$PROJECT_ROOT"

# Build the control plane JAR
./gradlew :launchers:control-plane:build

# Build the Docker image
DOCKER_BUILDKIT=1 docker build \
  --build-arg JAR=../build/libs/control-plane.jar \
  -t edc-control-plane:latest \
  launchers/control-plane

echo "Control plane Docker image built: edc-control-plane:latest" 