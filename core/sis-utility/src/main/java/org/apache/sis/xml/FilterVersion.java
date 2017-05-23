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

import java.util.HashMap;
import java.util.Map;


/**
 * The target version of standards for {@link FilteredNamespaces}.
 *
 * See {@link FilteredNamespaces} for more information.
 *
 * @author  Cullen Rombach		(Image Matters)
 * @since 0.8
 * @version 0.8
 * @module
 */
public enum FilterVersion {
	
	/**
	 * Filter used to change GML version to 3.1
	 */
	GML31(NamespaceFilter.GML31),
	
	/**
	 * Filter used to convert an ISO 19115-3 document to ISO 19139
	 */
	ISO19139(NamespaceFilter.ISO19139),

    /**
     * Apply all known namespace replacements. This can be used only at unmarshalling time,
     * for replacing all namespaces by the namespaces declared in Apache SIS JAXB annotations.
     */
	ALL(NamespaceFilter.ALL);

    /**
     * The URI replacements to apply when going from the "real" data producer (JAXB marshaller)
     * to the filtered reader/writer. Keys are the actual URIs as declared in SIS implementation,
     * and values are the URIs read or to write instead of the actual ones.
     *
     * @see FilteredNamespaces#toView
     */
    public final Map<String,String> toView;

    /**
     * The URI replacements to apply when going from the filtered reader/writer to the "real"
     * data consumer (JAXB unmarshaller). This map is the converse of {@link #toView}.
     *
     * @see FilteredNamespaces#toImpl
     */
    public final Map<String,String> toImpl;

    /**
     * Creates a new enum for replacing only one namespace.
     */
    private FilterVersion(NamespaceFilter[] filters) {
    	this.toView = new HashMap<String,String>();
    	this.toImpl = new HashMap<String,String>();
    	// Loop through NamespaceFilters
    	for(NamespaceFilter filter : filters) {
    		// Add each key/value pair to this object's toView and toImpl maps.
    		for(String key : filter.toView.keySet()) {
    			this.toView.put(key, filter.toView.get(key));
    			this.toImpl.put(filter.toView.get(key), key);
    		}
    	}
    }
}
