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
package org.apache.sis.internal.jaxb.metadata.replace;

import static org.apache.sis.test.Assert.assertXmlEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.xml.bind.JAXBException;

import org.apache.sis.internal.jaxb.LegacyNamespaces;
import org.apache.sis.test.ISOTestUtils;
import org.apache.sis.test.XMLTestCase;
import org.apache.sis.util.iso.Names;
import org.apache.sis.xml.Namespaces;
import org.junit.Test;
import org.opengis.parameter.ParameterDirection;
import org.opengis.util.MemberName;


/**
 * Tests {@link ServiceParameter}.
 *
 * @author  Martin Desruisseaux (Geomatys)
 * @author  Cullen Rombach		(Image Matters)
 * @since   0.5
 * @version 0.8
 * @module
 */
public final strictfp class ServiceParameterTest extends XMLTestCase {
	/**
	 * Creates the parameter to use for testing purpose.
	 *
	 * @return The test parameter.
	 */
	public static ServiceParameter create() {
		final MemberName paramName = Names.createMemberName(null, null, "Version", String.class);
		final ServiceParameter param = new ServiceParameter();
		param.memberName    = paramName;
		param.optionality   = true;
		param.repeatability = false;
		param.direction     = ParameterDirection.IN;
		return param;
	}

	/**
	 * Tests {@link ServiceParameter#getValueType()} and {@link ServiceParameter#getValueClass()}.
	 */
	@Test
	public void testGetValueType() {
		final ServiceParameter param = create();
		assertEquals("valueType", "OGC:CharacterString", param.getValueType().toFullyQualifiedName().toString());
		assertEquals("valueClass", String.class, param.getValueClass());
	}

	/**
	 * Tests {@link ServiceParameter#getOptionality()} and {@link ServiceParameter#setOptionality(String)}.
	 */
	@Test
	public void testOptionalityLabel() {
		final ServiceParameter param = create();
		assertEquals("Optional", param.getOptionality());

		param.optionality = false;
		assertEquals("Mandatory", param.getOptionality());

		param.setOptionality("Optional");
		assertTrue(param.optionality);

		param.setOptionality("Mandatory");
		assertFalse(param.optionality);
	}

	/**
	 * Tests marshalling of an almost empty parameter (except for default mandatory values).
	 * The main purpose is to ensure that the XML does not contains spurious elements like
	 * empty enumeration wrapper. ISO 19139.
	 *
	 * @throws JAXBException if an error occurred during marshalling.
	 */
	@Test
	public void testMarshalEmpty19139() throws JAXBException {
		final String xml = marshal(new ServiceParameter());
		assertXmlEquals(getServiceParameterXML(), xml, "xlmns:*");
	}

	/**
	 * Tests marshalling of an almost empty parameter (except for default mandatory values).
	 * The main purpose is to ensure that the XML does not contains spurious elements like
	 * empty enumeration wrapper. ISO 19115-3.
	 *
	 * @throws JAXBException if an error occurred during marshalling.
	 */
	@Test
	public void testMarshalEmpty191153() throws JAXBException {
		final String xml = marshal(new ServiceParameter(), Namespaces.ISO_19115_3);
		assertXmlEquals(ISOTestUtils.from19139(getServiceParameterXML()), xml, "xlmns:*");
	}

	/**
	 * @return A string representing the XML encoding of a Service Parameter in ISO 19139 format.
	 */
	private static String getServiceParameterXML() {
		return "<srv:SV_Parameter xmlns:srv=\"" + LegacyNamespaces.SRV + '"' +
				" xmlns:gco=\"" + LegacyNamespaces.GCO + "\">\n" +
				"  <srv:optionality>\n" +
				"    <gco:CharacterString>Mandatory</gco:CharacterString>\n" +
				"  </srv:optionality>\n" +
				"  <srv:repeatability>\n" +
				"    <gco:Boolean>false</gco:Boolean>\n" +
				"  </srv:repeatability>\n" +
				"</srv:SV_Parameter>\n";
	}
}
