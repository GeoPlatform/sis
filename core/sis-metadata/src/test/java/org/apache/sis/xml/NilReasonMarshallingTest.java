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

import static org.apache.sis.test.Assert.assertXmlEquals;
import static org.apache.sis.test.MetadataAssert.assertTitleEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.opengis.test.Assert.assertInstanceOf;

import javax.xml.bind.JAXBException;

import org.apache.sis.internal.jaxb.LegacyNamespaces;
import org.apache.sis.test.DependsOnMethod;
import org.apache.sis.test.ISOTestUtils;
import org.apache.sis.test.XMLTestCase;
import org.junit.Test;
import org.opengis.metadata.citation.Citation;
import org.opengis.metadata.citation.Series;
import org.opengis.metadata.content.Band;
import org.opengis.metadata.quality.ConformanceResult;
import org.opengis.metadata.spatial.Dimension;


/**
 * Tests the XML marshalling of object having {@code nilReason} attribute.
 *
 * @author  Martin Desruisseaux (Geomatys)
 * @author  Cullen Rombach		(Image Matters)
 * @since   0.3
 * @version 0.8
 * @module
 *
 * @see <a href="http://jira.geotoolkit.org/browse/GEOTK-149">GEOTK-149</a>
 */
public final strictfp class NilReasonMarshallingTest extends XMLTestCase {

	private static String getMissingXML() {
		return "<gmd:CI_Citation xmlns:gmd=\"" + Namespaces.GMD + '"' +
				" xmlns:gco=\"" + LegacyNamespaces.GCO + "\">\n" +
				"  <gmd:title>\n" +
				"    <gco:CharacterString>A title</gco:CharacterString>\n" +
				"  </gmd:title>\n" +
				"  <gmd:series gco:nilReason=\"missing\"/>\n" +
				"</gmd:CI_Citation>";
	}

	private static String getMissingBooleanXML() {
		return "<gmd:DQ_ConformanceResult xmlns:gmd=\"" + Namespaces.GMD + '"' +
				" xmlns:gco=\"" + LegacyNamespaces.GCO + "\">\n" +
				"  <gmd:explanation>\n" +
				"    <gco:CharacterString>An explanation</gco:CharacterString>\n" +
				"  </gmd:explanation>\n" +
				"  <gmd:pass gco:nilReason=\"missing\"/>\n" +
				"</gmd:DQ_ConformanceResult>";
	}

	private static String getMissingIntegerXML() {
		return "<gmd:MD_Dimension xmlns:gmd=\"" + Namespaces.GMD + '"' +
				" xmlns:gco=\"" + LegacyNamespaces.GCO + "\">\n" +
				"  <gmd:dimensionSize gco:nilReason=\"unknown\"/>\n" +
				"</gmd:MD_Dimension>";
	}

	private static String getMissingDoubleXML() {
		return "<gmd:MD_Band xmlns:gmd=\"" + Namespaces.GMD + '"' +
				" xmlns:gco=\"" + LegacyNamespaces.GCO + "\">\n" +
				"  <gmd:minValue gco:nilReason=\"unknown\"/>\n" +
				"  <gmd:peakResponse gco:nilReason=\"missing\"/>\n" +
				"</gmd:MD_Band>";
	}

	private static String getOtherXML() {
		return "<gmd:CI_Citation xmlns:gmd=\"" + Namespaces.GMD + '"' +
				" xmlns:gco=\"" + LegacyNamespaces.GCO + "\">\n" +
				"  <gmd:title>\n" +
				"    <gco:CharacterString>A title</gco:CharacterString>\n" +
				"  </gmd:title>\n" +
				"  <gmd:series gco:nilReason=\"other:myReason\"/>\n" +
				"</gmd:CI_Citation>";
	}

	private static String getURI_XML() {
		return "<gmd:CI_Citation xmlns:gmd=\"" + Namespaces.GMD + '"' +
				" xmlns:gco=\"" + LegacyNamespaces.GCO + "\">\n" +
				"  <gmd:title>\n" +
				"    <gco:CharacterString>A title</gco:CharacterString>\n" +
				"  </gmd:title>\n" +
				"  <gmd:series gco:nilReason=\"http://www.myreason.org\"/>\n" +
				"</gmd:CI_Citation>";
	}

	/**
	 * Tests a simple case for a missing data.
	 *
	 * @throws JAXBException Should never happen.
	 */
	@Test
	public void testMissing19139() throws JAXBException {
		final String expected = getMissingXML();

		final Citation citation = (Citation) XML.unmarshal(expected);
		assertTitleEquals("citation", "A title", citation);

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
	 * @see testMissing19139(). This is the ISO 19115-3 version.
	 * @throws JAXBException
	 */
	@Test
	public void testMissing191153() throws JAXBException {
		final String expected = ISOTestUtils.from19139(getMissingXML());

		final Citation citation = (Citation) XML.unmarshal(expected, Namespaces.ISO_19115_3);
		assertTitleEquals("citation", "A title", citation);

		final Series series = citation.getSeries();
		assertInstanceOf("Should have instantiated a proxy.", NilObject.class, series);

		final NilReason reason = ((NilObject) series).getNilReason();
		assertSame("nilReason", NilReason.MISSING, reason);
		assertNull("NilReason.explanation", reason.getOtherExplanation());
		assertNull("NilReason.URI",         reason.getURI());

		assertEquals("Series[missing]", series.toString());
		assertNull("All attributes are expected to be null.", series.getName());

		final String actual = XML.marshal(citation, Namespaces.ISO_19115_3);
		assertXmlEquals(expected, actual, "xmlns:*");
		assertEquals(citation, XML.unmarshal(actual, Namespaces.ISO_19115_3));
	}

	/**
	 * Tests a missing boolean value. The {@link Boolean}, {@link Integer}, {@link Double} and {@link String}
	 * values are implemented as special cases in {@link NilReason}, because they are final classes on which
	 * we have no control.
	 *
	 * @throws JAXBException Should never happen.
	 */
	@Test
	@DependsOnMethod("testMissing19139")
	public void testMissingBoolean19139() throws JAXBException {
		final String expected = getMissingBooleanXML();

		final ConformanceResult result = (ConformanceResult) XML.unmarshal(expected);
		assertEquals("explanation", "An explanation", result.getExplanation().toString());

		final Boolean pass = result.pass();
		assertNotNull("Expected a sentinel value.", pass);
		assertEquals ("Nil value shall be false.",  Boolean.FALSE, pass);
		assertNotSame("Expected a sentinel value.", Boolean.FALSE, pass);
		assertSame("nilReason", NilReason.MISSING, NilReason.forObject(pass));

		final String actual = XML.marshal(result);
		assertXmlEquals(expected, actual, "xmlns:*");
		assertEquals(result, XML.unmarshal(actual));
	}
	
	/**
	 * @see testMissingBoolean19139(). This is the ISO 19115-3 version.
	 * @throws JAXBException
	 */
	@Test
	@DependsOnMethod("testMissing191153")
	public void testMissingBoolean191153() throws JAXBException {
		final String expected = ISOTestUtils.from19139(getMissingBooleanXML());

		final ConformanceResult result = (ConformanceResult) XML.unmarshal(expected, Namespaces.ISO_19115_3);
		assertEquals("explanation", "An explanation", result.getExplanation().toString());

		final Boolean pass = result.pass();
		assertNotNull("Expected a sentinel value.", pass);
		assertEquals ("Nil value shall be false.",  Boolean.FALSE, pass);
		assertNotSame("Expected a sentinel value.", Boolean.FALSE, pass);
		assertSame("nilReason", NilReason.MISSING, NilReason.forObject(pass));

		final String actual = XML.marshal(result, Namespaces.ISO_19115_3);
		assertXmlEquals(expected, actual, "xmlns:*");
		assertEquals(result, XML.unmarshal(actual, Namespaces.ISO_19115_3));
	}

	/**
	 * Tests a missing integer value. The {@link Boolean}, {@link Integer}, {@link Double} and {@link String}
	 * values are implemented as special cases in {@link NilReason}, because they are final classes on which
	 * we have no control.
	 *
	 * @throws JAXBException Should never happen.
	 */
	@Test
	@DependsOnMethod("testMissing19139")
	@SuppressWarnings("UnnecessaryBoxing")
	public void testMissingInteger19139() throws JAXBException {
		final String expected = getMissingIntegerXML();

		final Dimension result = (Dimension) XML.unmarshal(expected);

		final Integer size = result.getDimensionSize();
		assertNotNull("Expected a sentinel value.", size);
		assertEquals ("Nil value shall be 0.",      Integer.valueOf(0), size);
		assertNotSame("Expected a sentinel value.", Integer.valueOf(0), size);
		assertSame("nilReason", NilReason.UNKNOWN, NilReason.forObject(size));

		final String actual = XML.marshal(result);
		assertXmlEquals(expected, actual, "xmlns:*");
		assertEquals(result, XML.unmarshal(actual));
	}
	
	/**
	 * @see testMissingInteger19139(). This is the ISO 19115-3 version.
	 * @throws JAXBException
	 */
	@Test
	@DependsOnMethod("testMissing191153")
	@SuppressWarnings("UnnecessaryBoxing")
	public void testMissingInteger191153() throws JAXBException {
		final String expected = ISOTestUtils.from19139(getMissingIntegerXML());

		final Dimension result = (Dimension) XML.unmarshal(expected, Namespaces.ISO_19115_3);

		final Integer size = result.getDimensionSize();
		assertNotNull("Expected a sentinel value.", size);
		assertEquals ("Nil value shall be 0.",      Integer.valueOf(0), size);
		assertNotSame("Expected a sentinel value.", Integer.valueOf(0), size);
		assertSame("nilReason", NilReason.UNKNOWN, NilReason.forObject(size));

		final String actual = XML.marshal(result, Namespaces.ISO_19115_3);
		assertXmlEquals(expected, actual, "xmlns:*");
		assertEquals(result, XML.unmarshal(actual, Namespaces.ISO_19115_3));
	}

	/**
	 * Tests a missing double value.
	 *
	 * @throws JAXBException Should never happen.
	 */
	@Test
	@DependsOnMethod("testMissing19139")
	public void testMissingDouble19139() throws JAXBException {
		final String expected = getMissingDoubleXML();	

		final Band result = (Band) XML.unmarshal(expected);

		final Double minValue = result.getMinValue();
		assertNotNull("Expected a sentinel value.", minValue);
		assertTrue("Nil value shall be NaN.", minValue.isNaN());
		assertSame("nilReason", NilReason.UNKNOWN, NilReason.forObject(minValue));

		final Double peakResponse = result.getMinValue();
		assertNotNull("Expected a sentinel value.", peakResponse);
		assertTrue("Nil value shall be NaN.", peakResponse.isNaN());
		assertSame("nilReason", NilReason.UNKNOWN, NilReason.forObject(peakResponse));

		final String actual = XML.marshal(result);
		assertXmlEquals(expected, actual, "xmlns:*");
		assertEquals(result, XML.unmarshal(actual));
	}
	
	/**
	 * @see testMissingDouble19139(). This is the ISO 19115-3 version.
	 * @throws JAXBException
	 */
	@Test
	@DependsOnMethod("testMissing191153")
	public void testMissingDouble191153() throws JAXBException {
		final String expected = ISOTestUtils.from19139(getMissingDoubleXML());	

		final Band result = (Band) XML.unmarshal(expected, Namespaces.ISO_19115_3);

		final Double minValue = result.getMinValue();
		assertNotNull("Expected a sentinel value.", minValue);
		assertTrue("Nil value shall be NaN.", minValue.isNaN());
		assertSame("nilReason", NilReason.UNKNOWN, NilReason.forObject(minValue));

		final Double peakResponse = result.getMinValue();
		assertNotNull("Expected a sentinel value.", peakResponse);
		assertTrue("Nil value shall be NaN.", peakResponse.isNaN());
		assertSame("nilReason", NilReason.UNKNOWN, NilReason.forObject(peakResponse));

		final String actual = XML.marshal(result, Namespaces.ISO_19115_3);
		assertXmlEquals(expected, actual, "xmlns:*");
		assertEquals(result, XML.unmarshal(actual, Namespaces.ISO_19115_3));
	}

	/**
	 * Tests a case where the nil reason is specified by an other reason.
	 *
	 * @throws JAXBException Should never happen.
	 */
	@Test
	@DependsOnMethod("testMissing19139")
	public void testOther19139() throws JAXBException {
		final String expected = getOtherXML();

		final Citation citation = (Citation) XML.unmarshal(expected);
		assertTitleEquals("citation", "A title", citation);

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
	 * @see testOther19139(). This is the ISO 19115-3 version.
	 * @throws JAXBException
	 */
	@Test
	@DependsOnMethod("testMissing191153")
	public void testOther191153() throws JAXBException {
		final String expected = ISOTestUtils.from19139(getOtherXML());

		final Citation citation = (Citation) XML.unmarshal(expected, Namespaces.ISO_19115_3);
		assertTitleEquals("citation", "A title", citation);

		final Series series = citation.getSeries();
		assertInstanceOf("Should have instantiated a proxy.", NilObject.class, series);

		final NilReason reason = ((NilObject) series).getNilReason();
		assertEquals("NilReason.explanation", "myReason", reason.getOtherExplanation());
		assertNull("NilReason.URI", reason.getURI());

		assertEquals("Series[other:myReason]", series.toString());
		assertNull("All attributes are expected to be null.", series.getName());

		final String actual = XML.marshal(citation, Namespaces.ISO_19115_3);
		assertXmlEquals(expected, actual, "xmlns:*");
		assertEquals(citation, XML.unmarshal(actual, Namespaces.ISO_19115_3));
	}

	/**
	 * Tests a case where the nil reason is specified by a URI.
	 *
	 * @throws JAXBException Should never happen.
	 */
	@Test
	@DependsOnMethod("testMissing19139")
	public void testURI19139() throws JAXBException {
		final String expected = getURI_XML();

		final Citation citation = (Citation) XML.unmarshal(expected);
		assertTitleEquals("citation", "A title", citation);

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
	
	/**
	 * @see testURI19139(). This is the ISO 19115-3 version.
	 * @throws JAXBException
	 */
	@Test
	@DependsOnMethod("testMissing191153")
	public void testURI191153() throws JAXBException {
		final String expected = ISOTestUtils.from19139(getURI_XML());

		final Citation citation = (Citation) XML.unmarshal(expected, Namespaces.ISO_19115_3);
		assertTitleEquals("citation", "A title", citation);

		final Series series = citation.getSeries();
		assertInstanceOf("Should have instantiated a proxy.", NilObject.class, series);

		final NilReason reason = ((NilObject) series).getNilReason();
		assertNull("NilReason.explanation", reason.getOtherExplanation());
		assertEquals("NilReason.URI", "http://www.myreason.org", String.valueOf(reason.getURI()));

		assertEquals("Series[http://www.myreason.org]", series.toString());
		assertNull("All attributes are expected to be null.", series.getName());

		final String actual = XML.marshal(citation, Namespaces.ISO_19115_3);
		assertXmlEquals(expected, actual, "xmlns:*");
		assertEquals(citation, XML.unmarshal(actual, Namespaces.ISO_19115_3));
	}
}
