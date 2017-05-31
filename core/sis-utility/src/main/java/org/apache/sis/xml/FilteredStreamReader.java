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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.bind.DatatypeConverter;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.util.StreamReaderDelegate;


/**
 * A filter replacing the namespaces found in a XML document by the namespaces expected by SIS at unmarshalling time.
 * This class forwards every method calls to the wrapped {@link XMLStreamReader}, with all {@code namespaceURI}
 * arguments filtered before being delegated.
 *
 * See {@link FilteredNamespaces} for more information.
 *
 * @author  Martin Desruisseaux (Geomatys)
 * @author  Cullen Rombach		(Image Matters)
 * @since   0.4
 * @version 0.8
 * @module
 */
final class FilteredStreamReader extends StreamReaderDelegate {
	/**
	 * The other version to unmarshal from.
	 */
	private final FilterVersion version;

	/**
	 * Map of element names to namespaces. Used when reading ISO 19139.
	 * 
	 * How data is stored internally in the map:
	 * 
	 * key: ElementName|ParentElementName
	 * value: NamespaceURI
	 * 
	 * If an element does not have a parent (i.e. it is its own Java class)
	 * then the format is as follows:
	 * 
	 * key: ElementName
	 * value: NamespaceURI
	 */
	private Map<String,String> elementNamespaceMap;

	/**
	 * Map of elements that need to have their name changed.
	 * For example, gmd:URL needs to be changed to gco:CharacterString for reading ISO 19139.
	 * 
	 * key: OldElementName
	 * value: NewElementName
	 */
	private Map<String,String> localNameMap;

	/**
	 * Location of ElementNameSpaceMap
	 */
	private final String MAP_PATH = "/org/apache/sis/internal/jaxb/ElementNamespaceMap.txt";

	/**
	 * List of encountered XML tags, in order. Used for backtracking.
	 */
	private ArrayList<CloseableElement> elements;

	/**
	 * Creates a new filter for the given version of the standards.
	 */
	FilteredStreamReader(final XMLStreamReader in, final FilterVersion version) {
		super(in);
		this.version = version;

		// Initialize map file.
		InputStream inputStream = FilteredStreamReader.class.getResourceAsStream(MAP_PATH);

		// Initialize elements list.
		elements = new ArrayList<CloseableElement>();

		// If reading ISO 19139, need to load ElementNamespaceMap.txt
		// and convert its contents to a map for use in filtered reading.
		if(version.equals(FilterVersion.ISO19139)) {
			try {
				readElementNamespaceMap(inputStream);
			} catch (IOException e) {
				// Do nothing for now. The file should always be there.
				e.printStackTrace();
			}
		}

		// Initialize list of local names that need to be replaced.
		// This is kind of hacky, but it should work for now.
		// Used to circumvent type issues, e.g. change from gmd:URL to gco:CharacterString.
		localNameMap = new TreeMap<String,String>();
		localNameMap.put("URL", "CharacterString");
	}

	/**
	 * Reads the element namespace map text file into a map to be used by this class.
	 * Stores the results in the {@code elementNamespaceMap} Map.
	 * @throws IOException 
	 */
	private void readElementNamespaceMap(InputStream stream) throws IOException {
		// Instantiate the elementNamespaceMap
		elementNamespaceMap = new TreeMap<String, String>();

		// Create a buffered reader.
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));

		// Read the file into the elementNamespaceMap.
		String line = reader.readLine();
		while(line != null) {
			// Get the element name.
			String element = line.substring(0, line.indexOf('|'));
			// Get the parent element name (if there is one).
			String parent = line.substring(line.indexOf('|'), line.lastIndexOf('|'));
			// Get the namespace URI.
			String namespace = line.substring(line.lastIndexOf('|') + 1);

			// Store the values in the map.
			elementNamespaceMap.put(element + parent, namespace);

			// Read the next line.
			line = reader.readLine();
		}

		// Close the reader.
		reader.close();
	}

	/**
	 * Get a collection of possible parents for an element with the given name.
	 * @param element The name of element to get parents for.
	 * @return Collection of possible parents.
	 */
	private Collection<String> getParents(String element) {
		if(element != null && elementNamespaceMap != null) {
			Collection<String> parents = new ArrayList<String>();
			// Loop through the map.
			for(String key : elementNamespaceMap.keySet()) {
				if(key.startsWith(element + "|")) {
					String parent = key.substring(key.indexOf("|") + 1);
					if(!parent.equals("")) {
						parents.add(parent);
					}
				}
			}
			// Return the list of parents.
			return parents.size() == 0 ? null : parents;
		}
		return null;
	}

	/**
	 * Given an element name and parent name, get the mapped ISO 19115-3 namespace.
	 * @param element
	 * @param parent
	 * @return the mapped namespace.
	 */
	private String getNamespace(String element, String parent) {
		if(parent == null) {
			parent = "";
		}
		return elementNamespaceMap == null ? null : elementNamespaceMap.get(element + "|" + parent);
	}

	/**
	 * Returns a boolean indicating whether or not an element with the given name exists
	 * within the elementNamespaceMap. Returns false if elementNamespaceMap is null.
	 * @param name The name of the element.
	 */
	private boolean inMap(String element) {
		if(elementNamespaceMap != null && element != null) {
			// Loop through the map.
			for(String key : elementNamespaceMap.keySet()) {
				if(key.startsWith(element + "|")) {
					return true;
				}
			}
		}
		// If the element wasn't found, return false.
		return false;
	}

	/**
	 * Return the namespace mapped to the given name in the context of the current part of the XML document being read.
	 * @param name The element or attribute name currently being read.
	 * @return The ISO 19115-3 namespace URI for the element or attribute in the current context.
	 */
	private String getMappedNamespace(String name, String previousNamespace) {
		// Store the namespace that will be returned.
		String namespace = previousNamespace;

		if(inMap(name)) {
			// If the element is a root element, return the associated namespace.
			if(getParents(name) == null) {
				namespace = getNamespace(name, null);
			}

			// If the element is not a root element, we need to backtrack until we find
			// the latest possible parent element. Then, we use the namespace associated with that parent.
			else {
				Collection<String> parents = getParents(name);
				for(int ndx = elements.size()-1; ndx >= 0; ndx--) {
					if(elements.get(ndx).isOpen() && parents.contains(elements.get(ndx).getName())) {
						String parentName = elements.get(ndx).getName();
						namespace = getNamespace(name, parentName);
					}
				}
			}
		}

		// Return the calculated namespace.
		return toImpl(namespace).intern();
	}

	/**
	 * Returns true if the element name needs replacement, false otherwise.
	 * @param element
	 */
	private boolean needsNameReplacement(String element) {
		return (element != null && localNameMap != null && localNameMap.get(element) != null);
	}

	/**
	 * Converts a JAXB URI to the URI seen by the consumer of this wrapper.
	 */
	private String toView(final String uri) {
		final String replacement = version.toView.get(uri);
		return (replacement != null) ? replacement : uri;
	}

	/**
	 * Converts a URI read from the XML document to the URI to give to JAXB.
	 */
	private String toImpl(final String uri) {
		final String replacement = version.toImpl.get(uri);
		return (replacement != null) ? replacement : uri;
	}

	/**
	 * Converts a name read from the XML document to the name to give to JAXB.
	 */
	private QName toImpl(QName name) {
		final String namespaceURI = name.getNamespaceURI();
		String replacementURI = name.getNamespaceURI();
		String localName = name.getLocalPart();

		// Check if a hard-coded name replacement is necessary here.
		if(needsNameReplacement(localName)) {
			localName = localNameMap.get(localName);
		}

		// If the local name is in the element-namespace map, get the associated URI in context.
		if(inMap(localName)) {
			replacementURI = getMappedNamespace(localName, name.getNamespaceURI());
		}
		// If it's not in the map, fall back to the simpler toImpl()
		else {
			replacementURI = toImpl(namespaceURI);
		}

		// If the URI has been changed, create a new QName.
		if (!replacementURI.equals(namespaceURI) || !localName.equals(name.getLocalPart())) {
			name = new QName(replacementURI, localName, name.getPrefix());
		}

		// Return the new QName.
		return name;
	}

	/** Replaces the given URI if needed, then forwards the call. */
	@Override
	public void require(final int type, final String namespaceURI, final String localName) throws XMLStreamException {
		if(inMap(localName)) {
			super.require(type, getMappedNamespace(localName, namespaceURI), localName);
		}
		// If the localName isn't in the element-namespace map, fall back to toView().
		else {
			super.require(type, toView(namespaceURI), localName);
		}
	}

	/** Returns the context of the underlying reader wrapped in a filter that converts the namespaces on the fly. */
	@Override
	public NamespaceContext getNamespaceContext() {
		NamespaceContext context = new FilteredNamespaces(super.getNamespaceContext(), version, true);
		return context;
	}

	/** Forwards the call, then replaces the namespace URI if needed. */
	@Override
	public QName getName() {
		return toImpl(super.getName());
	}

	/** Replaces the local name if necessary for mapping purposes */
	@Override
	public String getLocalName() {
		String localName = super.getLocalName();
		if(needsNameReplacement(localName)) {
			return localNameMap.get(localName).intern();
		}
		return localName.intern();
	}

	/** Forwards the call, then replaces the namespace URI if needed. */
	@Override
	public QName getAttributeName(final int index) {
		return toImpl(super.getAttributeName(index));
	}

	@Override
	public int next() throws XMLStreamException {
		if(getEventType() == XMLStreamConstants.START_ELEMENT || getEventType() == XMLStreamConstants.END_ELEMENT) {
			// Get the local name of this element.
			String name = getName().getLocalPart();

			// If the current element is a start element, add it to the list.
			if(getEventType() == XMLStreamConstants.START_ELEMENT) {
				elements.add(new CloseableElement(name));
			}

			// If the element is an end element, close the last instance of that element.
			else if(getEventType() == XMLStreamConstants.END_ELEMENT) {
				// Loop through the list of elements.
				for(int ndx = elements.size()-1; ndx >= 0; ndx--) {
					// If this is an end element, close the last open one with a matching name.
					String elementName = elements.get(ndx).getName();
					if(elementName.equals(getLocalName()) && elements.get(ndx).isOpen()) {
						elements.get(ndx).close();
					}
				}
			}
		}

		return super.next();
	}

	/** Forwards the call, then replaces the returned URI if needed. */
	@Override
	public String getNamespaceURI() {
		// Get the local name of this element.
		String name = getName().getLocalPart();

		// If the name needs a replacement, do so.
		if(needsNameReplacement(name)) {
			name = localNameMap.get(name);
		}

		// Return the ISO 19115-3 namespace.
		return getMappedNamespace(name, super.getNamespaceURI());
	}

	/** Forwards the call, then replaces the returned URI if needed. */
	@Override
	public String getNamespaceURI(int index) {
		return toImpl(super.getNamespaceURI(index)).intern();
		// NOTE: The "index" passed to this method is the index of a namespace declaration on the root element.
		// This should not matter as long as each /element/ has the proper namespace URI.
	}

	/** Forwards the call, then replaces the returned URI if needed. */
	@Override
	public String getNamespaceURI(final String prefix) {
		return toImpl(super.getNamespaceURI(prefix)).intern();
	}

	/** Forwards the call, then replaces the returned URI if needed. */
	@Override
	public String getAttributeNamespace(final int index) {
		String namespace = toImpl(super.getAttributeNamespace(index));
		if(namespace == null) {
			return null;
		}
		return namespace.intern();
	}

	@Override
	public String getAttributeValue(int index) {
		// Store the attribute value given by the XML.
		String value = super.getAttributeValue(index);

		// Try to parse the value as a QName.
		try {
			// Parse the value of the attribute as a QName.
			QName qname = DatatypeConverter.parseQName(value, this.getNamespaceContext());
			// If the Qname is valid, convert it to an ISO 19139 QName and replace the old value with the newly generated one.
			if(!qname.getPrefix().equals("") && !qname.getNamespaceURI().equals("")) {
				String newLocalName = qname.getLocalPart();
				String newURI = getMappedNamespace(newLocalName, qname.getNamespaceURI());
				String newPrefix = Namespaces.getPreferredPrefix(newURI, "");
				value = newPrefix + ":" + newLocalName;
			}
		} catch (IllegalArgumentException e) {
			// Do nothing. This just means the value isn't a valid QName.
		}

		return value.intern();
	}

	/**
	 * Small class used for keeping track of elements that have been read and determining if they have been closed.
	 * Useful for determining the parent of a child element so we can assign the correct namespace.
	 */
	private class CloseableElement {
		private String name;
		private boolean closed;

		public CloseableElement(String name) {
			this.name = name;
			this.closed = false;
		}

		public void close() {
			this.closed = true;
		}

		public boolean isOpen() {
			return !closed;
		}

		public String getName() {
			return name;
		}
	}
}
