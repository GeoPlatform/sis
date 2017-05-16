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
package org.apache.sis.metadata.iso.citation;

import static org.apache.sis.test.TestUtilities.getSingleton;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.Iterator;

import javax.xml.bind.JAXBException;

import org.apache.sis.test.TestUtilities;
import org.apache.sis.test.XMLTestCase;
import org.apache.sis.util.iso.SimpleInternationalString;
import org.apache.sis.xml.IdentifierSpace;
import org.apache.sis.xml.Namespaces;
import org.junit.Test;
import org.opengis.metadata.citation.CitationDate;
import org.opengis.metadata.citation.Contact;
import org.opengis.metadata.citation.DateType;
import org.opengis.metadata.citation.Party;
import org.opengis.metadata.citation.Responsibility;
import org.opengis.metadata.citation.Role;


/**
 * Tests {@link DefaultCitation}.
 *
 * @author  Martin Desruisseaux (Geomatys)
 * @author  Cullen Rombach		(Image Matters)
 * @since   0.8
 * @version 0.8
 * @module
 */
public final strictfp class DefaultCitationTest extends XMLTestCase {
    /**
     * An XML file in this package containing a citation.
     */
    private static final String XML_FILE = "Citation.xml";

    /**
     * Tests XML marshalling.
     * This method also tests usage of {@code gml:id} and {@code xlink:href}.
     *
     * @throws JAXBException if an error occurred during marshalling.
     */
    @Test
    public void testMarshalling() throws JAXBException {
        final DefaultContact contact = new DefaultContact();
        contact.setContactInstructions(new SimpleInternationalString("Send carrier pigeon."));
        contact.getIdentifierMap().putSpecialized(IdentifierSpace.ID, "ip-protocol");
        final DefaultCitation c = new DefaultCitation("Fight against poverty");
        c.setCitedResponsibleParties(Arrays.asList(
                new DefaultResponsibility(Role.ORIGINATOR, null, new DefaultIndividual("Maid Marian", null, contact)),
                new DefaultResponsibility(Role.FUNDER,     null, new DefaultIndividual("Robin Hood",  null, contact))
        ));
        c.getDates().add(new DefaultCitationDate(TestUtilities.date("2015-10-17 00:00:00"), DateType.ADOPTED));
        // Check that XML file built by the marshaller is the same as the example file.
        // The addition of the argument "Namespaces.ISO_19115_3" is the primary change from the ISO 19139 test.
        assertMarshalEqualsFile(XML_FILE, c, Namespaces.ISO_19115_3, "xlmns:*", "xsi:schemaLocation");
    }

    /**
     * Tests XML unmarshalling.
     * This method also tests usage of {@code gml:id} and {@code xlink:href}.
     *
     * @throws JAXBException if an error occurred during unmarshalling.
     */
    @Test
    public void testUnmarshalling() throws JAXBException {
        final DefaultCitation c = unmarshalFile(DefaultCitation.class, XML_FILE, Namespaces.ISO_19115_3);
        assertEquals("title", c.getTitle().toString(), "Fight against poverty");

        final CitationDate date = getSingleton(c.getDates());
        assertEquals("date", date.getDate(), TestUtilities.date("2015-10-17 00:00:00"));
        assertEquals("dateType", date.getDateType(), DateType.ADOPTED);

        final Iterator<Responsibility> it = c.getCitedResponsibleParties().iterator();
        final Contact contact = assertResponsibilityEquals(Role.ORIGINATOR, "Maid Marian", it.next());
        assertEquals("Contact instruction", "Send carrier pigeon.", contact.getContactInstructions().toString());

        // Thanks to xlink:href, the Contact shall be the same instance as above.
        assertSame("contact", contact, assertResponsibilityEquals(Role.FUNDER, "Robin Hood", it.next()));
        assertFalse(it.hasNext());
    }

    /**
     * Asserts that the given responsibility has the expected properties, then returns its contact info.
     */
    private static Contact assertResponsibilityEquals(final Role role, final String name, final Responsibility actual) {
        assertEquals("role", role, actual.getRole());
        final Party p = getSingleton(actual.getParties());
        assertEquals("name", name, p.getName().toString());
        return getSingleton(p.getContactInfo());
    }
}
