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
package org.apache.sis.metadata.iso.identification;

import static java.util.Collections.singleton;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.xml.bind.JAXBException;

import org.apache.sis.internal.system.DefaultFactories;
import org.apache.sis.test.DependsOn;
import org.apache.sis.test.XMLTestCase;
import org.apache.sis.util.ComparisonMode;
import org.apache.sis.xml.Namespaces;
import org.apache.sis.xml.NilReason;
import org.apache.sis.xml.XML;
import org.junit.Test;
import org.opengis.metadata.citation.Citation;
import org.opengis.metadata.identification.CouplingType;
import org.opengis.util.NameFactory;


/**
 * Tests {@link DefaultServiceIdentification}.
 *
 * @author  Martin Desruisseaux (Geomatys)
 * @author  Cullen Rombach		(Image Matters)
 * @since   0.5
 * @version 0.8
 * @module
 */
@DependsOn({
	DefaultCoupledResourceTest.class,
	org.apache.sis.metadata.iso.identification.DefaultDataIdentificationTest.class
})
public final strictfp class DefaultServiceIdentificationTest extends XMLTestCase {
	/**
	 * An XML file in this package containing a service identification.
	 */
	private static final String XML_FILE = "ServiceIdentification.xml";

	/**
	 * Creates the service identification to use for testing purpose.
	 */
	private static DefaultServiceIdentification create() {
		final NameFactory factory = DefaultFactories.forBuildin(NameFactory.class);
		final DefaultCoupledResource resource = DefaultCoupledResourceTest.create();
		final DefaultServiceIdentification id = new DefaultServiceIdentification(
				factory.createGenericName(null, "Web Map Server"),      // serviceType
				NilReason.MISSING.createNilObject(Citation.class),      // citation
				"A dummy service for testing purpose.");                // abstract
		id.setServiceTypeVersions(singleton("1.0"));
		id.setCoupledResources(singleton(resource));
		id.setCouplingType(CouplingType.LOOSE);
		id.setContainsOperations(singleton(resource.getOperation()));
		return id;
	}

	/**
	 * Tests the marshalling of a service metadata for ISO 19139.
	 *
	 * @throws JAXBException If an error occurred during the during marshalling process.
	 */
	@Test
	public void testMarshal19139() throws JAXBException {
		assertMarshalEqualsFile(XML_FILE, create(), "xlmns:*", "xsi:schemaLocation");
	}

	/**
	 * Tests the (un)marshalling of a service metadata for ISO 19115-3.
	 * This is only a reliable test if testMarshal19139() works properly.
	 *
	 * @throws JAXBException If an error occurred during the during marshalling process.
	 */
	@Test
	public void test191153() throws JAXBException {
		// Unmarshal from ISO 19139 to Java object.
		DefaultServiceIdentification dsi19139 = unmarshalFile(DefaultServiceIdentification.class, XML_FILE);
		// Marshal back to ISO 19115-3.
		String xmlFile191153 = XML.marshal(dsi19139, Namespaces.ISO_19115_3);
		// Unmarshal from ISO 19115-3 to Java object.
		DefaultServiceIdentification dsi191153 = (DefaultServiceIdentification) XML.unmarshal(xmlFile191153, Namespaces.ISO_19115_3);
		// Check that the two objects are the same.
		assertEquals(dsi19139, dsi191153);
	}

	/**
	 * Tests the unmarshalling of a service metadata.
	 *
	 * <p><b>XML test file:</b>
	 * {@code "core/sis-metadata/src/test/resources/org/apache/sis/metadata/iso/service/ServiceIdentification.xml"}</p>
	 *
	 * @throws JAXBException If an error occurred during the during unmarshalling process.
	 */
	@Test
	public void testUnmarshal19139() throws JAXBException {
		DefaultServiceIdentification dsi = unmarshalFile(DefaultServiceIdentification.class, XML_FILE);
		assertTrue(create().equals(dsi, ComparisonMode.DEBUG));
		/* There are some problems with the following original assert statement that have to do with
		 * DefaultLocalName throwing NullPointerExceptions:
		 * 
		 * assertTrue(create().equals(dsi, ComparisonMode.DEBUG));
		 * 
		 * As a replacement, This test now compares XML that has been marshalled from the dsi object
		 * to XML that is directly from the file.
		 */
		//String xmlMarshalled = XML.marshal(dsi);
		//assertXmlEquals(DefaultExtentTest.getResource(XML_FILE), xmlMarshalled, "xmlns:*", "xsi:schemaLocation");
	}
}
