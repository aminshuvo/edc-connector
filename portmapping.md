# Port Mapping: Connector vs Tractus-X EDC

This document summarizes the port mapping for the Connector project, aligned with the Tractus-X EDC reference (from `charts/tractusx-connector/values.yaml`).

## Control Plane Ports
| Purpose         | Property Key                | Port |
|-----------------|----------------------------|------|
| Default API     | web.http.default.port      | 8080 |
| Management API  | web.http.management.port   | 8081 |
| Control API     | web.http.control.port      | 8083 |
| DSP API         | web.http.protocol.port     | 8084 |
| Catalog         | web.http.catalog.port      | 8085 |
| Metrics         | web.http.metrics.port      | 9090 |

## Data Plane Ports
| Purpose         | Property Key                | Port |
|-----------------|----------------------------|------|
| Default API     | web.http.default.port      | 8080 |
| Public API      | web.http.public.port       | 8081 |
| Control API     | web.http.control.port      | 8084 |
| Proxy           | web.http.proxy.port        | 8186 |
| Metrics         | web.http.metrics.port      | 9090 |

## DSP Protocol Configuration

Both control plane and data plane now have DSP (Dataspace Protocol) enabled with the following settings:

### DSP Properties
| Property Key | Value | Description |
|-------------|-------|-------------|
| edc.dsp.callback.address | http://localhost:8084/api/v1/dsp | DSP callback address |
| edc.dsp.context.enabled | true | Enable DSP context |
| edc.dsp.management.enabled | true | Enable DSP management |
| edc.dsp.well-known-path.enabled | false | Disable well-known path resolution |

### DSP Environment Variables
| Environment Variable | Value | Description |
|---------------------|-------|-------------|
| EDC_DSP_CALLBACK_ADDRESS | http://localhost:8084/api/v1/dsp | DSP callback address |
| EDC_DSP_CONTEXT_ENABLED | true | Enable DSP context |
| EDC_DSP_MANAGEMENT_ENABLED | true | Enable DSP management |
| EDC_DSP_WELL_KNOWN_PATH_ENABLED | false | Disable well-known path resolution |

These settings are now reflected in the sample property files for both control plane and data plane in this project. 