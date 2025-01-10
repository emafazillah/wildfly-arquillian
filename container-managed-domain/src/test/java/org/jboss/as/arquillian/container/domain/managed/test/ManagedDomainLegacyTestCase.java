/*
 * Copyright The WildFly Authors
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.as.arquillian.container.domain.managed.test;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * For Domain server DeployableContainer implementations, the DeployableContainer will register
 * all groups/individual servers it controls as Containers in Arquillian's Registry during start.
 *
 * @author <a href="mailto:aslak@redhat.com">Aslak Knutsen</a>
 */
@ExtendWith(ArquillianExtension.class)
public class ManagedDomainLegacyTestCase {

    @Deployment(name = "dep1")
    @TargetsContainer("main-server-group")
    public static WebArchive create1() {
        return ShrinkWrap.create(WebArchive.class);
    }

    @Test
    @OperateOnDeployment("dep1")
    @TargetsContainer("master:server-one")
    public void shouldRunInContainer1() throws Exception {
        // Get the logger path which should contain the name of the server
        final String logDir = System.getProperty("jboss.server.log.dir");
        Assertions.assertNotNull(logDir, "Could not determine the jboss.server.log.dir property");
        Assertions.assertTrue(logDir.contains("server-one"), "Log dir should contain server-one: " + logDir);
    }

    @Test
    @TargetsContainer("master:server-two")
    public void shouldRunInContainer2() throws Exception {
        // Get the logger path which should contain the name of the server
        final String logDir = System.getProperty("jboss.server.log.dir");
        Assertions.assertNotNull(logDir, "Could not determine the jboss.server.log.dir property");
        Assertions.assertTrue(logDir.contains("server-two"), "Log dir should contain server-two: " + logDir);
    }
}
