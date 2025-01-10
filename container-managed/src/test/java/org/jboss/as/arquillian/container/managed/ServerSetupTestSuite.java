/*
 * Copyright The WildFly Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.jboss.as.arquillian.container.managed;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * Ensures the order of the tests to execute in the correct order.
 *
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
@Suite
@SelectClasses({
        ServerSetupDeploymentTestCase.class,
        ServerSetupAssumptionViolationTestCase.class,
        ServerSetupUnmanagedAssumptionViolationTestCase.class,
        ServerSetupAfterClassTestCase.class
})
public class ServerSetupTestSuite {
    static final String SYSTEM_PROPERTY_KEY = "server.setup.key";
    static final String SYSTEM_PROPERTY_VALUE = "server.setup.value";
}
