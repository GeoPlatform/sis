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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * Uses the XSLT files provided by ISO to convert XML snippets used for testing
 * from ISO 19139 to ISO 19115-3. This was done to save time when creating unit
 * tests for the ISO 19115-3 implementation.
 *
 * @author  Cullen Rombach		(Image Matters)
 * @since   0.8
 * @version 0.8
 * @module
 */
public class ISOConverter {

	private static Transformer to191153_Transformer;

	/**
	 * Default constructor to set up XSL transformer.
	 */
	public ISOConverter() {
		try {
			// Create transformer factory.
			TransformerFactory factory = new net.sf.saxon.TransformerFactoryImpl();

			// Load the XSL file detailing the transformation from ISO 19139 to ISO 19115-3.
			URL from19139url = this.getClass().getResource("transforms/fromISO19139.xsl");
			Source from19139source = new StreamSource(from19139url.openStream(),
													  from19139url.toExternalForm());

			// Create the transformer.
			to191153_Transformer = factory.newTransformer(from19139source);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** Convert the given ISO 19139 XML string to an ISO 19115-3 XML string.
	 * @param xml19139 A snippet of ISO 19139 XML as a string.
	 * @return The ISO 19115-3 XML equivalent of the input XML as a string.
	 */
	public String to191153(String xml19139) {
		try {
			// Prepare the input string.
			Source source = new StreamSource(new ByteArrayInputStream(
					xml19139.getBytes(StandardCharsets.UTF_8)));

			// Prepare the output writer.
			StringWriter outWriter = new StringWriter();
			Result result = new StreamResult(outWriter);

			// Apply the XSLT to the source file and write the result
			// to the output string.
			to191153_Transformer.transform(source, result);
			StringBuffer stringBuffer = outWriter.getBuffer();

			// Return the final output.
			return stringBuffer.toString();
			
		} catch (TransformerException e) {
			// An error occurred while applying the XSL file.
			// Get some information for debugging.
			SourceLocator locator = e.getLocator();
			int col = locator.getColumnNumber();
			int line = locator.getLineNumber();
			String publicId = locator.getPublicId();
			String systemId = locator.getSystemId();
		}

		// Return null if there was an exception.
		return null;
	}

}
