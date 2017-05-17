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
import java.util.Collections;
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
 * Tests the XML marshaling of {@code CodeList}.
 *
 * @author  Martin Desruisseaux (Geomatys)
 * @author  Guilhem Legal 		(Geomatys)
 * @author	Cullen Rombach 		(Image Matters)
 * @since   0.3
 * @version 0.8
 * @module
 *
 * @see <a href="http://jira.geotoolkit.org/browse/GEOTK-121">GEOTK-121</a>
 */
public strictfp class CodeListMarshallingTest extends XMLTestCase {
    /**
     * Returns a XML string in 19139 to use for testing purpose.
     *
     * @param baseURL The base URL of XML schemas.
     */
    private static String getResponsiblePartyXML(final String baseURL, final String codelistsPath) {
        return "<gmd:CI_ResponsibleParty xmlns:gmd=\"" + Namespaces.GMD + "\">\n" +
               "  <gmd:role>\n" +
               "    <gmd:CI_RoleCode codeList=\"" + baseURL + codelistsPath + "#CI_RoleCode\"" +
                    " codeListValue=\"principalInvestigator\">" + "Principal investigator</gmd:CI_RoleCode>\n" +
               "  </gmd:role>\n" +
               "</gmd:CI_ResponsibleParty>";
    }

    /**
     * Returns a XML string in 19139 to use for testing purpose.
     *
     * @param baseURL The base URL of XML schemas.
     */
    private static String getCitationXML(final String baseURL, final String codelistsPath, final String language, final String value) {
        return "<gmd:CI_Date xmlns:gmd=\"" + Namespaces.GMD + "\">\n" +
               "  <gmd:dateType>\n" +
               "    <gmd:CI_DateTypeCode codeList=\"" + baseURL + codelistsPath + "#CI_DateTypeCode\"" +
                    " codeListValue=\"creation\" codeSpace=\"" + language + "\">" + value + "</gmd:CI_DateTypeCode>\n" +
               "  </gmd:dateType>\n" +
               "</gmd:CI_Date>";
    }
    
    /**
	 * Returns a XML string in 19115-3 to use for testing purpose.
	 *
	 * @param baseURL The base URL of XML schemas.
	 */
	private String getResponsiblePartyXML191153(final String baseURL, final String codelistPath) {
		return ISOTestUtils.from19139(CodeListMarshallingTest.getResponsiblePartyXML(baseURL, codelistPath));
	}

	/**
	 * Returns a XML string in 19115-3 to use for testing purpose.
	 *
	 * @param baseURL The base URL of XML schemas.
	 */
	private String getCitationXML191153(final String baseURL, final String codelistPath, final String language, final String value) {
		return ISOTestUtils.from19139(CodeListMarshallingTest.getCitationXML(baseURL, codelistPath, language, value));
	}


    /**
     * Tests marshaling using the default URL.
     *
     * @throws JAXBException If an error occurred while marshaling the XML.
     */
    @Test
    public void testDefaultURL19139() throws JAXBException {
        final String expected = getResponsiblePartyXML(Schemas.METADATA_ROOT_19139, Schemas.CODELISTS_PATH_19139);
        final Responsibility rp = (Responsibility) XML.unmarshal(expected);
        assertEquals(Role.PRINCIPAL_INVESTIGATOR, rp.getRole());
        /*
         * Use the convenience method in order to avoid the effort of creating
         * our own MarshallerPool.
         */
        final String actual = XML.marshal(rp);
        assertXmlEquals(expected, actual, "xmlns:*");
    }
    
    /**
	 * Tests marshaling using the default URL.
	 *
	 * @throws JAXBException If an error occurred while marshaling the XML.
	 */
	@Test
	public void testDefaultURL191153() throws JAXBException {
		final String expected = getResponsiblePartyXML191153(Schemas.METADATA_ROOT_191153, Schemas.CODELISTS_PATH_191153);
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
     * Tests marshaling using the ISO URL.
     *
     * @throws JAXBException If an error occurred while marshaling the XML.
     */
    @Test
    public void testISO_URL19139() throws JAXBException {
        final String expected = getResponsiblePartyXML(Schemas.ISO_19139_ROOT, Schemas.CODELISTS_PATH_19139);
        final Responsibility rp = (Responsibility) XML.unmarshal(expected);
        assertEquals(Role.PRINCIPAL_INVESTIGATOR, rp.getRole());

        final MarshallerPool pool = getMarshallerPool();
        final Marshaller marshaller = pool.acquireMarshaller();
        marshaller.setProperty(XML.SCHEMAS, Collections.singletonMap("gmd",
                "http://standards.iso.org/ittf/PubliclyAvailableStandards/ISO_19139_Schemas")); // Intentionally omit trailing '/'.
        final String actual = marshal(marshaller, rp);
        pool.recycle(marshaller);
        assertXmlEquals(expected, actual, "xmlns:*");
    }

    /**
     * Tests a code list localization.
     *
     * @throws JAXBException If an error occurred while marshaling the XML.
     */
    @Test
    public void testLocalization19139() throws JAXBException {
        final MarshallerPool pool = getMarshallerPool();
        final Marshaller marshaller = pool.acquireMarshaller();
        /*
         * First, test using the French locale.
         */
        marshaller.setProperty(XML.LOCALE, Locale.FRENCH);
        String expected = getCitationXML(Schemas.METADATA_ROOT_19139, Schemas.CODELISTS_PATH_19139, "fra", "Création");
        CitationDate ci = (CitationDate) XML.unmarshal(expected);
        assertEquals(DateType.CREATION, ci.getDateType());
        String actual = marshal(marshaller, ci);
        assertXmlEquals(expected, actual, "xmlns:*");
        /*
         * Tests again using the English locale.
         */
        marshaller.setProperty(XML.LOCALE, Locale.ENGLISH);
        expected = getCitationXML(Schemas.METADATA_ROOT_19139, Schemas.CODELISTS_PATH_19139, "eng", "Creation");
        ci = (CitationDate) XML.unmarshal(expected);
        assertEquals(DateType.CREATION, ci.getDateType());
        actual = marshal(marshaller, ci);
        assertXmlEquals(expected, actual, "xmlns:*");

        pool.recycle(marshaller);
    }
    
    /**
	 * Tests a code list localization.
	 *
	 * @throws JAXBException If an error occurred while marshaling the XML.
	 */
	@Test
	public void testLocalization191153() throws JAXBException {
		final MarshallerPool pool = getMarshallerPool();
		final Marshaller marshaller = pool.acquireMarshaller();
		/*
		 * First, test using the French locale.
		 */
		marshaller.setProperty(XML.LOCALE, Locale.FRENCH);
		String expected19139 = CodeListMarshallingTest.getCitationXML(Schemas.METADATA_ROOT_191153, Schemas.CODELISTS_PATH_191153, "fra", "Création");
		String expected = ISOTestUtils.from19139(expected19139, marshaller);
		CitationDate ci = (CitationDate) XML.unmarshal(expected, Namespaces.ISO_19115_3);
		assertEquals(DateType.CREATION, ci.getDateType());
		String actual = marshal(marshaller, ci, Namespaces.ISO_19115_3);
		assertXmlEquals(expected, actual, "xmlns:*");
		/*
		 * Tests again using the English locale.
		 */
		marshaller.setProperty(XML.LOCALE, Locale.ENGLISH);
		expected = getCitationXML191153(Schemas.METADATA_ROOT_191153, Schemas.CODELISTS_PATH_191153, "eng", "Creation");
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
    public void testExtraCodes19139() throws JAXBException {
        final DefaultCitation id = new DefaultCitation();
        id.setPresentationForms(Arrays.asList(
                PresentationForm.valueOf("IMAGE_DIGITAL"), // Existing code with UML id="imageDigital"
                PresentationForm.valueOf("test")));        // New code

        final String xml = marshal(id);

        // "IMAGE_DIGITAL" is marshalled as "imageDigital" because is contains a UML id, which is lower-case.
        assertXmlEquals(
                "<gmd:CI_Citation xmlns:gmd=\"" + Namespaces.GMD + "\">\n" +
                "  <gmd:presentationForm>\n" +
                "    <gmd:CI_PresentationFormCode codeListValue=\"imageDigital\">Image digital</gmd:CI_PresentationFormCode>\n" +
                "  </gmd:presentationForm>\n" +
                "  <gmd:presentationForm>\n" +
                "    <gmd:CI_PresentationFormCode codeListValue=\"test\">Test</gmd:CI_PresentationFormCode>\n" +
                "  </gmd:presentationForm>\n" +
                "</gmd:CI_Citation>\n",
                xml, "xmlns:*", "codeList", "codeSpace");
    }
    
    /**
	 * Tests marshaling of a code list which is not in the list of standard codes.
	 *
	 * @throws JAXBException If an error occurred while marshaling the XML.
	 */
	@Test
	public void testExtraCodes191153() throws JAXBException {
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
