# EDC Helm Chart

This Helm chart deploys the Eclipse Dataspace Connector (EDC) with separate Control Plane and Data Plane components.

## Prerequisites

- Kubernetes 1.19+
- Helm 3.0+
- Ingress controller (if using ingress)
- Vault server (if using Vault integration)

## Installation

### Basic Installation

```bash
# Add the chart repository (if using a repository)
helm repo add edc https://your-repo-url

# Install the chart
helm install edc ./helm/edc

# Or install with custom values
helm install edc ./helm/edc -f custom-values.yaml
```

### Installation with Custom Values

Create a `custom-values.yaml` file:

```yaml
# Custom values example
namespace:
  name: "my-edc"

controlPlane:
  image:
    repository: my-registry/edc-controlplane
    tag: "v1.0.0"
  
  env:
    EDC_PARTICIPANT_ID: "my-control-plane"
    EDC_HOSTNAME: "my-control-plane"

dataPlane:
  image:
    repository: my-registry/edc-dataplane
    tag: "v1.0.0"
  
  env:
    EDC_PARTICIPANT_ID: "my-data-plane"
    EDC_HOSTNAME: "my-data-plane"

vault:
  enabled: true
  url: "http://vault.my-domain.com"
  tokenSecretName: "vault-edc-token"
  tokenSecretKey: "token"
```

Then install:

```bash
helm install edc ./helm/edc -f custom-values.yaml
```

## Configuration

### Global Configuration

| Parameter | Description | Default |
|-----------|-------------|---------|
| `global.imageRegistry` | Global Docker image registry | `""` |
| `global.imagePullSecrets` | Global image pull secrets | `[]` |
| `global.nameOverride` | Global name override | `""` |
| `global.fullnameOverride` | Global fullname override | `""` |

### Namespace Configuration

| Parameter | Description | Default |
|-----------|-------------|---------|
| `namespace.create` | Create namespace | `true` |
| `namespace.name` | Namespace name | `"edc"` |

### Control Plane Configuration

| Parameter | Description | Default |
|-----------|-------------|---------|
| `controlPlane.enabled` | Enable control plane | `true` |
| `controlPlane.replicaCount` | Number of replicas | `1` |
| `controlPlane.image.repository` | Image repository | `"nuruldhamar/edc-controlplane"` |
| `controlPlane.image.tag` | Image tag | `"latest"` |
| `controlPlane.resources` | Resource limits/requests | See values.yaml |

### Data Plane Configuration

| Parameter | Description | Default |
|-----------|-------------|---------|
| `dataPlane.enabled` | Enable data plane | `true` |
| `dataPlane.replicaCount` | Number of replicas | `1` |
| `dataPlane.image.repository` | Image repository | `"nuruldhamar/edc-dataplane"` |
| `dataPlane.image.tag` | Image tag | `"latest"` |
| `dataPlane.resources` | Resource limits/requests | See values.yaml |

### Service Configuration

| Parameter | Description | Default |
|-----------|-------------|---------|
| `service.type` | Service type | `"ClusterIP"` |
| `service.annotations` | Service annotations | `{}` |

### Ingress Configuration

| Parameter | Description | Default |
|-----------|-------------|---------|
| `ingress.enabled` | Enable ingress | `false` |
| `ingress.className` | Ingress class name | `"nginx"` |
| `ingress.annotations` | Ingress annotations | `{}` |
| `ingress.hosts` | Ingress hosts | See values.yaml |
| `ingress.tls` | Ingress TLS configuration | `[]` |

### Vault Configuration

| Parameter | Description | Default |
|-----------|-------------|---------|
| `controlPlane.vault.enabled` | Enable Vault for control plane | `true` |
| `controlPlane.vault.url` | Vault URL | `"http://vault.tx.test"` |
| `controlPlane.vault.tokenSecretName` | Vault token secret name | `"vault-edc-token"` |
| `controlPlane.vault.tokenSecretKey` | Vault token secret key | `"token"` |
| `dataPlane.vault.enabled` | Enable Vault for data plane | `true` |
| `dataPlane.vault.url` | Vault URL | `"http://vault.tx.test"` |
| `dataPlane.vault.tokenSecretName` | Vault token secret name | `"vault-edc-token"` |
| `dataPlane.vault.tokenSecretKey` | Vault token secret key | `"token"` |

### Health Check Configuration

| Parameter | Description | Default |
|-----------|-------------|---------|
| `controlPlane.healthCheck.enabled` | Enable health checks | `true` |
| `controlPlane.healthCheck.initialDelaySeconds` | Initial delay | `40` |
| `controlPlane.healthCheck.periodSeconds` | Check period | `30` |
| `controlPlane.healthCheck.timeoutSeconds` | Timeout | `10` |
| `controlPlane.healthCheck.failureThreshold` | Failure threshold | `3` |

## Ports

### Control Plane Ports

| Port | Service | Description |
|------|---------|-------------|
| 8080 | Default | Default API |
| 8081 | Management | Management API |
| 8083 | Control | Control API |
| 8084 | Protocol | DSP Protocol API |
| 8085 | Catalog | Catalog API |
| 9090 | Metrics | Metrics endpoint |

### Data Plane Ports

| Port | Service | Description |
|------|---------|-------------|
| 8181 | Default | Default API |
| 8182 | Data | Data API |
| 8183 | Management | Management API |
| 8184 | Public | Public API |
| 8185 | Proxy | Proxy API |
| 8186 | Metrics | Metrics endpoint |

## Environment Variables

The chart supports all EDC environment variables. See the `values.yaml` file for the complete list of configurable environment variables for both control plane and data plane.

## Vault Secret

The chart automatically creates a Kubernetes secret named `vault-edc-token` containing the Vault authentication token. This secret is used by both control plane and data plane components to authenticate with Vault.

To use a custom Vault token, you can either:

1. **Update the secret after deployment:**
   ```bash
   kubectl patch secret vault-edc-token -n edc --type='json' -p='[{"op": "replace", "path": "/data/token", "value":"<base64-encoded-token>"}]'
   ```

2. **Create the secret manually before deployment:**
   ```bash
   kubectl create secret generic vault-edc-token -n edc --from-literal=token="your-vault-token"
   ```

3. **Override the secret name in values.yaml:**
   ```yaml
   controlPlane:
     vault:
       tokenSecretName: "my-custom-vault-secret"
       tokenSecretKey: "my-token-key"
   ```

## Upgrading

```bash
# Upgrade the release
helm upgrade edc ./helm/edc

# Upgrade with custom values
helm upgrade edc ./helm/edc -f custom-values.yaml
```

## Uninstalling

```bash
# Uninstall the release
helm uninstall edc

# Uninstall and delete namespace
helm uninstall edc
kubectl delete namespace edc
```

## Troubleshooting

### Check Pod Status

```bash
kubectl get pods -n edc
kubectl describe pod <pod-name> -n edc
```

### Check Logs

```bash
# Control plane logs
kubectl logs -f deployment/edc-control-plane -n edc

# Data plane logs
kubectl logs -f deployment/edc-data-plane -n edc
```

### Check Services

```bash
kubectl get svc -n edc
kubectl describe svc <service-name> -n edc
```

### Port Forward for Local Access

```bash
# Control plane
kubectl port-forward svc/edc-control-plane 8080:8080 -n edc

# Data plane
kubectl port-forward svc/edc-data-plane 8181:8181 -n edc
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test the chart
5. Submit a pull request

## License

This chart is licensed under the Apache License 2.0. 