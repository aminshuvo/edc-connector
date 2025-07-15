plugins {
    `java-library`
    id("application")
    alias(libs.plugins.shadow)
}

dependencies {
    // Core Control Plane BOM (includes most control plane extensions)
    implementation(project(":dist:bom:controlplane-base-bom"))
    
    // Boot and Core Dependencies
    implementation(project(":core:common:boot"))
    implementation(project(":core:common:connector-core"))
    implementation(project(":core:control-plane:control-plane-core"))
    implementation(project(":core:control-plane:control-plane-catalog"))
    implementation(project(":core:control-plane:control-plane-contract"))
    implementation(project(":core:control-plane:control-plane-contract-manager"))
    implementation(project(":core:control-plane:control-plane-transfer"))
    implementation(project(":core:control-plane:control-plane-transfer-manager"))
    implementation(project(":core:control-plane:control-plane-transform"))
    implementation(project(":core:control-plane:control-plane-aggregate-services"))
    
    // API Configuration Extensions
    implementation(project(":extensions:common:api:management-api-configuration"))
    implementation(project(":extensions:common:api:control-api-configuration"))
    implementation(project(":extensions:common:api:api-core"))
    
    // Authentication and IAM
    implementation(project(":extensions:common:iam:iam-mock"))
    implementation(project(":extensions:common:auth"))
    
    // HTTP and Web Support
    implementation(project(":extensions:common:http"))
    
    // JSON-LD Support
    implementation(project(":extensions:common:json-ld"))
    
    // Configuration and Monitoring
    implementation(project(":extensions:common:configuration:configuration-filesystem"))
    implementation(project(":extensions:common:monitor:monitor-jdk-logger"))
    implementation(project(":extensions:common:metrics:micrometer-core"))
    
    // Events and Transactions
    implementation(project(":extensions:common:events"))
    implementation(project(":extensions:common:transaction"))
    
    // Crypto and Vault
    implementation(project(":extensions:common:crypto"))
    implementation(project(":extensions:common:vault"))
    
    // Validator
    implementation(project(":extensions:common:validator"))
    
    // Control Plane Specific Extensions
    implementation(project(":extensions:control-plane:api"))
    implementation(project(":extensions:control-plane:callback"))
    implementation(project(":extensions:control-plane:edr"))
    implementation(project(":extensions:control-plane:provision"))
    implementation(project(":extensions:control-plane:store"))
    implementation(project(":extensions:control-plane:transfer"))
    
    // DSP Protocol Support (all versions)
    implementation(project(":data-protocols:dsp:dsp-core"))
    implementation(project(":data-protocols:dsp:dsp-08"))
    implementation(project(":data-protocols:dsp:dsp-2024"))
    implementation(project(":data-protocols:dsp:dsp-2025"))
    implementation(project(":data-protocols:dsp:dsp-version"))
    implementation(project(":data-protocols:dsp:dsp-http-spi"))
    implementation(project(":data-protocols:dsp:dsp-spi"))
    implementation(project(":data-protocols:dsp:dsp-lib"))
    
    // Policy Monitor
    implementation(project(":core:policy-monitor:policy-monitor-core"))
    implementation(project(":extensions:policy-monitor:store"))
    
    // Data Plane Selector
    implementation(project(":core:data-plane-selector:data-plane-selector-core"))
    implementation(project(":extensions:data-plane-selector:data-plane-selector-api"))
    implementation(project(":extensions:data-plane-selector:data-plane-selector-client"))
    implementation(project(":extensions:data-plane-selector:data-plane-selector-control-api"))
    implementation(project(":extensions:data-plane-selector:store"))
    
    // Store Extensions
    implementation(project(":extensions:common:store"))
    
    // SQL Support
    implementation(project(":extensions:common:sql"))
}

application {
    mainClass.set("org.eclipse.edc.boot.system.runtime.BaseRuntime")
}

tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    mergeServiceFiles()
    archiveFileName.set("control-plane.jar")
}

edcBuild {
    publish.set(false)
} 