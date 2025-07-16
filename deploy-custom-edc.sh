#!/bin/bash

# Deploy EDC with custom images
# This script deploys the EDC standalone with our custom control plane and data plane images

set -e

echo "🚀 Deploying EDC with custom images..."

# Check if we're in the right directory
if [ ! -f "helm/edc-standalone/values.yaml" ]; then
    echo "❌ Error: Helm chart not found. Please run this script from the project root."
    exit 1
fi

# Check if kubectl is available
if ! command -v kubectl &> /dev/null; then
    echo "❌ Error: kubectl is not installed or not in PATH"
    exit 1
fi

# Check if helm is available
if ! command -v helm &> /dev/null; then
    echo "❌ Error: helm is not installed or not in PATH"
    exit 1
fi

echo "📋 Current deployment status:"
kubectl -n edc-standalone get all

echo ""
echo "🔄 Upgrading EDC deployment with custom images..."

# Upgrade the Helm release with our custom values
helm upgrade edc-standalone ./helm/edc-standalone \
  --namespace edc-standalone \
  --values ./helm/edc-standalone/values.yaml \
  --wait \
  --timeout 10m

echo ""
echo "✅ Deployment completed!"
echo ""
echo "📊 New deployment status:"
kubectl -n edc-standalone get all

echo ""
echo "🔍 Checking pod status:"
kubectl -n edc-standalone get pods

echo ""
echo "🌐 Service endpoints:"
echo "Control Plane:"
echo "  - HTTP API: http://localhost:8080"
echo "  - Management API: http://localhost:8081/api/v1/data"
echo "  - Control API: http://localhost:8083/api/v1/control"
echo "  - DSP Protocol: http://localhost:8084/api/v1/dsp"
echo "  - Catalog API: http://localhost:8085/api/v1/catalog"
echo "  - Metrics: http://localhost:9090/metrics"
echo ""
echo "Data Plane:"
echo "  - HTTP API: http://localhost:8080"
echo "  - Public API: http://localhost:8081/api/public"
echo "  - Control API: http://localhost:8084/api/v1/control"
echo "  - Proxy API: http://localhost:8186/api/v1/data"
echo "  - Metrics: http://localhost:9090/metrics"

echo ""
echo "🎉 EDC deployment with custom images is ready!" 