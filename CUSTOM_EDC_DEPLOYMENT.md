# Custom EDC Deployment with Custom Images

## Overview

This document describes how to deploy EDC (Eclipse Dataspace Connector) using our custom Docker images instead of the default Tractus-X EDC images.

## Custom Images

We have built and pushed two custom Docker images:

- **Control Plane**: `nuruldhamar/controlplane:latest`
- **Data Plane**: `nuruldhamar/dataplane:latest`

### Image Features

Both images include:
- ✅ **DSP Protocol** enabled for both components
- ✅ **OpenTelemetry observability** with console exporters
- ✅ **Tractus-X EDC port mapping** (8080, 8081, 8083, 8084, 8085, 8186, 9090)
- ✅ **Proper logging and metrics** configuration
- ✅ **Non-root user** security (docker:10100)
- ✅ **Java 17** runtime (eclipse-temurin:24.0.1_9-jre-alpine)

## Port Configuration

### Control Plane Ports
- **8080**: HTTP API (default)
- **8081**: Management API (`/api/v1/data`)
- **8083**: Control API (`/api/v1/control`)
- **8084**: DSP Protocol (`/api/v1/dsp`)
- **8085**: Catalog API (`/api/v1/catalog`)
- **9090**: Metrics (`/metrics`)

### Data Plane Ports
- **8080**: HTTP API (default)
- **8081**: Public API (`/api/public`)
- **8084**: Control API (`/api/v1/control`)
- **8186**: Proxy API (`/api/v1/data`)
- **9090**: Metrics (`/metrics`)

## Helm Chart Modifications

### 1. Values Configuration (`helm/edc-standalone/values.yaml`)

Updated to use custom images:
```yaml
edc:
  images:
    controlPlane:
      repository: nuruldhamar/controlplane
      tag: "latest"
      pullPolicy: IfNotPresent
    dataPlane:
      repository: nuruldhamar/dataplane
      tag: "latest"
      pullPolicy: IfNotPresent
```

Updated port configuration:
```yaml
ports:
  http: 8080
  data: 8081
  management: 8081
  control: 8083
  protocol: 8084
  catalog: 8085
  proxy: 8186
  metrics: 9090
```

### 2. Deployment Templates

#### Control Plane (`helm/edc-standalone/templates/standalone/deployment-control-plane.yaml`)
- Uses `nuruldhamar/controlplane:latest`
- Exposes all control plane ports (8080, 8081, 8083, 8084, 8085, 9090)
- Removed ConfigMap volume mount (configuration embedded in image)

#### Data Plane (`helm/edc-standalone/templates/standalone/deployment-data-plane.yaml`)
- Uses `nuruldhamar/dataplane:latest`
- Exposes all data plane ports (8080, 8081, 8084, 8186, 9090)
- Removed ConfigMap volume mount (configuration embedded in image)

### 3. Service Templates

#### Control Plane Service (`helm/edc-standalone/templates/standalone/service-control-plane.yaml`)
- Exposes all control plane ports
- Updated selectors for proper routing

#### Data Plane Service (`helm/edc-standalone/templates/standalone/service-data-plane.yaml`)
- Exposes all data plane ports
- Updated selectors for proper routing

### 4. Peer Components

Similar updates applied to peer control plane and data plane components.

## Deployment

### Prerequisites

1. **Kubernetes cluster** running
2. **kubectl** configured
3. **Helm** installed
4. **Custom images** built and pushed to Docker Hub

### Deployment Steps

1. **Build and push images** (if not already done):
   ```bash
   # Control Plane
   cd launchers/control-plane
   ./build.sh
   
   # Data Plane
   cd launchers/data-plane
   ./build.sh
   ```

2. **Deploy using the custom Helm chart**:
   ```bash
   ./deploy-custom-edc.sh
   ```

   Or manually:
   ```bash
   helm upgrade edc-standalone ./helm/edc-standalone \
     --namespace edc-standalone \
     --values ./helm/edc-standalone/values.yaml \
     --wait \
     --timeout 10m
   ```

### Verification

Check deployment status:
```bash
kubectl -n edc-standalone get all
kubectl -n edc-standalone get pods
```

Check service endpoints:
```bash
kubectl -n edc-standalone get services
```

## Service Endpoints

### Control Plane
- **HTTP API**: `http://localhost:8080`
- **Management API**: `http://localhost:8081/api/v1/data`
- **Control API**: `http://localhost:8083/api/v1/control`
- **DSP Protocol**: `http://localhost:8084/api/v1/dsp`
- **Catalog API**: `http://localhost:8085/api/v1/catalog`
- **Metrics**: `http://localhost:9090/metrics`

### Data Plane
- **HTTP API**: `http://localhost:8080`
- **Public API**: `http://localhost:8081/api/public`
- **Control API**: `http://localhost:8084/api/v1/control`
- **Proxy API**: `http://localhost:8186/api/v1/data`
- **Metrics**: `http://localhost:9090/metrics`

## Key Differences from Original

### 1. Image Configuration
- **Original**: Single `tractusx/edc-runtime-memory:0.7.0` image
- **Custom**: Separate `nuruldhamar/controlplane:latest` and `nuruldhamar/dataplane:latest`

### 2. Port Configuration
- **Original**: Single port 8181 for all services
- **Custom**: Multiple ports (8080, 8081, 8083, 8084, 8085, 8186, 9090)

### 3. Configuration Management
- **Original**: Configuration mounted from ConfigMap
- **Custom**: Configuration embedded in Docker images

### 4. DSP Protocol
- **Original**: Not explicitly enabled
- **Custom**: DSP protocol enabled and configured

### 5. Observability
- **Original**: Basic metrics
- **Custom**: OpenTelemetry integration with console exporters

## Troubleshooting

### Common Issues

1. **Image Pull Errors**
   ```bash
   kubectl -n edc-standalone describe pod <pod-name>
   ```
   Check if images are accessible and properly tagged.

2. **Port Conflicts**
   ```bash
   kubectl -n edc-standalone get services
   ```
   Verify service ports are correctly configured.

3. **Configuration Issues**
   ```bash
   kubectl -n edc-standalone logs <pod-name>
   ```
   Check application logs for configuration errors.

### Rollback

To rollback to original deployment:
```bash
helm rollback edc-standalone --namespace edc-standalone
```

## Files Modified

### Helm Chart Files
- `helm/edc-standalone/values.yaml` - Updated image and port configuration
- `helm/edc-standalone/templates/standalone/deployment-control-plane.yaml` - Custom control plane deployment
- `helm/edc-standalone/templates/standalone/deployment-data-plane.yaml` - Custom data plane deployment
- `helm/edc-standalone/templates/standalone/service-control-plane.yaml` - Control plane service
- `helm/edc-standalone/templates/standalone/service-data-plane.yaml` - Data plane service
- `helm/edc-standalone/templates/peer/deployment-control-plane.yaml` - Peer control plane deployment
- `helm/edc-standalone/templates/peer/deployment-data-plane.yaml` - Peer data plane deployment
- `helm/edc-standalone/templates/peer/service-control-plane.yaml` - Peer control plane service
- `helm/edc-standalone/templates/peer/service-data-plane.yaml` - Peer data plane service

### Scripts
- `deploy-custom-edc.sh` - Deployment script

## Next Steps

1. **Test DSP Protocol**: Verify DSP endpoints are accessible and functional
2. **Integration Testing**: Test inter-connector communication
3. **Performance Testing**: Monitor resource usage and performance
4. **Security Review**: Review security configurations
5. **Production Deployment**: Prepare for production environment

## Support

For issues or questions:
1. Check pod logs: `kubectl -n edc-standalone logs <pod-name>`
2. Check service status: `kubectl -n edc-standalone get services`
3. Check deployment status: `kubectl -n edc-standalone get deployments` 