/*
 *  Copyright (c) 2024 Contributors to the Eclipse Foundation
 *
 *  This program and the accompanying materials are made available under the
 *  terms of the Apache License, Version 2.0 which is available at
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 *  SPDX-License-Identifier: Apache-2.0
 *
 *  Contributors:
 *       Contributors to the Eclipse Foundation - initial API and implementation
 *
 */

package org.eclipse.edc.boot;

/**
 * Data Plane Launcher for EDC Connector.
 */
public class Launcher {
    /**
     * Main method to start the data plane runtime.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        org.eclipse.edc.boot.system.runtime.BaseRuntime.main(args);
    }
} 