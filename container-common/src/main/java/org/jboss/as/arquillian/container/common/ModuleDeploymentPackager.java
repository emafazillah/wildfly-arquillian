/*
 * JBoss, Home of Professional Open Source
 * Copyright 2009, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.as.arquillian.container.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.jboss.arquillian.spi.Context;
import org.jboss.arquillian.spi.DeploymentPackager;
import org.jboss.arquillian.spi.TestDeployment;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.asset.Asset;
import org.jboss.shrinkwrap.api.container.ManifestContainer;

/**
 * JBossASDeploymentPackager
 *
 * @author Thomas.Diesler@jboss.com
 * @author Kabir Khan
 * @since 17-Nov-2010
 */
public class ModuleDeploymentPackager implements DeploymentPackager {

    public Archive<?> generateDeployment(Context context, TestDeployment testDeployment) {

        Archive<?> appArchive = testDeployment.getApplicationArchive();
        addDefaultDependencies(appArchive);
        return appArchive;
    }

    //These are now added in JBossAsArquillianDependencyProcessor and JBossAsArchiveProcessor
    private void addDefaultDependencies(Archive<?> appArchive) {

        if (appArchive instanceof ManifestContainer<?> == false)
            throw new IllegalArgumentException("ManifestContainer expected " + appArchive);

        final Manifest manifest = DelegatingDeploymentPackager.getOrCreateManifest(appArchive);
        Attributes attributes = manifest.getMainAttributes();
        String value = attributes.getValue("Dependencies");
        StringBuffer moduleDeps = new StringBuffer(value != null && value.trim().length() > 0 ? value + "," : "");
        moduleDeps.append("org.jboss.arquillian.api");
        moduleDeps.append(",org.jboss.arquillian.junit");
        moduleDeps.append(",org.jboss.arquillian.spi");
        moduleDeps.append(",org.jboss.shrinkwrap.api");
        moduleDeps.append(",junit.junit");
        attributes.putValue("Dependencies", moduleDeps.toString());

        // Add the manifest to the archive
        ((ManifestContainer<?>)appArchive).setManifest(new Asset() {
            public InputStream openStream() {
                try {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    manifest.write(baos);
                    return new ByteArrayInputStream(baos.toByteArray());
                } catch (IOException ex) {
                    throw new IllegalStateException("Cannot write manifest", ex);
                }
            }
        });
    }
}