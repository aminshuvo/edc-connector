plugins {
    `java-library`
    id("application")
    alias(libs.plugins.shadow)
}

dependencies {
    // Boot and Core Dependencies
    implementation(project(":core:common:boot"))
    implementation(project(":core:common:connector-core"))
    implementation(project(":core:common:runtime-core"))
    implementation(project(":core:common:token-core"))
    implementation(project(":core:data-plane:data-plane-core"))
    implementation(project(":core:data-plane:data-plane-util"))
    
    // Control Plane Core (needed for DSP extensions)
    implementation(project(":core:control-plane:control-plane-core"))
    implementation(project(":core:control-plane:control-plane-catalog"))
    implementation(project(":core:control-plane:control-plane-contract"))
    implementation(project(":core:control-plane:control-plane-transfer"))
    
    // Data Plane Specific Extensions
    implementation(project(":extensions:data-plane:data-plane-http"))
    implementation(project(":extensions:data-plane:data-plane-http-oauth2"))
    implementation(project(":extensions:data-plane:data-plane-http-oauth2-core"))
    implementation(project(":extensions:data-plane:data-plane-iam"))
    implementation(project(":extensions:data-plane:data-plane-integration-tests"))
    implementation(project(":extensions:data-plane:data-plane-kafka"))
    implementation(project(":extensions:data-plane:data-plane-public-api-v2"))
    // implementation(project(":extensions:data-plane:data-plane-self-registration"))
    implementation(project(":extensions:data-plane:data-plane-signaling:data-plane-signaling-api"))
    implementation(project(":extensions:data-plane:store"))
    
    // Common API dependencies
    implementation(project(":extensions:common:api:api-core"))
    implementation(project(":extensions:common:api:control-api-configuration"))
    implementation(project(":extensions:common:api:management-api-configuration"))
    
    // HTTP and Web Support
    implementation(project(":extensions:common:http"))
    
    // JSON-LD Support
    implementation(project(":extensions:common:json-ld"))
    
    // Configuration and Monitoring
    implementation(project(":extensions:common:configuration:configuration-filesystem"))
    implementation(project(":extensions:common:monitor:monitor-jdk-logger"))
    implementation(project(":extensions:common:metrics:micrometer-core"))
    
    // Authentication and IAM
    implementation(project(":extensions:common:auth"))
    implementation(project(":extensions:common:iam:iam-mock"))
    
    // Events and Transactions
    implementation(project(":extensions:common:events"))
    implementation(project(":extensions:common:transaction"))
    
    // Crypto and Vault
    implementation(project(":extensions:common:crypto"))
    implementation(project(":extensions:common:vault"))
    
    // Validator
    implementation(project(":extensions:common:validator"))
    
    // Control Plane API Client (for data plane to communicate with control plane)
    implementation(project(":extensions:control-plane:api:control-plane-api-client"))
    implementation(project(":extensions:control-plane:api"))
    implementation(project(":extensions:control-plane:store"))
    
    // DSP Protocol Support (all versions)
    implementation(project(":data-protocols:dsp:dsp-core"))
    implementation(project(":data-protocols:dsp:dsp-08"))
    implementation(project(":data-protocols:dsp:dsp-2024"))
    implementation(project(":data-protocols:dsp:dsp-2025"))
    implementation(project(":data-protocols:dsp:dsp-version"))
    implementation(project(":data-protocols:dsp:dsp-http-spi"))
    implementation(project(":data-protocols:dsp:dsp-spi"))
    implementation(project(":data-protocols:dsp:dsp-lib"))
    
    // Data Plane Selector (minimal dependencies for self-registration)
    implementation(project(":spi:data-plane-selector:data-plane-selector-spi"))
    implementation(project(":extensions:data-plane-selector:data-plane-selector-client"))
    
    // Store Extensions
    implementation(project(":extensions:common:store"))
    
    // SQL Support
    implementation(project(":extensions:common:sql"))
    
    // Available External Dependencies from Maven Central
    implementation("org.eclipse.edc:http-spi:0.13.0")
    implementation("org.eclipse.edc:web-spi:0.13.0")
    implementation("org.eclipse.edc:validator-spi:0.13.0")
    implementation("org.eclipse.edc:transform-spi:0.13.0")
}

application {
    mainClass.set("org.eclipse.edc.boot.system.runtime.BaseRuntime")
}

tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    mergeServiceFiles()
    archiveFileName.set("data-plane.jar")
}

edcBuild {
    publish.set(false)
} 