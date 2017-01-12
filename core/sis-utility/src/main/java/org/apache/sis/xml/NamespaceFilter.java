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

import static java.util.Collections.singletonMap;

import java.util.Map;

import org.apache.sis.internal.jaxb.LegacyNamespaces;


/**
 * The target version of standards for {@link FilteredNamespaces}.
 *
 * See {@link FilteredNamespaces} for more information.
 * 
 * Changed in version 0.8 - used to be named FilterVersion. FilterVersion is now a wrapper
 * for NamespaceFilters, since a filter version may need to replace any number of namespaces.
 *
 * @author  Martin Desruisseaux (Geomatys)
 * @author  Cullen Rombach		(Image Matters)
 * @since   0.4
 * @version 0.8
 * @module
 */
enum NamespaceFilter {
	/**
	 * GML using the legacy {@code "http://www.opengis.net/gml"} namespace.
	 */
	GML31_FILTER(Namespaces.GML, LegacyNamespaces.GML),

	// Filters for transforming ISO 191315-3 namespaces to ISO 19139 namespaces.
	CAT(Namespaces.CAT, Namespaces.GMD),
	CIT(Namespaces.CIT, Namespaces.GMD),
	LAN(Namespaces.LAN, Namespaces.GMD),
	MAC(Namespaces.MAC, Namespaces.GMD),
	MAS(Namespaces.MAS, Namespaces.GMD),
	MCC(Namespaces.MCC, Namespaces.GMD),
	MCO(Namespaces.MCO, Namespaces.GMD),
	MEX(Namespaces.MEX, Namespaces.GMD),
	MMI(Namespaces.MMI, Namespaces.GMD),
	MPC(Namespaces.MPC, Namespaces.GMD),
	MRC(Namespaces.MRC, Namespaces.GMD),
	MRD(Namespaces.MRD, Namespaces.GMD),
	MDQ(Namespaces.MDQ, Namespaces.GMD),
	MRI(Namespaces.MRI, Namespaces.GMD),
	MRL(Namespaces.MRL, Namespaces.GMD),
	MRS(Namespaces.MRS, Namespaces.GMD),
	MSR(Namespaces.MSR, Namespaces.GMD),
	MDS(Namespaces.MDS, Namespaces.GMD),
	MD1(Namespaces.MD1, Namespaces.GMD),
	MDA(Namespaces.MDA, Namespaces.GMD),
	MDT(Namespaces.MDT, Namespaces.GMD),
	MD2(Namespaces.MD2, Namespaces.GMD),
	RCE(Namespaces.RCE, Namespaces.GMD),
	FCC(Namespaces.FCC, Namespaces.GMD),
	GMW(Namespaces.GMW, Namespaces.GMD),
	DQC(Namespaces.DQC, Namespaces.GMD),
	MDB(Namespaces.MDB, Namespaces.GMD),
	GEX(Namespaces.GEX, Namespaces.GMD),
	GCX(Namespaces.GCX, LegacyNamespaces.GMX),
	GCO(Namespaces.GCO, LegacyNamespaces.GCO),
	SRV(Namespaces.SRV, LegacyNamespaces.SRV);

	/**
	 * Filters used to change GML version to 3.1
	 */
	static NamespaceFilter[] GML31 = {GML31_FILTER};

	/**
	 * Namespace changes used to convert an ISO 19115-3 document to ISO 19139
	 */
	static NamespaceFilter[] ISO19139 = {CAT, CIT, LAN, MAC, MAS, MCC, MCO, DQC, MEX, MMI, MPC, MRC,
			MRD, MDQ, MRI, MRL, MRS, MSR, MDS, MD1, MDT, MD2, GCO, SRV, RCE, FCC, GEX, GMW, MDB, GCX};

	/**
	 * Apply all known namespace replacements. This can be used only at unmarshalling time,
	 * for replacing all namespaces by the namespaces declared in Apache SIS JAXB annotations.
	 */
	static NamespaceFilter[] ALL = {GML31_FILTER, CAT, CIT, LAN, MAC, MAS, MCC, MCO, DQC, MEX, MMI, MPC, MRC,
			MRD, MDQ, MRI, MRL, MRS, MSR, MDS, MD1, MDT, MD2, GCO, SRV, RCE, FCC, GEX, GMW, MDB, GCX};

	/**
	 * The URI replacements to apply when going from the "real" data producer (JAXB marshaller)
	 * to the filtered reader/writer. Keys are the actual URIs as declared in SIS implementation,
	 * and values are the URIs read or to write instead of the actual ones.
	 *
	 * @see FilteredNamespaces#toView
	 */
	final Map<String,String> toView;

	/**
	 * The URI replacements to apply when going from the filtered reader/writer to the "real"
	 * data consumer (JAXB unmarshaller). This map is the converse of {@link #toView}.
	 *
	 * @see FilteredNamespaces#toImpl
	 */
	final Map<String,String> toImpl;

	/**
	 * Creates a new enum for replacing only one namespace.
	 */
	private NamespaceFilter(final String impl, final String view) {
		this.toView = singletonMap(impl, view);
		this.toImpl = singletonMap(view, impl);
	}
}
