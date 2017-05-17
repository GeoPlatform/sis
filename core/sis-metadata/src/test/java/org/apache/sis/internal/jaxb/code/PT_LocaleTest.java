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
import static org.junit.Assert.assertArrayEquals;

import java.util.Arrays;
import java.util.Locale;

import javax.xml.bind.JAXBException;

import org.apache.sis.internal.jaxb.LegacyNamespaces;
import org.apache.sis.internal.jaxb.Schemas;
import org.apache.sis.metadata.iso.DefaultMetadata;
import org.apache.sis.test.ISOTestUtils;
import org.apache.sis.test.XMLTestCase;
import org.apache.sis.xml.Namespaces;
import org.junit.Test;


/**
 * Tests the XML marshaling of {@link PT_Locale}.
 *
 * @author  Martin Desruisseaux (Geomatys)
 * @author  Cullen Rombach		(Image Matters)
 * @since   0.4
 * @version 0.8
 * @module
 */
public final strictfp class PT_LocaleTest extends XMLTestCase {
    /**
     * The path to the {@code gmxCodelists.xml} path.
     */
    private static final String CODELISTS_PATH = Schemas.METADATA_ROOT_19139 + Schemas.CODELISTS_PATH_19139;

    /**
     * The {@code <gmd:characterEncoding>} element to be repeated for every locale. This element is not
     * of interest for this test. In current Apache SIS implementation, it is totally redundant with the
     * encoding declared in the XML header. Unfortunately those elements are mandatory according OGC/ISO
     * schemas, so we have to carry their weight.
     */
    private static final String ENCODING =
            "      <gmd:characterEncoding>\n" +
            "        <gmd:MD_CharacterSetCode codeList=\"" + CODELISTS_PATH + "#MD_CharacterSetCode\" codeListValue=\"utf8\">UTF-8</gmd:MD_CharacterSetCode>\n" +
            "      </gmd:characterEncoding>\n";

    /**
     * The locales to use for the tests. For better test coverage we need at least:
     *
     * <ul>
     *   <li>One locale which is a language without specifying the country</li>
     *   <li>At least two different countries for the same language.</li>
     * </ul>
     */
    private static final Locale[] LOCALES = {
            Locale.ENGLISH, Locale.JAPANESE, Locale.CANADA, Locale.FRANCE, Locale.CANADA_FRENCH
    };

    /**
     * XML representation of the {@link #LOCALES} list.
     */
    private static final String XML =
            "<gmd:MD_Metadata xmlns:gmd=\"" + Namespaces.GMD + "\" xmlns:gco=\"" + LegacyNamespaces.GCO + "\">\n" +
            "  <gmd:language>\n" +
            "    <gco:CharacterString>eng;</gco:CharacterString>\n" +
            "  </gmd:language>\n" +
            "  <gmd:locale>\n" +
            "    <gmd:PT_Locale>\n" +
            "      <gmd:languageCode>\n" +
            "        <gmd:LanguageCode codeList=\"" + CODELISTS_PATH + "#LanguageCode\" codeListValue=\"jpn\" codeSpace=\"eng\">Japanese</gmd:LanguageCode>\n" +
            "      </gmd:languageCode>\n" + ENCODING +
            "    </gmd:PT_Locale>\n" +
            "  </gmd:locale>\n" +
            "  <gmd:locale>\n" +
            "    <gmd:PT_Locale>\n" +
            "      <gmd:languageCode>\n" +
            "        <gmd:LanguageCode codeList=\"" + CODELISTS_PATH + "#LanguageCode\" codeListValue=\"eng\" codeSpace=\"eng\">English</gmd:LanguageCode>\n" +
            "      </gmd:languageCode>\n" +
            "      <gmd:country>\n" +
            "        <gmd:Country codeList=\"" + CODELISTS_PATH + "#Country\" codeListValue=\"CA\" codeSpace=\"eng\">Canada</gmd:Country>\n" +
            "      </gmd:country>\n" + ENCODING +
            "    </gmd:PT_Locale>\n" +
            "  </gmd:locale>\n" +
            "  <gmd:locale>\n" +
            "    <gmd:PT_Locale>\n" +
            "      <gmd:languageCode>\n" +
            "        <gmd:LanguageCode codeList=\"" + CODELISTS_PATH + "#LanguageCode\" codeListValue=\"fra\" codeSpace=\"eng\">French</gmd:LanguageCode>\n" +
            "      </gmd:languageCode>\n" +
            "      <gmd:country>\n" +
            "        <gmd:Country codeList=\"" + CODELISTS_PATH + "#Country\" codeListValue=\"FR\" codeSpace=\"eng\">France</gmd:Country>\n" +
            "      </gmd:country>\n" + ENCODING +
            "    </gmd:PT_Locale>\n" +
            "  </gmd:locale>\n" +
            "  <gmd:locale>\n" +
            "    <gmd:PT_Locale>\n" +
            "      <gmd:languageCode>\n" +
            "        <gmd:LanguageCode codeList=\"" + CODELISTS_PATH + "#LanguageCode\" codeListValue=\"fra\" codeSpace=\"eng\">French</gmd:LanguageCode>\n" +
            "      </gmd:languageCode>\n" +
            "      <gmd:country>\n" +
            "        <gmd:Country codeList=\"" + CODELISTS_PATH + "#Country\" codeListValue=\"CA\" codeSpace=\"eng\">Canada</gmd:Country>\n" +
            "      </gmd:country>\n" + ENCODING +
            "    </gmd:PT_Locale>\n" +
            "  </gmd:locale>\n" +
            "</gmd:MD_Metadata>";

    /**
     * Tests marshalling of a few locales for ISO 19139.
     *
     * @throws JAXBException Should never happen.
     */
    @Test
    public void testMarshalling19139() throws JAXBException {
        final DefaultMetadata metadata = new DefaultMetadata();
        metadata.setLanguages(Arrays.asList(LOCALES));
        assertXmlEquals(XML, marshal(metadata), "xlmns:*");
    }

    /**
     * Tests unmarshalling of a few locales for ISO 19139.
     *
     * @throws JAXBException Should never happen.
     */
    @Test
    public void testUnmarshalling19139() throws JAXBException {
        final DefaultMetadata metadata = unmarshal(DefaultMetadata.class, XML);
        assertArrayEquals(LOCALES, metadata.getLanguages().toArray());
    }
    
    /**
     * Tests marshalling of a few locales for ISO 19115-3.
     *
     * @throws JAXBException Should never happen.
     */
    @Test
    public void testMarshalling191153() throws JAXBException {
        final DefaultMetadata metadata = new DefaultMetadata();
        metadata.setLanguages(Arrays.asList(LOCALES));
        assertXmlEquals(ISOTestUtils.from19139(XML), marshal(metadata, Namespaces.ISO_19115_3), "xlmns:*");
    }
    
    /**
     * Tests unmarshalling of a few locales for ISO 19115-3.
     *
     * @throws JAXBException Should never happen.
     */
    @Test
    public void testUnmarshalling191153() throws JAXBException {
        final DefaultMetadata metadata = unmarshal(DefaultMetadata.class, ISOTestUtils.from19139(XML), Namespaces.ISO_19115_3);
        assertArrayEquals(LOCALES, metadata.getLanguages().toArray());
    }
    
    
}
