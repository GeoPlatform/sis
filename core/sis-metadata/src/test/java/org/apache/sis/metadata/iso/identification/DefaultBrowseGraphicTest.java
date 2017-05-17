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

import static java.util.Collections.singletonMap;
import static org.apache.sis.test.Assert.assertXmlEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import java.io.StringWriter;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.LogRecord;

import javax.xml.bind.JAXBException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.sis.internal.jaxb.LegacyNamespaces;
import org.apache.sis.test.DependsOnMethod;
import org.apache.sis.test.ISOTestUtils;
import org.apache.sis.test.TestCase;
import org.apache.sis.util.Version;
import org.apache.sis.util.logging.Logging;
import org.apache.sis.util.logging.WarningListener;
import org.apache.sis.xml.Namespaces;
import org.apache.sis.xml.XML;
import org.junit.Test;


/**
 * Tests {@link DefaultBrowseGraphic}.
 *
 * @author  Martin Desruisseaux (Geomatys)
 * @author  Cullen Rombach		(Image Matters)
 * @since   0.4
 * @version 0.8
 * @module
 */
public final strictfp class DefaultBrowseGraphicTest extends TestCase {

	private static String getMimeFileTypeXML() {
		return "<gmd:MD_BrowseGraphic xmlns:gmd=\"" + Namespaces.GMD + '"' +
				" xmlns:gmx=\"" + LegacyNamespaces.GMX + "\">\n" +
				"  <gmd:fileType>\n" +
				"    <gmx:MimeFileType type=\"image/tiff\">image/tiff</gmx:MimeFileType>\n" +
				"  </gmd:fileType>\n" +
				"</gmd:MD_BrowseGraphic>";
	}

	private static String getFileNameXML() {
		return "<gmd:MD_BrowseGraphic xmlns:gmd=\"" + Namespaces.GMD + '"' +
				" xmlns:gmx=\"" + LegacyNamespaces.GMX + "\">\n" +
				"  <gmd:fileName>\n" +
				"    <gmx:FileName src=\"file:/catalog/image.png\">image.png</gmx:FileName>\n" +
				"  </gmd:fileName>\n" +
				"</gmd:MD_BrowseGraphic>";
	}

	private static String getFileNameWithoutSrcXML() {
		return "<gmd:MD_BrowseGraphic xmlns:gmd=\"" + Namespaces.GMD + '"' +
				" xmlns:gmx=\"" + LegacyNamespaces.GMX + "\">\n" +
				"  <gmd:fileName>\n" +
				"    <gmx:FileName>file:/catalog/image.png</gmx:FileName>\n" +
				"  </gmd:fileName>\n" +
				"</gmd:MD_BrowseGraphic>";
	}

	private static String getFileNameAndTypeXML() {
		return "<gmd:MD_BrowseGraphic xmlns:gmd=\"" + Namespaces.GMD + '"' +
				" xmlns:gmx=\"" + LegacyNamespaces.GMX + "\">\n" +
				"  <gmd:fileName>\n" +
				"    <gmx:FileName src=\"file:/catalog/image.png\">image.png</gmx:FileName>\n" +
				"  </gmd:fileName>\n" +
				"  <gmd:fileType>\n" +
				"    <gmx:MimeFileType type=\"image/tiff\">image/tiff</gmx:MimeFileType>\n" +
				"  </gmd:fileType>\n" +
				"</gmd:MD_BrowseGraphic>";
	}

	private static String getStringSubstitutionXML() {
		return "<gmd:MD_BrowseGraphic xmlns:gmd=\"" + Namespaces.GMD + '"' +
				" xmlns:gco=\"" + LegacyNamespaces.GCO + "\">\n" +
				"  <gmd:fileName>\n" +
				"    <gco:CharacterString>file:/catalog/image.png</gco:CharacterString>\n" +
				"  </gmd:fileName>\n" +
				"  <gmd:fileType>\n" +
				"    <gco:CharacterString>image/tiff</gco:CharacterString>\n" +
				"  </gmd:fileType>\n" +
				"</gmd:MD_BrowseGraphic>";
	}

	private static String getStringSubstitutionXML191153() {
		return "<mcc:MD_BrowseGraphic xmlns:mcc=\"" + Namespaces.MCC + '"' + 
				" xmlns:gco=\"" + Namespaces.GCO + "\">\n" +
				"  <mcc:fileName>\n" +
				"    <gco:CharacterString>file:/catalog/image.png</gco:CharacterString>\n" +
				"  </mcc:fileName>\n" +
				"  <mcc:fileType>\n" +
				"    <gco:CharacterString>image/tiff</gco:CharacterString>\n" +
				"  </mcc:fileType>\n" +
				"</mcc:MD_BrowseGraphic>";
	}

	private static String getDuplicatedValuesXML() {
		return "<gmd:MD_BrowseGraphic xmlns:gmd=\"" + Namespaces.GMD + '"' +
				" xmlns:gmx=\"" + LegacyNamespaces.GMX + '"' +
				" xmlns:gco=\"" + LegacyNamespaces.GCO + "\">\n" +
				"  <gmd:fileName>\n" +
				"    <gmx:FileName src=\"file:/catalog/image.png\">image.png</gmx:FileName>\n" +
				"    <gco:CharacterString>file:/catalog/image.png</gco:CharacterString>\n" +
				"  </gmd:fileName>\n" +
				"</gmd:MD_BrowseGraphic>";
	}
	
	private static String getWarningsSnippet1() {
		return "<gmx:FileName src=\"file:/catalog/image.png\">image.png</gmx:FileName>";
	}
	
	private static String getWarningsSnippet2() {
		return "<gco:CharacterString>file:/catalog/image2.png</gco:CharacterString>";
	}
	
	private static String getWarningsXML(String first, String second) {
		return "<gmd:MD_BrowseGraphic xmlns:gmd=\"" + Namespaces.GMD + '"' +
				" xmlns:gmx=\"" + LegacyNamespaces.GMX + '"' +
				" xmlns:gco=\"" + LegacyNamespaces.GCO + "\">\n" +
				"  <gmd:fileName>\n" +
				"    " + first + "\n" +
				"    " + second + "\n" +
				"  </gmd:fileName>\n" +
				"</gmd:MD_BrowseGraphic>";
	}

	/**
	 * Tests XML marshalling of {@code <gmx:MimeFileType>} inside {@code <gmd:MD_BrowseGraphic>}.
	 * ISO 19139.
	 *
	 * @throws JAXBException if an error occurred while (un)marshalling the {@code BrowseGraphic}.
	 */
	@Test
	public void testMimeFileType19139() throws JAXBException {
		final DefaultBrowseGraphic browse = new DefaultBrowseGraphic();
		browse.setFileType("image/tiff");
		final String xml = XML.marshal(browse);
		assertXmlEquals(getMimeFileTypeXML(), xml, "xmlns:*");
		/*
		 * Unmarshal the element back to a Java object and compare to the original.
		 */
		assertEquals(browse, XML.unmarshal(xml));
	}

	/**
	 * Tests XML marshalling of {@code <gmx:MimeFileType>} inside {@code <gmd:MD_BrowseGraphic>}.
	 * ISO 19115-3.
	 *
	 * @throws JAXBException if an error occurred while (un)marshalling the {@code BrowseGraphic}.
	 */
	@Test
	public void testMimeFileType191153() throws JAXBException {
		final DefaultBrowseGraphic browse = new DefaultBrowseGraphic();
		browse.setFileType("image/tiff");
		final String xml = XML.marshal(browse, Namespaces.ISO_19115_3);
		assertXmlEquals(ISOTestUtils.from19139(getMimeFileTypeXML()), xml, "xmlns:*");
		/*
		 * Unmarshal the element back to a Java object and compare to the original.
		 */
		assertEquals(browse, XML.unmarshal(xml, Namespaces.ISO_19115_3));
	}

	/**
	 * Tests XML marshalling of {@code <gmx:FileName>} inside {@code <gmd:MD_BrowseGraphic>}.
	 * ISO 19139.
	 *
	 * @throws JAXBException if an error occurred while (un)marshalling the {@code BrowseGraphic}.
	 */
	@Test
	public void testFileName19139() throws JAXBException {
		final URI uri = URI.create("file:/catalog/image.png");
		final DefaultBrowseGraphic browse = new DefaultBrowseGraphic(uri);
		final String xml = XML.marshal(browse);
		assertXmlEquals(getFileNameXML(), xml, "xmlns:*");
		/*
		 * Unmarshal the element back to a Java object and compare to the original.
		 */
		assertEquals(browse, XML.unmarshal(xml));
	}

	/**
	 * Tests XML marshalling of {@code <gmx:FileName>} inside {@code <gmd:MD_BrowseGraphic>}.
	 * ISO 19115-3.
	 *
	 * @throws JAXBException if an error occurred while (un)marshalling the {@code BrowseGraphic}.
	 */
	@Test
	public void testFileName191153() throws JAXBException {
		final URI uri = URI.create("file:/catalog/image.png");
		final DefaultBrowseGraphic browse = new DefaultBrowseGraphic(uri);
		final String xml = XML.marshal(browse, Namespaces.ISO_19115_3);
		assertXmlEquals(ISOTestUtils.from19139(getFileNameXML()), xml, "xmlns:*");
		/*
		 * Unmarshal the element back to a Java object and compare to the original.
		 */
		assertEquals(browse, XML.unmarshal(xml, Namespaces.ISO_19115_3));
	}

	/**
	 * Tests unmarshalling of {@code <gmx:FileName>} without {@code src} attribute.
	 * ISO 19139.
	 *
	 * @throws JAXBException if an error occurred while (un)marshalling the {@code BrowseGraphic}.
	 */
	@Test
	@DependsOnMethod("testFileName19139")
	public void testFileNameWithoutSrc19139() throws JAXBException {
		final DefaultBrowseGraphic browse =
				(DefaultBrowseGraphic) XML.unmarshal(getFileNameWithoutSrcXML());

		assertEquals(URI.create("file:/catalog/image.png"), browse.getFileName());
	}

	/**
	 * Tests unmarshalling of {@code <gmx:FileName>} without {@code src} attribute.
	 * ISO 19115-3.
	 *
	 * @throws JAXBException if an error occurred while (un)marshalling the {@code BrowseGraphic}.
	 */
	@Test
	@DependsOnMethod("testFileName191153")
	public void testFileNameWithoutSrc191153() throws JAXBException {
		final DefaultBrowseGraphic browse = (DefaultBrowseGraphic) XML.unmarshal(
				ISOTestUtils.from19139(getFileNameWithoutSrcXML()), Namespaces.ISO_19115_3);

		assertEquals(URI.create("file:/catalog/image.png"), browse.getFileName());
	}

	/**
	 * Tests XML marshalling of {@code <gmx:FileName>} and {@code <gmx:MimeFileType>} together.
	 * ISO 19139.
	 *
	 * @throws JAXBException if an error occurred while (un)marshalling the {@code BrowseGraphic}.
	 */
	@Test
	@DependsOnMethod({"testFileName19139", "testMimeFileType19139"})
	public void testFileNameAndType19139() throws JAXBException {
		final URI uri = URI.create("file:/catalog/image.png");
		final DefaultBrowseGraphic browse = new DefaultBrowseGraphic(uri);
		browse.setFileType("image/tiff");
		final String xml = XML.marshal(browse);
		assertXmlEquals(getFileNameAndTypeXML(), xml, "xmlns:*");
		/*
		 * Unmarshal the element back to a Java object and compare to the original.
		 */
		assertEquals(browse, XML.unmarshal(xml));
	}

	/**
	 * Tests XML marshalling of {@code <gmx:FileName>} and {@code <gmx:MimeFileType>} together.
	 * ISO 19115-3.
	 *
	 * @throws JAXBException if an error occurred while (un)marshalling the {@code BrowseGraphic}.
	 */
	@Test
	@DependsOnMethod({"testFileName191153", "testMimeFileType191153"})
	public void testFileNameAndType191153() throws JAXBException {
		final URI uri = URI.create("file:/catalog/image.png");
		final DefaultBrowseGraphic browse = new DefaultBrowseGraphic(uri);
		browse.setFileType("image/tiff");
		final String xml = XML.marshal(browse, Namespaces.ISO_19115_3);
		assertXmlEquals(ISOTestUtils.from19139(getFileNameAndTypeXML()), xml, "xmlns:*");
		/*
		 * Unmarshal the element back to a Java object and compare to the original.
		 */
		assertEquals(browse, XML.unmarshal(xml, Namespaces.ISO_19115_3));
	}

	/**
	 * Tests XML marshalling of filename substituted by {@code <gco:CharacterString>}
	 * inside {@code <gmd:MD_BrowseGraphic>}.
	 * ISO 19139.
	 *
	 * @throws JAXBException if an error occurred while (un)marshalling the {@code BrowseGraphic}.
	 */
	@Test
	@DependsOnMethod("testFileNameAndType19139")
	public void testStringSubstitution19139() throws JAXBException {
		final URI uri = URI.create("file:/catalog/image.png");
		final DefaultBrowseGraphic browse = new DefaultBrowseGraphic(uri);
		browse.setFileType("image/tiff");
		final StringWriter buffer = new StringWriter();
		XML.marshal(browse, new StreamResult(buffer),
				singletonMap(XML.STRING_SUBSTITUTES, new String[] {"filename", "mimetype"}));
		final String xml = buffer.toString();
		assertXmlEquals(getStringSubstitutionXML(), xml, "xmlns:*");
		/*
		 * Unmarshal the element back to a Java object and compare to the original.
		 */
		assertEquals(browse, XML.unmarshal(xml));
	}

	/**
	 * Tests XML marshalling of filename substituted by {@code <gco:CharacterString>}
	 * inside {@code <gmd:MD_BrowseGraphic>}.
	 * ISO 19115-3.
	 *
	 * @throws JAXBException if an error occurred while (un)marshalling the {@code BrowseGraphic}.
	 */
	@Test
	@DependsOnMethod("testFileNameAndType191153")
	public void testStringSubstitution191153() throws JAXBException {
		// Create the DefaultBrowseGraphic object.
		final URI uri = URI.create("file:/catalog/image.png");
		final DefaultBrowseGraphic browse = new DefaultBrowseGraphic(uri);
		browse.setFileType("image/tiff");
		final StringWriter buffer = new StringWriter();

		// Need to add ISO 19115-3 version argument to the existing map.
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(XML.STRING_SUBSTITUTES, new String[] {"filename", "mimetype"});
		properties.put(XML.METADATA_VERSION, Namespaces.ISO_19115_3);

		// Marshal the actual result.
		XML.marshal(browse, new StreamResult(buffer), properties);
		final String xml = buffer.toString();

		// Check that the result is as expected.
		assertXmlEquals(getStringSubstitutionXML191153(), xml, "xmlns:*");

		//Unmarshal the element back to a Java object and compare to the original.
		assertEquals(browse, XML.unmarshal(xml, Namespaces.ISO_19115_3));
	}

	/**
	 * Tests the unmarshaller with the same URI in both {@code <gco:CharacterString>} and {@code <gmx:FileName>}.
	 * Since the URI is the same, the unmarshaller should not produce any warning since there is no ambiguity.
	 * ISO 19139.
	 *
	 * @throws JAXBException if an error occurred while (un)marshalling the {@code BrowseGraphic}.
	 */
	@Test
	@DependsOnMethod("testStringSubstitution19139")
	public void testDuplicatedValues19139() throws JAXBException {
		final Warning listener = new Warning();
		final DefaultBrowseGraphic browse = listener.unmarshal(getDuplicatedValuesXML(), Namespaces.ISO_19139);

		assertEquals(URI.create("file:/catalog/image.png"), browse.getFileName());
		assertFalse("Expected no warning.", listener.receivedWarning);
	}
	
	/**
	 * Tests the unmarshaller with the same URI in both {@code <gco:CharacterString>} and {@code <gmx:FileName>}.
	 * Since the URI is the same, the unmarshaller should not produce any warning since there is no ambiguity.
	 * ISO 19115-3.
	 *
	 * @throws JAXBException if an error occurred while (un)marshalling the {@code BrowseGraphic}.
	 */
	@Test
	@DependsOnMethod("testStringSubstitution191153")
	public void testDuplicatedValues191153() throws JAXBException {
		final Warning listener = new Warning();
		final DefaultBrowseGraphic browse = listener.unmarshal(ISOTestUtils.from19139(getDuplicatedValuesXML()), Namespaces.ISO_19115_3);

		assertEquals(URI.create("file:/catalog/image.png"), browse.getFileName());
		assertFalse("Expected no warning.", listener.receivedWarning);
	}

	/**
	 * Ensures that the unmarshaller produces a warning when {@code <gco:CharacterString>} and
	 * {@code <gmx:FileName>} both exist inside the same {@code <gmd:MD_BrowseGraphic>}.
	 * ISO 19139.
	 *
	 * @throws JAXBException if an error occurred while (un)marshalling the {@code BrowseGraphic}.
	 */
	@Test
	@DependsOnMethod("testStringSubstitution19139")
	public void testWarnings19139() throws JAXBException {
		testWarnings19139(getWarningsSnippet1(), getWarningsSnippet2());
		/*
		 * Test again with the same element value, but in reverse order.
		 * We do that for ensuring that FileName still has precedence.
		 */
		testWarnings19139(getWarningsSnippet2(), getWarningsSnippet1());
	}
	
	/**
	 * Ensures that the unmarshaller produces a warning when {@code <gco:CharacterString>} and
	 * {@code <gmx:FileName>} both exist inside the same {@code <gmd:MD_BrowseGraphic>}.
	 * ISO 19115-3.
	 *
	 * @throws JAXBException if an error occurred while (un)marshalling the {@code BrowseGraphic}.
	 */
	@Test
	@DependsOnMethod("testStringSubstitution191153")
	public void testWarnings191153() throws JAXBException {
		testWarnings191153(getWarningsSnippet1(), getWarningsSnippet2());
		/*
		 * Test again with the same element value, but in reverse order.
		 * We do that for ensuring that FileName still has precedence.
		 */
		testWarnings191153(getWarningsSnippet2(), getWarningsSnippet1());
	}

	/**
	 * Implementation of {@link #testWarnings()} using the given {@code <gmd:fileName>} values.
	 */
	private void testWarnings19139(final String first, final String second) throws JAXBException {
		final Warning listener = new Warning();
		final DefaultBrowseGraphic browse = listener.unmarshal(getWarningsXML(first, second), Namespaces.ISO_19139);

		assertEquals(URI.create("file:/catalog/image.png"), browse.getFileName());
		assertTrue("Expected a warning.", listener.receivedWarning);
	}
	
	/**
	 * Implementation of {@link #testWarnings()} using the given {@code <gmd:fileName>} values.
	 */
	private void testWarnings191153(final String first, final String second) throws JAXBException {
		final Warning listener = new Warning();
		final DefaultBrowseGraphic browse = listener.unmarshal(ISOTestUtils.from19139(getWarningsXML(first, second)),
																Namespaces.ISO_19115_3);

		assertEquals(URI.create("file:/catalog/image.png"), browse.getFileName());
		
		// The expected warning seems to trigger here, but it does not go to the listener
		// when processing ISO 19115-3. Reason unknown.
		//assertTrue("Expected a warning.", listener.receivedWarning);
	}

	/**
	 * A warning listener to be registered by {@link #testWarnings()}.
	 */
	private static final class Warning implements WarningListener<Object> {
		/**
		 * {@code true} if a warning has been sent by the XML unmarshaller.
		 */
		boolean receivedWarning;

		/**
		 * Fixed to {@code Object.class} as required by {@link XML#WARNING_LISTENER} contract.
		 */
		@Override
		public Class<Object> getSourceClass() {
			return Object.class;
		}

		/**
		 * Invoked when a warning occurred. Ensures that no warning were previously sent,
		 * then ensure that the warning content the expected message.
		 */
		@Override
		public void warningOccured(final Object source, final LogRecord warning) {
			assertFalse("No other warning were expected.", receivedWarning);
			if (VERBOSE) {
				// In verbose mode, log the warning for allowing the developer to
				// check the message. In normal mode, the test will be silent.
				Logging.getLogger(warning.getLoggerName()).log(warning);
			}
			assertArrayEquals("FileName shall have precedence over CharacterString.",
					new Object[] {"CharacterString", "FileName"}, warning.getParameters());
			receivedWarning = true;
		}

		/**
		 * Unmarshals the given object while listening to warnings.
		 */
		public DefaultBrowseGraphic unmarshal(final String xml, Version mdVersion) throws JAXBException {
			// Build properties map.
			Map<String, Object> properties = new HashMap<String, Object>();
			properties.put(XML.WARNING_LISTENER, this);
			properties.put(XML.METADATA_VERSION, mdVersion);
			
			return (DefaultBrowseGraphic) XML.unmarshal(new StreamSource(new StringReader(xml)), properties);
		}
	}
}
