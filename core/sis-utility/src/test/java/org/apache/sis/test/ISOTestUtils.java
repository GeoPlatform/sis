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
package org.apache.sis.test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.sis.util.Static;
import org.apache.sis.xml.Namespaces;
import org.apache.sis.xml.XML;

/**
 * Utility class used in testing to convert ISO 19139 XML metadata to 
 * ISO 19115-3 XML metadata, and vice versa.
 *
 * @author  Cullen Rombach		(Image Matters)
 * @since   0.8
 * @version 0.8
 * @module
 */
public final class ISOTestUtils extends Static {

	/**
	 * Private constructor to avoid unnecessary instantiation.
	 */
	private ISOTestUtils() {
		// Do nothing.
	}

	/** Convert the given ISO 19139 XML string to an ISO 19115-3 XML string.
	 * @param xml19139 A snippet of ISO 19139 XML as a string.
	 * @return The ISO 19115-3 XML equivalent of the input XML as a string.
	 */
	public static String from19139(String xml19139){
		try {
			// Unmarshal the ISO 19139 XML to a Java object.
			Object metadata = XML.unmarshal(xml19139);
			
			// Marshal the Java object to ISO 19115-3 XML.
			final String xml191153 = XML.marshal(metadata, Namespaces.ISO_19115_3);
			
			// Return the result.
			return xml191153;
			
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	/** Convert the given ISO 19139 XML string to an ISO 19115-3 XML string using the given Marshaller.
	 * This is primarily used to preserve properties set on the Marshaller, such as a Locale.
	 * @param xml19139 A snippet of ISO 19139 XML as a string.
	 * @param marshaller The Marshaller to use.
	 * @return The ISO 19115-3 XML equivalent of the input XML as a string.
	 */
	public static String from19139(String xml19139, Marshaller marshaller){
		try {
			// Unmarshal the ISO 19139 XML to a Java object.
			Object metadata = XML.unmarshal(xml19139);
			
			// Marshal the Java object to ISO 19115-3 XML.
	        StringWriter buffer = new StringWriter();
	        buffer.getBuffer().setLength(0);
	        marshaller.setProperty(XML.METADATA_VERSION, Namespaces.ISO_19115_3);
	        marshaller.marshal(metadata, buffer);
			final String xml191153 = buffer.toString();
			
			// Return the result.
			return xml191153;
			
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	/** Convert the given ISO 19139 XML string to an ISO 19115-3 XML string using the given Marshaller
	 * and Unmarshaller.
	 * This is primarily used to preserve properties set on the (Un)Marshaller, such as a Locale.
	 * @param xml19139 A snippet of ISO 19139 XML as a string.
	 * @param marshaller The Marshaller to use.
	 * @param unmarshaller The Unmarshaller to use.
	 * @return The ISO 19115-3 XML equivalent of the input XML as a string.
	 */
	public static String from19139(String xml19139,  Unmarshaller unmarshaller, Marshaller marshaller){
		try {
			// Unmarshal the ISO 19139 XML to a Java object.
			InputStream is = new ByteArrayInputStream(xml19139.getBytes());
			unmarshaller.setProperty(XML.METADATA_VERSION, Namespaces.ISO_19139);
			Object metadata = unmarshaller.unmarshal(is);
			
			// Marshal the Java object to ISO 19115-3 XML.
	        StringWriter buffer = new StringWriter();
	        buffer.getBuffer().setLength(0);
	        marshaller.setProperty(XML.METADATA_VERSION, Namespaces.ISO_19115_3);
	        marshaller.marshal(metadata, buffer);
			final String xml191153 = buffer.toString();
			
			// Return the result.
			return xml191153;
			
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	/** Convert the given ISO 19115-3 XML string to an ISO 19139 XML string.
	 * @param xml191153 A snippet of ISO 19115-3 XML as a string.
	 * @return The ISO 19139 XML equivalent of the input XML as a string.
	 */
	public static String to19139(String xml191153){
		try {
			// Unmarshal the ISO 19139 XML to a Java object.
			Object metadata = XML.unmarshal(xml191153, Namespaces.ISO_19115_3);
			
			// Marshal the Java object to ISO 19115-3 XML.
			final String xml19139 = XML.marshal(metadata);
			
			// Return the result.
			return xml19139;
			
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

}
