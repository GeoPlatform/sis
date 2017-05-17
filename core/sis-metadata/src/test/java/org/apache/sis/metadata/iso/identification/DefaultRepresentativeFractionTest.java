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

import static org.apache.sis.test.Assert.assertXmlEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import javax.xml.bind.JAXBException;

import org.apache.sis.internal.jaxb.LegacyNamespaces;
import org.apache.sis.test.ISOTestUtils;
import org.apache.sis.test.TestCase;
import org.apache.sis.xml.IdentifierSpace;
import org.apache.sis.xml.Namespaces;
import org.apache.sis.xml.XML;
import org.junit.Test;


/**
 * Tests {@link DefaultRepresentativeFraction}.
 *
 * @author  Martin Desruisseaux (Geomatys)
 * @author  Cullen Rombach		(Image Matters)
 * @since   0.4
 * @version 0.8
 */
public final strictfp class DefaultRepresentativeFractionTest extends TestCase {
	
	private static String getIdentifiersXML() {
		return "<gmd:MD_RepresentativeFraction xmlns:gmd=\"" + Namespaces.GMD + '"' +
                                              " xmlns:gco=\"" + LegacyNamespaces.GCO + '"' +
                                              " id=\"scale\">\n" +
                "  <gmd:denominator>\n" +
                "    <gco:Integer>8</gco:Integer>\n" +
                "  </gmd:denominator>\n" +
                "</gmd:MD_RepresentativeFraction>";
	}
	
    /**
     * Test {@link DefaultRepresentativeFraction#setScale(double)}.
     */
    @Test
    public void testSetScale() {
        final DefaultRepresentativeFraction fraction = new DefaultRepresentativeFraction();
        assertEquals("Initial value", 0L, fraction.getDenominator());
        assertTrue("Initial value", Double.isNaN(fraction.doubleValue()));

        fraction.setScale(0.25);
        assertEquals("getDenominator()", 4L, fraction.getDenominator());
        assertEquals("doubleValue()", 0.25, fraction.doubleValue(), 0.0);
    }

    /**
     * Tests XML marshalling of identifiers.
     * ISO 19139.
     *
     * @throws JAXBException Should never happen.
     */
    @Test
    public void testIdentifiers19139() throws JAXBException {
        final DefaultRepresentativeFraction fraction = new DefaultRepresentativeFraction(8);
        fraction.getIdentifierMap().putSpecialized(IdentifierSpace.ID, "scale");
        final String xml = XML.marshal(fraction);
        assertXmlEquals(getIdentifiersXML(), xml, "xmlns:*");
        /*
         * Unmarshal the element back to a Java object and compare to the original.
         */
        assertEquals(fraction, XML.unmarshal(xml));
    }
    
    /**
     * Tests XML marshalling of identifiers.
     * ISO 19115-3.
     *
     * @throws JAXBException Should never happen.
     */
    @Test
    public void testIdentifiers191153() throws JAXBException {
        final DefaultRepresentativeFraction fraction = new DefaultRepresentativeFraction(8);
        fraction.getIdentifierMap().putSpecialized(IdentifierSpace.ID, "scale");
        final String xml = XML.marshal(fraction, Namespaces.ISO_19115_3);
        assertXmlEquals(ISOTestUtils.from19139(getIdentifiersXML()), xml, "xmlns:*");
        /*
         * Unmarshal the element back to a Java object and compare to the original.
         */
        assertEquals(fraction, XML.unmarshal(xml, Namespaces.ISO_19115_3));
    }

    /**
     * Tests indirectly {@link DefaultRepresentativeFraction#freeze()}.
     * This method verifies that a call to {@link DefaultResolution#freeze()}
     * implies a call to {@link DefaultRepresentativeFraction#freeze()}.
     *
     * @since 0.7
     */
    @Test
    public void testFreeze() {
        final DefaultRepresentativeFraction fraction = new DefaultRepresentativeFraction(1000);
        final DefaultResolution resolution = new DefaultResolution(fraction);
        resolution.freeze();
        final DefaultRepresentativeFraction clone = (DefaultRepresentativeFraction) resolution.getEquivalentScale();
        assertEquals ("Fraction should have the same value.",      fraction, clone);
        assertNotSame("Should have copied the fraction instance.", fraction, clone);
        try {
            clone.setDenominator(10);
            fail("Shall not be allowed to modify an unmodifiable fraction.");
        } catch (UnsupportedOperationException e) {
            // This is the expected exception.
        }
    }
}
