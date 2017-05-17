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
package org.apache.sis.metadata.iso;

import javax.xml.bind.JAXBException;
import org.opengis.metadata.Identifier;
import org.apache.sis.internal.jaxb.LegacyNamespaces;
import org.apache.sis.metadata.iso.citation.DefaultCitation;
import org.apache.sis.util.CharSequences;
import org.apache.sis.xml.Namespaces;
import org.apache.sis.test.XMLTestCase;
import org.apache.sis.test.DependsOn;
import org.apache.sis.test.ISOTestUtils;
import org.junit.*;

import static org.apache.sis.test.MetadataAssert.*;


/**
 * Tests {@link DefaultIdentifier}.
 *
 * @author  Martin Desruisseaux
 * @author  Cullen Rombach	(Image Matters)
 * @since   0.4
 * @version 0.8
 * @module
 */
@DependsOn(org.apache.sis.metadata.iso.citation.DefaultCitationTest.class)
public final strictfp class DefaultIdentifierTest extends XMLTestCase {

	/**
	 * @return A string representing the XML encoding of an Identifier in ISO 19139 format.
	 */
	private static String getIdentifierXML() {
		return "<gmd:MD_Identifier xmlns:gmd=\"" + Namespaces.GMD + "\" " +
				"xmlns:gco=\"" + LegacyNamespaces.GCO + "\">\n" +
				"  <gmd:authority>\n" +
				"    <gmd:CI_Citation>\n" +
				"      <gmd:title>\n" +
				"        <gco:CharacterString>EPSG</gco:CharacterString>\n" +
				"      </gmd:title>\n" +
				"    </gmd:CI_Citation>\n" +
				"  </gmd:authority>\n" +
				"  <gmd:code>\n" +
				"    <gco:CharacterString>4326</gco:CharacterString>\n" +
				"  </gmd:code>\n" +
				"</gmd:MD_Identifier>";
	}

	/**
	 * Asserts that XML marshalling of the given object produce the {@link #XML} string (19139).
	 */
	void testMarshal19139(final String type, final Identifier identifier) throws JAXBException {
		assertXmlEquals(CharSequences.replace(getIdentifierXML(), "MD_Identifier", type).toString(), marshal(identifier), "xmlns:*");
	}
	
	/**
	 * Asserts that XML marshalling of the given object produce the {@link #XML} string (19115-3).
	 */
	void testMarshal191153(final String type, final Identifier identifier) throws JAXBException {
		assertXmlEquals(ISOTestUtils.from19139(CharSequences.replace(getIdentifierXML(), "MD_Identifier", type).toString()), marshal(identifier, Namespaces.ISO_19115_3), "xmlns:*");
	}

	/**
	 * Test XML marshalling for ISO 19139.
	 *
	 * @throws JAXBException Should never happen.
	 */
	@Test
	public void testMarshal19139() throws JAXBException {
		final DefaultIdentifier identifier = new DefaultIdentifier();
		identifier.setAuthority(new DefaultCitation("EPSG"));
		identifier.setCode("4326");
		testMarshal19139("MD_Identifier", identifier);
	}
	
	/**
	 * Test XML marshalling for ISO 19115-3.
	 *
	 * @throws JAXBException Should never happen.
	 */
	@Test
	public void testMarshal191153() throws JAXBException {
		final DefaultIdentifier identifier = new DefaultIdentifier();
		identifier.setAuthority(new DefaultCitation("EPSG"));
		identifier.setCode("4326");
		testMarshal191153("MD_Identifier", identifier);
	}

	/**
	 * Test XML unmarshalling for ISO 19139.
	 *
	 * @throws JAXBException Should never happen.
	 */
	@Test
	public void testUnmarshall19139() throws JAXBException {
		final DefaultIdentifier identifier = unmarshal(DefaultIdentifier.class, getIdentifierXML());
		assertNull       ("identifier",        identifier.getVersion());
		assertTitleEquals("authority", "EPSG", identifier.getAuthority());
		assertEquals     ("code",      "4326", identifier.getCode());
	}
	
	/**
	 * Test XML unmarshalling for ISO 19115-3.
	 *
	 * @throws JAXBException Should never happen.
	 */
	@Test
	public void testUnmarshall191153() throws JAXBException {
		final DefaultIdentifier identifier = unmarshal(DefaultIdentifier.class, ISOTestUtils.from19139(getIdentifierXML()), Namespaces.ISO_19115_3);
		assertNull       ("identifier",        identifier.getVersion());
		assertTitleEquals("authority", "EPSG", identifier.getAuthority());
		assertEquals     ("code",      "4326", identifier.getCode());
	}
}
