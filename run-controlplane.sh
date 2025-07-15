#!/bin/bash
# EDC Control Plane Run Script

# Load environment variables from the sample file
set -a
source edc-controlplane-sample.env
set +a

# Run the control plane with configuration file
java -jar launchers/control-plane/build/libs/control-plane.jar \
  --config edc-controlplane-sample.properties 