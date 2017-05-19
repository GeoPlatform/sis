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
package org.apache.sis.internal.jaxb.gmd;

import static org.apache.sis.internal.util.StandardDateFormat.UTC;
import static org.apache.sis.test.Assert.assertXmlEquals;
import static org.apache.sis.test.TestUtilities.getSingleton;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.sis.internal.jaxb.LegacyNamespaces;
import org.apache.sis.internal.jaxb.Schemas;
import org.apache.sis.test.DependsOnMethod;
import org.apache.sis.test.ISOTestUtils;
import org.apache.sis.test.XMLTestCase;
import org.apache.sis.test.mock.MetadataMock;
import org.apache.sis.xml.MarshallerPool;
import org.apache.sis.xml.Namespaces;
import org.apache.sis.xml.XML;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.metadata.Metadata;


/**
 * Tests the XML marshaling of {@code Locale} when used for a language.
 *
 * @author  Martin Desruisseaux (Geomatys)
 * @author  Cullen Rombach		(Image Matters)
 * @since   0.3
 * @version 0.8
 * @module
 */
public final strictfp class LanguageCodeTest extends XMLTestCase {
    /**
     * XML fragment using the {@code <gco:CharacterString>} construct.
     */
    private static final String CHARACTER_STRING = "<gco:CharacterString>jpn</gco:CharacterString>";

    /**
     * XML fragment using the {@code <gmd:LanguageCode>} construct without attributes.
     */
    private static final String LANGUAGE_CODE_WITHOUT_ATTRIBUTE = "<gmd:LanguageCode>jpn</gmd:LanguageCode>";

    /**
     * XML fragment using the {@code <gmd:LanguageCode>} construct with attributes.
     */
    private static final String LANGUAGE_CODE = "<gmd:LanguageCode" +
            " codeList=\"" + Schemas.METADATA_ROOT_19139 + Schemas.CODELISTS_PATH_19139 + "#LanguageCode\"" +
            " codeListValue=\"jpn\">Japanese</gmd:LanguageCode>";

    /**
     * A poll of configured {@link Marshaller} and {@link Unmarshaller}, created when first needed.
     */
    private static MarshallerPool pool;

    /**
     * Creates the XML (un)marshaller pool to be shared by all test methods.
     * The (un)marshallers locale and timezone will be set to fixed values.
     *
     * @throws JAXBException If an error occurred while creating the pool.
     *
     * @see #disposeMarshallerPool()
     */
    @BeforeClass
    public static void createMarshallerPool() throws JAXBException {
        final Map<String,Object> properties = new HashMap<>(4);
        assertNull(properties.put(XML.LOCALE, Locale.UK));
        assertNull(properties.put(XML.TIMEZONE, UTC));
        pool = new MarshallerPool(JAXBContext.newInstance(MetadataMock.class), properties);
    }

    /**
     * Invoked by JUnit after the execution of every tests in order to dispose
     * the {@link MarshallerPool} instance used internally by this class.
     */
    @AfterClass
    public static void disposeMarshallerPool() {
        pool = null;
    }

    /**
     * Returns the XML of a metadata element. This method returns a string like below,
     * where the {@code ${languageCode}} string is replaced by the given argument.
     *
     * {@preformat xml
     *   <gmd:MD_Metadata>
     *     <gmd:language>
     *       ${languageCode}
     *     </gmd:language>
     *   </gmd:MD_Metadata>
     * }
     *
     * @param languageCode The XML fragment to write inside the {@code <gmd:language>} element.
     */
    private static String getMetadataXML(final String languageCode) {
        return "<gmd:MD_Metadata" +
               " xmlns:gmd=\"" + Namespaces.GMD + '"' +
               " xmlns:gco=\"" + LegacyNamespaces.GCO + "\">\n" +
               "  <gmd:language>\n" +
               "    " + languageCode + '\n' +
               "  </gmd:language>\n" +
               "</gmd:MD_Metadata>";
    }

    /**
     * Tests marshalling of {@code <gmd:LanguageCode>}.
     * The result shall be as documented in {@link #testLanguageCode19139()}.
     * ISO 19139.
     *
     * @throws JAXBException Should never happen.
     *
     * @see #testMarshallCharacterString19139()
     */
    @Test
    public void testMarshallLanguageCode19139() throws JAXBException {
        final MetadataMock metadata = new MetadataMock(Locale.JAPANESE);
        final Marshaller marshaller = pool.acquireMarshaller();
        assertNull(marshaller.getProperty(XML.STRING_SUBSTITUTES));
        assertXmlEquals(getMetadataXML(LANGUAGE_CODE), marshal(marshaller, metadata), "xmlns:*");
        pool.recycle(marshaller);
    }
    
    /**
     * Tests marshalling of {@code <gmd:LanguageCode>}.
     * The result shall be as documented in {@link #testLanguageCode191153()}.
     * ISO 191153.
     *
     * @throws JAXBException Should never happen.
     *
     * @see #testMarshallCharacterString191153()
     */
    @Test
    public void testMarshallLanguageCode191153() throws JAXBException {
        final MetadataMock metadata = new MetadataMock(Locale.JAPANESE);
        final Marshaller marshaller = pool.acquireMarshaller();
        final Unmarshaller unmarshaller = pool.acquireUnmarshaller();
        assertNull(marshaller.getProperty(XML.STRING_SUBSTITUTES));
        assertXmlEquals(ISOTestUtils.from19139(getMetadataXML(LANGUAGE_CODE), unmarshaller, marshaller),
        		marshal(marshaller, metadata, Namespaces.ISO_19115_3), "xmlns:*");
        pool.recycle(unmarshaller);
        pool.recycle(marshaller);
    }

    /**
     * Tests the unmarshalling using the {@code <gmd:LanguageCode>} construct. XML fragment:
     *
     * {@preformat xml
     *   <gmd:MD_Metadata>
     *     <gmd:language>
     *       <gmd:LanguageCode codeList="(snip)/gmxCodelists.xml#LanguageCode" codeListValue="jpn">Japanese</gmd:LanguageCode>
     *     </gmd:language>
     *   </gmd:MD_Metadata>
     * }
     *
     * @throws JAXBException Should never happen.
     *
     * @see #testMarshallLanguageCode()
     */
    @Test
    public void testLanguageCode19139() throws JAXBException {
        final Unmarshaller unmarshaller = pool.acquireUnmarshaller();
        final String xml = getMetadataXML(LANGUAGE_CODE);
        final Metadata metadata = (Metadata) unmarshal(unmarshaller, xml);
        assertEquals(Locale.JAPANESE, getSingleton(metadata.getLanguages()));
        pool.recycle(unmarshaller);
    }
    
    /**
     * @see testLanguageCode19139(). This is the ISO 19115-3 version.
     * @throws JAXBException
     */
    @Test
    public void testLanguageCode191153() throws JAXBException {
        final Unmarshaller unmarshaller = pool.acquireUnmarshaller();
        final Marshaller marshaller = pool.acquireMarshaller();
        final String xml = ISOTestUtils.from19139(getMetadataXML(LANGUAGE_CODE), unmarshaller, marshaller);
        final Metadata metadata = (Metadata) unmarshal(unmarshaller, xml, Namespaces.ISO_19115_3);
        assertEquals(Locale.JAPANESE, getSingleton(metadata.getLanguages()));
        pool.recycle(unmarshaller);
        pool.recycle(marshaller);
    }

    /**
     * Tests the unmarshalling using the {@code <gmd:LanguageCode>} construct without attributes.
     * The adapter is expected to parse the element value. XML fragment:
     *
     * {@preformat xml
     *   <gmd:MD_Metadata>
     *     <gmd:language>
     *       <gmd:LanguageCode>jpn</gmd:LanguageCode>
     *     </gmd:language>
     *   </gmd:MD_Metadata>
     * }
     *
     * @throws JAXBException Should never happen.
     */
    @Test
    @DependsOnMethod("testLanguageCode19139")
    public void testLanguageCodeWithoutAttributes19139() throws JAXBException {
        final Unmarshaller unmarshaller = pool.acquireUnmarshaller();
        final String xml = getMetadataXML(LANGUAGE_CODE_WITHOUT_ATTRIBUTE);
        final Metadata metadata = (Metadata) unmarshal(unmarshaller, xml);
        assertEquals(Locale.JAPANESE, getSingleton(metadata.getLanguages()));
        pool.recycle(unmarshaller);
    }
    
    /**
     * @see testLanguageCodeWithoutAttributes19139(). This is the ISO 19115-3 version.
     * @throws JAXBException
     */
    @Test
    @DependsOnMethod("testLanguageCode191153")
    public void testLanguageCodeWithoutAttributes191153() throws JAXBException {
        final Unmarshaller unmarshaller = pool.acquireUnmarshaller();
        final Marshaller marshaller = pool.acquireMarshaller();
        final String xml = ISOTestUtils.from19139(getMetadataXML(LANGUAGE_CODE_WITHOUT_ATTRIBUTE), unmarshaller, marshaller);
        final Metadata metadata = (Metadata) unmarshal(unmarshaller, xml, Namespaces.ISO_19115_3);
        assertEquals(Locale.JAPANESE, getSingleton(metadata.getLanguages()));
        pool.recycle(unmarshaller);
        pool.recycle(marshaller);
    }

    /**
     * Tests marshalling of {@code <gco:CharacterString>}, which require explicit marshaller configuration.
     * The result shall be as documented in {@link #testCharacterString()}.
     *
     * @throws JAXBException Should never happen.
     *
     * @see #testMarshallLanguageCode()
     */
    @Test
    public void testMarshallCharacterString19139() throws JAXBException {
        final MetadataMock metadata = new MetadataMock(Locale.JAPANESE);
        final Marshaller marshaller = pool.acquireMarshaller();
        marshaller.setProperty(XML.STRING_SUBSTITUTES, new String[] {"dummy","language","foo"});
        assertArrayEquals(new String[] {"language"}, (String[]) marshaller.getProperty(XML.STRING_SUBSTITUTES));
        assertXmlEquals(getMetadataXML(CHARACTER_STRING), marshal(marshaller, metadata), "xmlns:*");
        pool.recycle(marshaller);
    }
    
    /**
     * @see testMarshallCharacterString19139(). This is the ISO 19115-3 version.
     * @throws JAXBException
     */
    @Test
    public void testMarshallCharacterString191153() throws JAXBException {
        final MetadataMock metadata = new MetadataMock(Locale.JAPANESE);
        final Marshaller marshaller = pool.acquireMarshaller();
        final Unmarshaller unmarshaller = pool.acquireUnmarshaller();
        marshaller.setProperty(XML.STRING_SUBSTITUTES, new String[] {"dummy","language","foo"});
        assertArrayEquals(new String[] {"language"}, (String[]) marshaller.getProperty(XML.STRING_SUBSTITUTES));
        String xml = ISOTestUtils.from19139(getMetadataXML(CHARACTER_STRING), unmarshaller, marshaller);
        assertXmlEquals(xml, marshal(marshaller, metadata, Namespaces.ISO_19115_3), "xmlns:*");
        pool.recycle(marshaller);
        pool.recycle(unmarshaller);
    }

    /**
     * Tests the unmarshalling of an XML using the {@code gco:CharacterString} construct.
     * XML fragment:
     *
     * {@preformat xml
     *   <gmd:MD_Metadata>
     *     <gmd:language>
     *       <gco:CharacterString>jpn</gco:CharacterString>
     *     </gmd:language>
     *   </gmd:MD_Metadata>
     * }
     *
     * @throws JAXBException Should never happen.
     */
    @Test
    public void testCharacterString19139() throws JAXBException {
        final Unmarshaller unmarshaller = pool.acquireUnmarshaller();
        final String xml = getMetadataXML(CHARACTER_STRING);
        final Metadata metadata = (Metadata) unmarshal(unmarshaller, xml);
        assertEquals(Locale.JAPANESE, getSingleton(metadata.getLanguages()));
        pool.recycle(unmarshaller);
    }
    
    /**
     * @see testCharacterString19139(). This is the ISO 19115-3 version.
     * @throws JAXBException
     */
    @Test
    public void testCharacterString191153() throws JAXBException {
        final Unmarshaller unmarshaller = pool.acquireUnmarshaller();
        final Marshaller marshaller = pool.acquireMarshaller();
        final String xml = ISOTestUtils.from19139(getMetadataXML(CHARACTER_STRING), unmarshaller, marshaller);
        final Metadata metadata = (Metadata) unmarshal(unmarshaller, xml, Namespaces.ISO_19115_3);
        assertEquals(Locale.JAPANESE, getSingleton(metadata.getLanguages()));
        pool.recycle(unmarshaller);
        pool.recycle(marshaller);
    }
}
