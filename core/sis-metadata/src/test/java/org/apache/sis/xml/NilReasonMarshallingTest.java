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
package org.apache.sis.xml;

import javax.xml.bind.JAXBException;
import org.opengis.metadata.citation.Series;
import org.opengis.metadata.citation.Citation;
import org.apache.sis.test.XMLTestCase;
import org.junit.Test;

import static org.apache.sis.test.Assert.*;


/**
 * Tests the XML marshalling of object having {@code nilReason} attribute.
 *
 * @author  Martin Desruisseaux (Geomatys)
 * @since   0.3 (derived from geotk-3.18)
 * @version 0.3
 * @module
 *
 * @see <a href="http://jira.geotoolkit.org/browse/GEOTK-149">GEOTK-149</a>
 */
public final strictfp class NilReasonMarshallingTest extends XMLTestCase {
    /**
     * Tests a simple case for a missing data.
     *
     * @throws JAXBException Should never happen.
     */
    @Test
    public void testMissing() throws JAXBException {
        final String expected =
            "<gmd:CI_Citation xmlns:gmd=\"" + Namespaces.GMD + '"' +
                            " xmlns:gco=\"" + Namespaces.GCO + '"' +
                            " xmlns:xlink=\"" + Namespaces.XLINK + "\">\n" +
            "  <gmd:title>\n" +
            "    <gco:CharacterString>A title</gco:CharacterString>\n" +
            "  </gmd:title>\n" +
            "  <gmd:series gco:nilReason=\"missing\"/>\n" +
            "</gmd:CI_Citation>";

        final Citation citation = (Citation) XML.unmarshal(expected);
        assertEquals("title", "A title", citation.getTitle().toString());

        final Series series = citation.getSeries();
        assertInstanceOf("Should have instantiated a proxy.", NilObject.class, series);

        final NilReason reason = ((NilObject) series).getNilReason();
        assertSame("nilReason", NilReason.MISSING, reason);
        assertNull("NilReason.explanation", reason.getOtherExplanation());
        assertNull("NilReason.URI",         reason.getURI());

        assertEquals("Series[missing]", series.toString());
        assertNull("All attributes are expected to be null.", series.getName());

        final String actual = XML.marshal(citation);
        assertXmlEquals(expected, actual, "xmlns:*");
        assertEquals(citation, XML.unmarshal(actual));
    }

    /**
     * Tests a case where the nil reason is specified by an other reason.
     *
     * @throws JAXBException Should never happen.
     */
    @Test
    public void testOther() throws JAXBException {
        final String expected =
            "<gmd:CI_Citation xmlns:gmd=\"" + Namespaces.GMD + '"' +
                            " xmlns:gco=\"" + Namespaces.GCO + '"' +
                            " xmlns:xlink=\"" + Namespaces.XLINK + "\">\n" +
            "  <gmd:title>\n" +
            "    <gco:CharacterString>A title</gco:CharacterString>\n" +
            "  </gmd:title>\n" +
            "  <gmd:series gco:nilReason=\"other:myReason\"/>\n" +
            "</gmd:CI_Citation>";

        final Citation citation = (Citation) XML.unmarshal(expected);
        assertEquals("title", "A title", citation.getTitle().toString());

        final Series series = citation.getSeries();
        assertInstanceOf("Should have instantiated a proxy.", NilObject.class, series);

        final NilReason reason = ((NilObject) series).getNilReason();
        assertEquals("NilReason.explanation", "myReason", reason.getOtherExplanation());
        assertNull("NilReason.URI", reason.getURI());

        assertEquals("Series[other:myReason]", series.toString());
        assertNull("All attributes are expected to be null.", series.getName());

        final String actual = XML.marshal(citation);
        assertXmlEquals(expected, actual, "xmlns:*");
        assertEquals(citation, XML.unmarshal(actual));
    }

    /**
     * Tests a case where the nil reason is specified by a URI.
     *
     * @throws JAXBException Should never happen.
     */
    @Test
    public void testURI() throws JAXBException {
        final String expected =
            "<gmd:CI_Citation xmlns:gmd=\"" + Namespaces.GMD + '"' +
                            " xmlns:gco=\"" + Namespaces.GCO + '"' +
                            " xmlns:xlink=\"" + Namespaces.XLINK + "\">\n" +
            "  <gmd:title>\n" +
            "    <gco:CharacterString>A title</gco:CharacterString>\n" +
            "  </gmd:title>\n" +
            "  <gmd:series gco:nilReason=\"http://www.myreason.org\"/>\n" +
            "</gmd:CI_Citation>";

        final Citation citation = (Citation) XML.unmarshal(expected);
        assertEquals("title", "A title", citation.getTitle().toString());

        final Series series = citation.getSeries();
        assertInstanceOf("Should have instantiated a proxy.", NilObject.class, series);

        final NilReason reason = ((NilObject) series).getNilReason();
        assertNull("NilReason.explanation", reason.getOtherExplanation());
        assertEquals("NilReason.URI", "http://www.myreason.org", String.valueOf(reason.getURI()));

        assertEquals("Series[http://www.myreason.org]", series.toString());
        assertNull("All attributes are expected to be null.", series.getName());

        final String actual = XML.marshal(citation);
        assertXmlEquals(expected, actual, "xmlns:*");
        assertEquals(citation, XML.unmarshal(actual));
    }
}
