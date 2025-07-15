#!/bin/bash
# EDC Data Plane Run Script

# Load environment variables from the sample file
set -a
source edc-dataplane-sample.env
set +a

# Run the data plane with configuration file
java -jar launchers/data-plane/build/libs/data-plane.jar \
  --config edc-dataplane-sample.properties 