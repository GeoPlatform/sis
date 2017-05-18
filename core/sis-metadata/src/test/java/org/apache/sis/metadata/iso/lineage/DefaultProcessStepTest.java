/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.sis.metadata.iso.lineage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.xml.bind.JAXBException;

import org.apache.sis.test.ISOTestUtils;
import org.apache.sis.test.XMLTestCase;
import org.apache.sis.util.iso.SimpleInternationalString;
import org.apache.sis.xml.Namespaces;
import org.apache.sis.xml.XML;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * Tests {@link DefaultProcessStep}.
 *
 * @author  Cédric Briançon 	(Geomatys)
 * @author  Martin Desruisseaux (Geomatys)
 * @author  Cullen Rombach		(Image Matters)
 * @since   0.3
 * @version 0.8
 * @module
 */
public final strictfp class DefaultProcessStepTest extends XMLTestCase {
	
	private static DefaultProcessStep pStep;
	
	/**
	 * @return a DefaultProcessStep object that matches the contents of the XML test file.
	 */
	@BeforeClass
	public static void createDefaultProcessStep() {
		final DefaultProcessing  processing  = new DefaultProcessing();
        final DefaultProcessStep processStep = new DefaultProcessStep("Some process step.");
        processing.setProcedureDescription(new SimpleInternationalString("Some procedure."));
        processStep.setProcessingInformation(processing);
        pStep = processStep;
	}
	
    /**
     * An XML file in this package containing a process step definition.
     */
    private static final String XML_FILE = "ProcessStep.xml";

    /**
     * Tests the (un)marshalling of a metadata mixing elements from ISO 19115 and ISO 19115-2 standards.
     * ISO 19139.
     *
     * <p><b>XML test file:</b>
     * {@code "core/sis-metadata/src/test/resources/org/apache/sis/metadata/iso/lineage/ProcessStep.xml"}</p>
     *
     * @throws JAXBException If an error occurred during the during marshalling / unmarshalling processes.
     */
    @Test
    public void testXML19139() throws JAXBException {
        /*
         * XML marshalling, and compare with the content of "ProcessStep.xml" file.
         */
        assertMarshalEqualsFile(XML_FILE, pStep, "xlmns:*", "xsi:schemaLocation");
        /*
         * XML unmarshalling: ensure that we didn't lost any information.
         */
        assertEquals(pStep, unmarshalFile(DefaultProcessStep.class, XML_FILE));
    }
    
    /**
     * Tests the (un)marshalling of a metadata mixing elements from ISO 19115 and ISO 19115-2 standards.
     * ISO 19115-3.
     *
     * <p><b>XML test file:</b>
     * {@code "core/sis-metadata/src/test/resources/org/apache/sis/metadata/iso/lineage/ProcessStep.xml"}</p>
     *
     * @throws JAXBException If an error occurred during the during marshalling / unmarshalling processes.
     * @throws URISyntaxException 
     * @throws IOException 
     */
    @Test
    public void testXML191153() throws JAXBException, IOException, URISyntaxException {
    	// Need to extract the string from the accessed resource in order to convert it to ISO 19115-3.
        String contents = new String(Files.readAllBytes(Paths.get(getResource(XML_FILE).toURI())));
        
        // Since from19139 both marshals and unmarshals, this is testing a full round trip.
        String contents191153 = ISOTestUtils.from19139(contents);
        
        // Convert the ISO 19115-3 XML to a Java object.
        DefaultProcessStep pStep191153 = (DefaultProcessStep) XML.unmarshal(contents191153, Namespaces.ISO_19115_3);
        
        // Compare the unmarshaled Java object to the expected one.
        assertEquals("19115-3 unmarshalled object does not match expected object.", pStep191153, pStep);
    }
    
    /**
     * Returns the URL to the XML file of the given name.
     * The argument shall be one of the files listed in the following directory:
     *
     * <ul>
     *   <li>{@code "core/sis-metadata/src/test/resources/org/apache/sis/metadata/iso/lineage"}</li>
     * </ul>
     *
     * @param  filename The name of the XML file.
     * @return The URL to the given XML file.
     */
    public static URL getResource(final String filename) {
        final URL resource = DefaultProcessStepTest.class.getResource(filename);
        assertNotNull(filename, resource);
        return resource;
    }
}
