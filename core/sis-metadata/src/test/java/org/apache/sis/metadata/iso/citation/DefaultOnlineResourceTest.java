package org.apache.sis.metadata.iso.citation;

import org.apache.sis.internal.jaxb.LegacyNamespaces;
import org.apache.sis.metadata.iso.DefaultMetadata;
import org.apache.sis.test.XMLTestCase;
import org.apache.sis.util.iso.SimpleInternationalString;
import org.apache.sis.xml.Namespaces;
import org.apache.sis.xml.XML;
import org.junit.Before;
import org.junit.Test;
import org.opengis.metadata.citation.OnLineFunction;
import org.opengis.metadata.citation.OnlineResource;
import org.opengis.metadata.distribution.DigitalTransferOptions;
import org.opengis.metadata.distribution.Distribution;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;

import static org.apache.sis.test.Assert.assertXmlEquals;
import static org.junit.Assert.assertEquals;

/**
 * This test class is primarily created to test if the description element of OnlineResource works.
 */
public class DefaultOnlineResourceTest extends XMLTestCase {

    private DefaultOnlineResource onlineResource;

    private String expectedXML =
            "<gmd:CI_OnlineResource xmlns:gmd=\"" + Namespaces.GMD + '"' +
                    " xmlns:gmi=\"" + LegacyNamespaces.GMI + '"' +
                    " xmlns:gco=\"" + LegacyNamespaces.GCO + "\">\n" +
                    "   <gmd:linkage>\n" +
                    "       <gmd:URL>https://basemap.nationalmap.gov/arcgis/rest/services/USGSShadedReliefOnly/MapServer</gmd:URL>\n" +
                    "   </gmd:linkage>\n" +
                    "   <gmd:name>\n" +
                    "       <gco:CharacterString>Shaded Relief Only Base Map (OGC WMS)</gco:CharacterString>\n" +
                    "   </gmd:name>\n" +
                    "   <gmd:description>\n" +
                    "       <gco:CharacterString>An OGC WMS Map Service for visualizing 1/3 arc-second elevation data as shaded relief.</gco:CharacterString>\n" +
                    "   </gmd:description>\n" +
                    "</gmd:CI_OnlineResource>";

    @Before
    public void setup() throws URISyntaxException {
        onlineResource = new DefaultOnlineResource();
        onlineResource.setLinkage(new URI("https://basemap.nationalmap.gov/arcgis/rest/services/USGSShadedReliefOnly/MapServer"));
        onlineResource.setName(new SimpleInternationalString("Shaded Relief Only Base Map (OGC WMS)"));
        onlineResource.setDescription(new SimpleInternationalString("An OGC WMS Map Service for visualizing 1/3 arc-second elevation data as shaded relief."));
    }

    @Test
    public void testMarshalOnlineResource() throws Exception {
        // Marshal the object to XML and check if it matches the expected XML.
        String actual = XML.marshal(onlineResource);
        assertXmlEquals(expectedXML, actual, "xmlns:*");
    }

    @Test
    public void testUnmarshalOnlineResource() throws Exception {
        DefaultOnlineResource actual = (DefaultOnlineResource) XML.unmarshal(expectedXML);
        assertEquals(onlineResource, actual);
    }

}
