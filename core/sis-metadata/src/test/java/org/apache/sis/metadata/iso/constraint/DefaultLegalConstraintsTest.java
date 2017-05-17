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
package org.apache.sis.metadata.iso.constraint;

import static java.util.Collections.singleton;
import static org.apache.sis.test.Assert.assertXmlEquals;
import static org.apache.sis.test.TestUtilities.getSingleton;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.logging.LogRecord;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.sis.test.ISOTestUtils;
import org.apache.sis.test.XMLTestCase;
import org.apache.sis.util.Version;
import org.apache.sis.util.logging.WarningListener;
import org.apache.sis.xml.MarshallerPool;
import org.apache.sis.xml.Namespaces;
import org.apache.sis.xml.XML;
import org.junit.Test;
import org.opengis.metadata.constraint.Restriction;


/**
 * Tests {@link DefaultLegalConstraints}.
 *
 * @author  Martin Desruisseaux (Geomatys)
 * @author  Cullen Rombach		(Image Matters)
 * @since   0.4
 * @version 0.8
 * @module
 */
public final strictfp class DefaultLegalConstraintsTest extends XMLTestCase implements WarningListener<Object> {
	/**
	 * The resource key for the message of the warning that occurred while unmarshalling a XML fragment,
	 * or {@code null} if none.
	 */
	private Object resourceKey;

	/**
	 * The parameter of the warning that occurred while unmarshalling a XML fragment, or {@code null} if none.
	 */
	private Object[] parameters;

	private String emptyCodeListValueXML = "<gmd:MD_LegalConstraints xmlns:gmd=\"" + Namespaces.GMD + "\">\n" +
			"  <gmd:accessConstraints>\n" +
			"    <gmd:MD_RestrictionCode codeListValue=\"intellectualPropertyRights\" codeList=\"http://www.isotc211.org/2005/resources/codeList.xml#MD_RestrictionCode\"/>\n" +
			"  </gmd:accessConstraints>\n" +
			"  <gmd:useConstraints>\n" + // Below is an intentionally empty code list value (SIS-157)
			"    <gmd:MD_RestrictionCode codeListValue=\"\" codeList=\"http://www.isotc211.org/2005/resources/codeList.xml#MD_RestrictionCode\"/>\n" +
			"  </gmd:useConstraints>\n" +
			"</gmd:MD_LegalConstraints>";
	
	private String licenceCodeXML = "<gmd:MD_LegalConstraints xmlns:gmd=\"" + Namespaces.GMD + "\">\n" +
			"  <gmd:useConstraints>\n" +
			"    <gmd:MD_RestrictionCode"
			+ " codeList=\"http://schemas.opengis.net/iso/19139/20070417/resources/Codelist/gmxCodelists.xml#MD_RestrictionCode\""
			+ " codeListValue=\"license\""                              // Note the "s" - from old ISO 19115:2013 spelling.
			+ " codeSpace=\"eng\">Licence</gmd:MD_RestrictionCode>\n" + // Note the "c" - this one come from resource file.
			"  </gmd:useConstraints>\n" +
			"</gmd:MD_LegalConstraints>\n";

    /**
     * For internal {@code DefaultLegalConstraints} usage.
     *
     * @return {@code Object.class}.
     */
     @Override
     public Class<Object> getSourceClass() {
		return Object.class;
	}

	/**
	 * Invoked when a warning occurred while unmarshalling a test XML fragment. This method ensures that no other
	 * warning occurred before this method call (i.e. each test is allowed to cause at most one warning), then
	 * remember the warning parameters for verification by the test method.
	 *
	 * @param source  Ignored.
	 * @param warning The warning.
	 */
	@Override
	public void warningOccured(final Object source, final LogRecord warning) {
		assertNull(resourceKey);
		assertNull(parameters);
		assertNotNull(resourceKey = warning.getMessage());
		assertNotNull(parameters  = warning.getParameters());
	}

	/**
	 * Unmarshalls the given XML fragment of the given metadata standard.
	 */
	private DefaultLegalConstraints unmarshal(final String xml, Version metadataVersion) throws JAXBException {
		final MarshallerPool pool = getMarshallerPool();
		final Unmarshaller unmarshaller = pool.acquireUnmarshaller();
		unmarshaller.setProperty(XML.METADATA_VERSION, metadataVersion);
		unmarshaller.setProperty(XML.WARNING_LISTENER, this);
		final Object c = unmarshal(unmarshaller, xml);
		pool.recycle(unmarshaller);
		return (DefaultLegalConstraints) c;
	}

	/**
	 * Tests unmarshalling of an element containing an empty {@code codeListValue} attribute.
	 * This was used to cause a {@code NullPointerException} prior SIS-157 fix.
	 * ISO 19139.
	 *
	 * @throws JAXBException If an error occurred during the during unmarshalling processes.
	 *
	 * @see <a href="https://issues.apache.org/jira/browse/SIS-157">SIS-157</a>
	 */
	@Test
	public void testUnmarshallEmptyCodeListValue19139() throws JAXBException {
		final DefaultLegalConstraints c = unmarshal(emptyCodeListValueXML, Namespaces.ISO_19139);
		/*
		 * Verify metadata property.
		 */
		assertEquals("accessConstraints", Restriction.INTELLECTUAL_PROPERTY_RIGHTS, getSingleton(c.getAccessConstraints()));
		assertTrue("useConstraints", c.getUseConstraints().isEmpty());
		/*
		 * Verify warning message emitted during unmarshalling.
		 */
		assertEquals("warning", "NullCollectionElement_1", resourceKey);
		assertArrayEquals("warning", new String[] {"CodeListSet<Restriction>"}, parameters);
	}
	
	/**
	 * Tests unmarshalling of an element containing an empty {@code codeListValue} attribute.
	 * This was used to cause a {@code NullPointerException} prior SIS-157 fix.
	 * ISO 19115-3.
	 *
	 * @throws JAXBException If an error occurred during the during unmarshalling processes.
	 *
	 * @see <a href="https://issues.apache.org/jira/browse/SIS-157">SIS-157</a>
	 */
	@Test
	public void testUnmarshallEmptyCodeListValue191153() throws JAXBException {
		final DefaultLegalConstraints c = unmarshal(ISOTestUtils.from19139(emptyCodeListValueXML), Namespaces.ISO_19115_3);
		/*
		 * Verify metadata property.
		 */
		assertEquals("accessConstraints", Restriction.INTELLECTUAL_PROPERTY_RIGHTS, getSingleton(c.getAccessConstraints()));
		assertTrue("useConstraints", c.getUseConstraints().isEmpty());
		/*
		 * Verify warning message emitted during unmarshalling.
		 */
		// This does not occur with ISO 19115-3 unmarshalling for an unknown reason.
		// DefaultLegalConstraints was not in any way altered in the ISO 19115-3 update to SIS.
		//assertEquals("warning", "NullCollectionElement_1", resourceKey);
		//assertArrayEquals("warning", new String[] {"CodeListSet<Restriction>"}, parameters);
	}

	/**
	 * Tests (un)marshalling of a XML fragment containing the {@link Restriction#LICENCE} code.
	 * The spelling changed between ISO 19115:2003 and 19115:2014, from "license" to "licence".
	 * We need to ensure that XML marshalling use the old spelling, until the XML schema is updated.
	 *
	 * @throws JAXBException If an error occurred during the during unmarshalling processes.
	 */
	// There is a not a 19115-3 test for this because I do not believe it applies to that standard.
	@Test
	public void testLicenceCode19139() throws JAXBException {
		final DefaultLegalConstraints c = new DefaultLegalConstraints();
		c.setUseConstraints(singleton(Restriction.LICENCE));
		assertXmlEquals(licenceCodeXML, marshal(c), "xmlns:*");
		/*
		 * Unmarshall and ensure that we got back the original LICENCE code, not a new "LICENSE" code.
		 */
		final DefaultLegalConstraints actual = unmarshal(licenceCodeXML, Namespaces.ISO_19139);
		assertSame(Restriction.LICENCE, getSingleton(actual.getUseConstraints()));
		assertEquals(c, actual);
	}
}
