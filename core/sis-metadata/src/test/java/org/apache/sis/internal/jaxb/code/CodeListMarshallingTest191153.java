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
package org.apache.sis.internal.jaxb.code;

import static org.apache.sis.test.Assert.assertXmlEquals;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Locale;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.sis.internal.jaxb.Schemas;
import org.apache.sis.metadata.iso.citation.DefaultCitation;
import org.apache.sis.test.ISOTestUtils;
import org.apache.sis.test.XMLTestCase;
import org.apache.sis.xml.MarshallerPool;
import org.apache.sis.xml.Namespaces;
import org.apache.sis.xml.XML;
import org.junit.Test;
import org.opengis.metadata.citation.CitationDate;
import org.opengis.metadata.citation.DateType;
import org.opengis.metadata.citation.PresentationForm;
import org.opengis.metadata.citation.Responsibility;
import org.opengis.metadata.citation.Role;


/**
 * Tests the XML marshaling of {@code CodeList} in ISO 19115-3.
 *
 * @author  Cullen Rombach 		(Image Matters)
 * @since   0.8
 * @version 0.8
 * @module
 */
public final strictfp class CodeListMarshallingTest191153 extends XMLTestCase {

	/**
	 * Returns a XML string to use for testing purpose.
	 *
	 * @param baseURL The base URL of XML schemas.
	 */
	private String getResponsiblePartyXML(final String baseURL, final String codelistPath) {
		return ISOTestUtils.from19139(CodeListMarshallingTest19139.getResponsiblePartyXML(baseURL, codelistPath));
	}

	/**
	 * Returns a XML string to use for testing purpose.
	 *
	 * @param baseURL The base URL of XML schemas.
	 */
	private String getCitationXML(final String baseURL, final String codelistPath, final String language, final String value) {
		return ISOTestUtils.from19139(CodeListMarshallingTest19139.getCitationXML(baseURL, codelistPath, language, value));
	}

	/**
	 * Tests marshaling using the default URL.
	 *
	 * @throws JAXBException If an error occurred while marshaling the XML.
	 */
	@Test
	public void testDefaultURL() throws JAXBException {
		final String expected = getResponsiblePartyXML(Schemas.METADATA_ROOT_NEW, Schemas.CODELISTS_PATH_NEW);
		final Responsibility rp = (Responsibility) XML.unmarshal(expected, Namespaces.ISO_19115_3);
		assertEquals(Role.PRINCIPAL_INVESTIGATOR, rp.getRole());
		/*
		 * Use the convenience method in order to avoid the effort of creating
		 * our own MarshallerPool.
		 */
		final String actual = XML.marshal(rp, Namespaces.ISO_19115_3);
		assertXmlEquals(expected, actual, "xmlns:*");
	}

	/**
	 * Tests a code list localization.
	 *
	 * @throws JAXBException If an error occurred while marshaling the XML.
	 */
	@Test
	public void testLocalization() throws JAXBException {
		final MarshallerPool pool = getMarshallerPool();
		final Marshaller marshaller = pool.acquireMarshaller();
		/*
		 * First, test using the French locale.
		 */
		marshaller.setProperty(XML.LOCALE, Locale.FRENCH);
		String expected19139 = CodeListMarshallingTest19139.getCitationXML(Schemas.METADATA_ROOT_NEW, Schemas.CODELISTS_PATH_NEW, "fra", "Création");
		String expected = ISOTestUtils.from19139(expected19139, marshaller);
		CitationDate ci = (CitationDate) XML.unmarshal(expected, Namespaces.ISO_19115_3);
		assertEquals(DateType.CREATION, ci.getDateType());
		String actual = marshal(marshaller, ci, Namespaces.ISO_19115_3);
		assertXmlEquals(expected, actual, "xmlns:*");
		/*
		 * Tests again using the English locale.
		 */
		marshaller.setProperty(XML.LOCALE, Locale.ENGLISH);
		expected = getCitationXML(Schemas.METADATA_ROOT_NEW, Schemas.CODELISTS_PATH_NEW, "eng", "Creation");
		ci = (CitationDate) XML.unmarshal(expected, Namespaces.ISO_19115_3);
		assertEquals(DateType.CREATION, ci.getDateType());
		actual = marshal(marshaller, ci, Namespaces.ISO_19115_3);
		assertXmlEquals(expected, actual, "xmlns:*");

		pool.recycle(marshaller);
	}

	/**
	 * Tests marshaling of a code list which is not in the list of standard codes.
	 *
	 * @throws JAXBException If an error occurred while marshaling the XML.
	 */
	@Test
	public void testExtraCodes() throws JAXBException {
		final DefaultCitation id = new DefaultCitation();
		id.setPresentationForms(Arrays.asList(
				PresentationForm.valueOf("IMAGE_DIGITAL"), // Existing code with UML id="imageDigital"
				PresentationForm.valueOf("test")));        // New code

		final String xml = marshal(id, Namespaces.ISO_19115_3);

		// "IMAGE_DIGITAL" is marshalled as "imageDigital" because is contains a UML id, which is lower-case.
		String oldXML = 
				"<gmd:CI_Citation xmlns:gmd=\"" + Namespaces.GMD + "\">\n" +
						"  <gmd:presentationForm>\n" +
						"    <gmd:CI_PresentationFormCode codeListValue=\"imageDigital\">Image digital</gmd:CI_PresentationFormCode>\n" +
						"  </gmd:presentationForm>\n" +
						"  <gmd:presentationForm>\n" +
						"    <gmd:CI_PresentationFormCode codeListValue=\"test\">Test</gmd:CI_PresentationFormCode>\n" +
						"  </gmd:presentationForm>\n" +
						"</gmd:CI_Citation>\n";
		assertXmlEquals(ISOTestUtils.from19139(oldXML), xml, "xmlns:*", "codeList", "codeSpace");
	}
}
