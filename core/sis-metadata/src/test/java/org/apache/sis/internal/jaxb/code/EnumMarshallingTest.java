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

import static org.apache.sis.test.Assert.assertSetEquals;
import static org.apache.sis.test.Assert.assertXmlEquals;
import static org.opengis.test.Assert.assertInstanceOf;

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;

import javax.xml.bind.JAXBException;

import org.apache.sis.metadata.iso.identification.DefaultDataIdentification;
import org.apache.sis.test.ISOTestUtils;
import org.apache.sis.test.XMLTestCase;
import org.apache.sis.xml.Namespaces;
import org.junit.Test;
import org.opengis.metadata.identification.TopicCategory;


/**
 * Tests the XML marshaling of {@code Enum}.
 *
 * @author  Martin Desruisseaux (Geomatys)
 * @author  Cullen Rombach		(Image Matters)
 * @since   0.5
 * @version 0.8
 * @module
 */
public final strictfp class EnumMarshallingTest extends XMLTestCase {
    /**
     * Tests (un)marshaling of an enumeration in ISO 19139.
     *
     * @throws JAXBException If an error occurred while (un)marshaling the XML.
     */
    @Test
    public void testTopicCategories19139() throws JAXBException {
        final Collection<TopicCategory> expected = Arrays.asList(
                TopicCategory.OCEANS,
                TopicCategory.ENVIRONMENT,
                TopicCategory.IMAGERY_BASE_MAPS_EARTH_COVER);   // We need to test at least one enum with many words.

        final DefaultDataIdentification id = new DefaultDataIdentification();
        id.setTopicCategories(expected);

        final String xml = marshal(id);
        assertXmlEquals(
                "<gmd:MD_DataIdentification xmlns:gmd=\"" + Namespaces.GMD + "\">\n" +
                "  <gmd:topicCategory>\n" +
                "    <gmd:MD_TopicCategoryCode>environment</gmd:MD_TopicCategoryCode>\n" +
                "  </gmd:topicCategory>\n" +
                "  <gmd:topicCategory>\n" +
                "    <gmd:MD_TopicCategoryCode>imageryBaseMapsEarthCover</gmd:MD_TopicCategoryCode>\n" +
                "  </gmd:topicCategory>\n" +
                "  <gmd:topicCategory>\n" +
                "    <gmd:MD_TopicCategoryCode>oceans</gmd:MD_TopicCategoryCode>\n" +
                "  </gmd:topicCategory>\n" +
                "</gmd:MD_DataIdentification>",
                xml, "xmlns:*");
        /*
         * Unmarshall the above XML and verify that we find all the topic categories.
         */
        final Collection<TopicCategory> unmarshalled = unmarshal(DefaultDataIdentification.class, xml).getTopicCategories();
        assertInstanceOf("topicCategory", EnumSet.class, unmarshalled);
        assertSetEquals(expected, unmarshalled);
    }
    
    /**
     * Tests (un)marshaling of an enumeration in ISO 19115-3.
     *
     * @throws JAXBException If an error occurred while (un)marshaling the XML.
     */
    @Test
    public void testTopicCategories191153() throws JAXBException {
        final Collection<TopicCategory> expected = Arrays.asList(
                TopicCategory.OCEANS,
                TopicCategory.ENVIRONMENT,
                TopicCategory.IMAGERY_BASE_MAPS_EARTH_COVER);   // We need to test at least one enum with many words.

        final DefaultDataIdentification id = new DefaultDataIdentification();
        id.setTopicCategories(expected);

        final String xml = marshal(id, Namespaces.ISO_19115_3);
        String xml19139 = "<gmd:MD_DataIdentification xmlns:gmd=\"" + Namespaces.GMD + "\">\n" +
                "  <gmd:topicCategory>\n" +
                "    <gmd:MD_TopicCategoryCode>environment</gmd:MD_TopicCategoryCode>\n" +
                "  </gmd:topicCategory>\n" +
                "  <gmd:topicCategory>\n" +
                "    <gmd:MD_TopicCategoryCode>imageryBaseMapsEarthCover</gmd:MD_TopicCategoryCode>\n" +
                "  </gmd:topicCategory>\n" +
                "  <gmd:topicCategory>\n" +
                "    <gmd:MD_TopicCategoryCode>oceans</gmd:MD_TopicCategoryCode>\n" +
                "  </gmd:topicCategory>\n" +
                "</gmd:MD_DataIdentification>";
        assertXmlEquals(ISOTestUtils.from19139(xml19139), xml, "xmlns:*");
        
        /*
         * Unmarshall the above XML and verify that we find all the topic categories.
         */
        final Collection<TopicCategory> unmarshalled = unmarshal(DefaultDataIdentification.class, xml, Namespaces.ISO_19115_3).getTopicCategories();
        assertInstanceOf("topicCategory", EnumSet.class, unmarshalled);
        assertSetEquals(expected, unmarshalled);
    }
}
