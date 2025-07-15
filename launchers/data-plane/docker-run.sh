#!/bin/bash

# Docker run script for EDC Data Plane
# Configuration via environment variables

echo "Starting EDC Data Plane container..."

# Default environment variables
export WEB_HTTP_DEFAULT_PORT=${WEB_HTTP_DEFAULT_PORT:-8080}
export WEB_HTTP_PUBLIC_PORT=${WEB_HTTP_PUBLIC_PORT:-8081}
export WEB_HTTP_CONTROL_PORT=${WEB_HTTP_CONTROL_PORT:-8084}
export WEB_HTTP_PROXY_PORT=${WEB_HTTP_PROXY_PORT:-8186}
export WEB_HTTP_METRICS_PORT=${WEB_HTTP_METRICS_PORT:-9090}

export EDC_HOSTNAME=${EDC_HOSTNAME:-localhost}
export EDC_PARTICIPANT_ID=${EDC_PARTICIPANT_ID:-data-plane}
export EDC_STORAGE_TYPE=${EDC_STORAGE_TYPE:-in-memory}
export EDC_AUTH_TYPE=${EDC_AUTH_TYPE:-none}

# Control plane endpoint for data plane communication
export EDC_CONTROLPLANE_ENDPOINT_URL=${EDC_CONTROLPLANE_ENDPOINT_URL:-http://localhost:8081}

docker run -d \
  --name edc-data-plane \
  --network host \
  -p ${WEB_HTTP_DEFAULT_PORT}:${WEB_HTTP_DEFAULT_PORT} \
  -p ${WEB_HTTP_PUBLIC_PORT}:${WEB_HTTP_PUBLIC_PORT} \
  -p ${WEB_HTTP_CONTROL_PORT}:${WEB_HTTP_CONTROL_PORT} \
  -p ${WEB_HTTP_PROXY_PORT}:${WEB_HTTP_PROXY_PORT} \
  -p ${WEB_HTTP_METRICS_PORT}:${WEB_HTTP_METRICS_PORT} \
  -e WEB_HTTP_DEFAULT_PORT=${WEB_HTTP_DEFAULT_PORT} \
  -e WEB_HTTP_PUBLIC_PORT=${WEB_HTTP_PUBLIC_PORT} \
  -e WEB_HTTP_CONTROL_PORT=${WEB_HTTP_CONTROL_PORT} \
  -e WEB_HTTP_PROXY_PORT=${WEB_HTTP_PROXY_PORT} \
  -e WEB_HTTP_METRICS_PORT=${WEB_HTTP_METRICS_PORT} \
  -e EDC_HOSTNAME=${EDC_HOSTNAME} \
  -e EDC_PARTICIPANT_ID=${EDC_PARTICIPANT_ID} \
  -e EDC_STORAGE_TYPE=${EDC_STORAGE_TYPE} \
  -e EDC_AUTH_TYPE=${EDC_AUTH_TYPE} \
  -e EDC_CONTROLPLANE_ENDPOINT_URL=${EDC_CONTROLPLANE_ENDPOINT_URL} \
  edc-data-plane:latest

echo "EDC Data Plane started!"
echo "Public API: http://localhost:${WEB_HTTP_PUBLIC_PORT}/api/public"
echo "Control API: http://localhost:${WEB_HTTP_CONTROL_PORT}/api/v1/control"
echo "Proxy API: http://localhost:${WEB_HTTP_PROXY_PORT}/api/v1/data"
echo "Metrics: http://localhost:${WEB_HTTP_METRICS_PORT}/metrics" 