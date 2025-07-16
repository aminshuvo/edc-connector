# Eclipse Dataspace Connector (EDC) - Connector Project

This project provides a modular implementation of the Eclipse Dataspace Connector (EDC) with Control Plane and Data Plane components. It supports running locally with Docker Compose or deploying to Kubernetes with Helm.

---

## Prerequisites

- Java 17+
- Docker & Docker Compose
- (For Kubernetes) Helm 3.0+ and kubectl
- HashiCorp Vault instance (for secrets management)

---

## Building the Project

This project uses Gradle for builds. To build all modules:

```bash
gradlew clean build
```

This will compile all modules and run the tests.

---

## Building Docker Images

To build the Docker images for the Control Plane and Data Plane:

```bash
# Control Plane
cd launchers/control-plane
./build.sh

# Data Plane
cd ../data-plane
./build.sh
```

This will produce Docker images (by default tagged as `latest`).

---

## Running with Docker Compose

A sample `docker-compose.yml` is provided to run both Control Plane and Data Plane locally:

```bash
docker-compose up -d
```

This will start both services and expose the following ports:

- Control Plane: 8080 (API), 8081 (Management), 8083 (Control), 8084 (DSP), 8085 (Catalog), 9090 (Metrics)
- Data Plane: 9080-9085 (mapped to 8181-8186 in the container)

**Vault Requirement:**
- The services expect a running Vault instance (see below).
- The Vault token must be provided via the environment variable `EDC_VAULT_HASHICORP_TOKEN` (see `docker-compose.yml`).

---

## Running with Helm (Kubernetes)

A Helm chart is provided in `helm/edc/` for deploying to Kubernetes.

### Basic Installation

```bash
helm install edc ./helm/edc
```

### Custom Values

You can override configuration by creating a `custom-values.yaml` file (see `helm/edc/custom-values.yaml` for an example):

```bash
helm install edc ./helm/edc -f custom-values.yaml
```

### Vault Integration

- The chart expects a Kubernetes secret named `vault-edc-token` containing your Vault token.
- To create it:

```bash
kubectl create secret generic vault-edc-token -n edc --from-literal=token="<your-vault-token>"
```

- You can override the secret name/key in your values file:

```yaml
controlPlane:
  vault:
    tokenSecretName: "my-custom-vault-secret"
    tokenSecretKey: "my-token-key"
```

---

## Vault Requirement

Both Control Plane and Data Plane require access to a HashiCorp Vault instance for secret management. The Vault token must be provided as:

- Environment variable: `EDC_VAULT_HASHICORP_TOKEN` (for Docker Compose)
- Kubernetes secret: `vault-edc-token` (for Helm)

**The token must have sufficient permissions to read/write secrets in the configured Vault path.**

---

## Useful Endpoints

- Control Plane:
  - HTTP API: http://localhost:8080
  - Management API: http://localhost:8081/api/v1/data
  - Control API: http://localhost:8083/api/v1/control
  - DSP Protocol: http://localhost:8084/api/v1/dsp
  - Catalog API: http://localhost:8085/api/v1/catalog
  - Metrics: http://localhost:9090/metrics
- Data Plane:
  - HTTP API: http://localhost:9080
  - Public API: http://localhost:9083/api/public
  - Proxy API: http://localhost:9084/api/v1/data
  - Metrics: http://localhost:9085/metrics

---

## Troubleshooting

- Check logs: `docker-compose logs` or `kubectl logs ...`
- Check Vault connectivity and token permissions if secrets are not accessible.

---

## License

This project is licensed under the Apache License 2.0. 