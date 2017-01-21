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

import java.util.Map;
import java.util.HashMap;
import java.util.Locale;
import javax.xml.XMLConstants;
import org.apache.sis.util.Static;
import org.apache.sis.util.Version;
import org.apache.sis.util.ArgumentChecks;


/**
 * Lists some namespaces URLs used by JAXB when (un)marshalling.
 *
 * <p><strong>Warning: string constants in this class may change in any SIS version.</strong>
 * Those constants are made available for applications who wish to use the same URLs than SIS
 * in their own JAXB annotations. Note that applications using those constants will have their
 * URLs determined by the SIS version available at compile-time, not runtime, because the
 * {@code javac} compiler inlines string constants.</p>
 *
 * <p>The following table lists the URLs, their usual prefix, and the SIS versions when each URL changed.
 * Note that it has not yet been updated to include new namespaces used in the ISO 19115-3 standard.</p>
 * <table class="sis">
 *   <caption>Namespaces and change log</caption>
 *   <tr><th>Prefix</th> <th>XML Namespace</th>   <th>Changes history</th></tr>
 *   <tr><td>gco</td>    <td>{@value #GCO}</td>   <td></td></tr>
 *   <tr><td>gfc</td>    <td>{@value #GFC}</td>   <td></td></tr>
 *   <tr><td>gmd</td>    <td>{@value #GMD}</td>   <td></td></tr>
 *   <tr><td>gmi</td>    <td>{@value #GMI}</td>   <td></td></tr>
 *   <tr><td>srv</td>    <td>{@value #SRV}</td>   <td></td></tr>
 *   <tr><td>gts</td>    <td>{@value #GTS}</td>   <td></td></tr>
 *   <tr><td>gmx</td>    <td>{@value #GCX}</td>   <td></td></tr>
 *   <tr><td>gml</td>    <td>{@value #GML}</td>   <td>0.4</td></tr>
 *   <tr><td>csw</td>    <td>{@value #CSW}</td>   <td></td></tr>
 *   <tr><td>xsi</td>    <td>{@value #XSI}</td>   <td></td></tr>
 *   <tr><td>xlink</td>  <td>{@value #XLINK}</td> <td></td></tr>
 * </table>
 *
 * <div class="section">Profiles</div>
 * Some countries or organizations define profiles of international standards, which may contain
 * country-specific extensions. The namespace of such extensions are usually defined in a separated
 * class dedicated to the profile. Some of them are listed below:
 *
 * <ul>
 *   <li>{@value org.apache.sis.profile.france.FrenchProfile#NAMESPACE}</li>
 * </ul>
 *
 * @author  Cédric Briançon (Geomatys)
 * @author  Quentin Boileau (Geomatys)
 * @author  Guilhem Legal   (Geomatys)
 * @author  Cullen Rombach  (Image Matters)
 * @since   0.3
 * @version 0.4
 * @module
 */
public final class Namespaces extends Static {
	/**
	 * Do not allow instantiation of this class.
	 */
	private Namespaces() {
	}
	
	// TODO: Remove

	/* --------------------------------------------- */
	/* Namespaces added for ISO 19115-3 integration. */
	/* --------------------------------------------- */

	/**
	 * The <code>{@value}</code> URL.
	 * The usual prefix for this namespace is {@code "dqc"}.
	 *
	 * @category ISO
	 */
	public static final String DQC = "http://standards.iso.org/iso/19157/-2/dqc/1.0";
	
	/**
	 * The <code>{@value}</code> URL.
	 * The usual prefix for this namespace is {@code "rce"}.
	 *
	 * @category ISO
	 */
	public static final String RCE = "http://standards.iso.org/iso/19111/rce/1.0";

	/**
	 * The <code>{@value}</code> URL.
	 * The usual prefix for this namespace is {@code "fcc"}.
	 *
	 * @category ISO
	 */
	public static final String FCC = "http://standards.iso.org/19110/fcc/1.0"; 

	/**
	 * The <code>{@value}</code> URL.
	 * The usual prefix for this namespace is {@code "xs"}.
	 * This is also defined by {@link XMLConstants#W3C_XML_SCHEMA_NS_URI}.
	 *
	 * @category W3C
	 * @see XMLConstants#W3C_XML_SCHEMA_NS_URI
	 */
	public static final String XS = XMLConstants.W3C_XML_SCHEMA_NS_URI;

	/**
	 * The <code>{@value}</code> URL.
	 * The usual prefix for this namespace is {@code "cat"}.
	 *
	 * @category ISO
	 */
	public static final String CAT = "http://standards.iso.org/iso/19115/-3/cat/1.0";

	/**
	 * The <code>{@value}</code> URL.
	 * The usual prefix for this namespace is {@code "cit"}.
	 *
	 * @category ISO
	 */
	public static final String CIT = "http://standards.iso.org/iso/19115/-3/cit/1.0";

	/**
	 * The <code>{@value}</code> URL.
	 * The usual prefix for this namespace is {@code "gco"}.
	 *
	 * @category ISO
	 */
	public static final String GCO = "http://standards.iso.org/iso/19115/-3/gco/1.0";

	/**
	 * The <code>{@value}</code> URL.
	 * The usual prefix for this namespace is {@code "gex"}.
	 *
	 * @category ISO
	 */
	public static final String GEX = "http://standards.iso.org/iso/19115/-3/gex/1.0";

	/**
	 * The <code>{@value}</code> URL.
	 * The usual prefix for this namespace is {@code "gmw"}.
	 *
	 * @category ISO
	 */
	public static final String GMW = "http://standards.iso.org/iso/19115/-3/gmw/1.0";

	/**
	 * The <code>{@value}</code> URL.
	 * The usual prefix for this namespace is {@code "lan"}.
	 *
	 * @category ISO
	 */
	public static final String LAN = "http://standards.iso.org/iso/19115/-3/lan/1.0";
	
	/**
	 * The <code>{@value}</code> URL.
	 * The usual prefix for this namespace is {@code "mac"}.
	 *
	 * @category ISO
	 */
	public static final String MAC = "http://standards.iso.org/iso/19115/-3/mac/1.0";

	/**
	 * The <code>{@value}</code> URL.
	 * The usual prefix for this namespace is {@code "mas"}.
	 *
	 * @category ISO
	 */
	public static final String MAS = "http://standards.iso.org/iso/19115/-3/mas/1.0";

	/**
	 * The <code>{@value}</code> URL.
	 * The usual prefix for this namespace is {@code "mcc"}.
	 *
	 * @category ISO
	 */
	public static final String MCC = "http://standards.iso.org/iso/19115/-3/mcc/1.0";

	/**
	 * The <code>{@value}</code> URL.
	 * The usual prefix for this namespace is {@code "mco"}.
	 *
	 * @category ISO
	 */
	public static final String MCO = "http://standards.iso.org/iso/19115/-3/mco/1.0";

	/**
	 * The <code>{@value}</code> URL.
	 * The usual prefix for this namespace is {@code "mdb"}.
	 *
	 * @category ISO
	 */
	public static final String MDB = "http://standards.iso.org/iso/19115/-3/mdb/1.0";

	/**
	 * The <code>{@value}</code> URL.
	 * The usual prefix for this namespace is {@code "mex"}.
	 *
	 * @category ISO
	 */
	public static final String MEX = "http://standards.iso.org/iso/19115/-3/mex/1.0";

	/**
	 * The <code>{@value}</code> URL.
	 * The usual prefix for this namespace is {@code "mmi"}.
	 *
	 * @category ISO
	 */
	public static final String MMI = "http://standards.iso.org/iso/19115/-3/mmi/1.0";

	/**
	 * The <code>{@value}</code> URL.
	 * The usual prefix for this namespace is {@code "mpc"}.
	 *
	 * @category ISO
	 */
	public static final String MPC = "http://standards.iso.org/iso/19115/-3/mpc/1.0";

	/**
	 * The <code>{@value}</code> URL.
	 * The usual prefix for this namespace is {@code "mrc"}.
	 *
	 * @category ISO
	 */
	public static final String MRC = "http://standards.iso.org/iso/19115/-3/mrc/1.0";

	/**
	 * The <code>{@value}</code> URL.
	 * The usual prefix for this namespace is {@code "mrd"}.
	 *
	 * @category ISO
	 */
	public static final String MRD = "http://standards.iso.org/iso/19115/-3/mrd/1.0";
	
	/**
	 * The <code>{@value}</code> URL.
	 * The usual prefix for this namespace is {@code "mdq"}.
	 *
	 * @category ISO
	 */
	public static final String MDQ = "http://standards.iso.org/iso/19115/-3/mdq/1.0";

	/**
	 * The <code>{@value}</code> URL.
	 * The usual prefix for this namespace is {@code "mri"}.
	 *
	 * @category ISO
	 */
	public static final String MRI = "http://standards.iso.org/iso/19115/-3/mri/1.0";

	/**
	 * The <code>{@value}</code> URL.
	 * The usual prefix for this namespace is {@code "mrl"}.
	 *
	 * @category ISO
	 */
	public static final String MRL = "http://standards.iso.org/iso/19115/-3/mrl/1.0";

	/**
	 * The <code>{@value}</code> URL.
	 * The usual prefix for this namespace is {@code "mrs"}.
	 *
	 * @category ISO
	 */
	public static final String MRS = "http://standards.iso.org/iso/19115/-3/mrs/1.0";

	/**
	 * The <code>{@value}</code> URL.
	 * The usual prefix for this namespace is {@code "msr"}.
	 *
	 * @category ISO
	 */
	public static final String MSR = "http://standards.iso.org/iso/19115/-3/msr/1.0";

	/**
	 * The <code>{@value}</code> URL.
	 * The usual prefix for this namespace is {@code "srv"}.
	 *
	 * @category ISO
	 */
	public static final String SRV = "http://standards.iso.org/iso/19115/-3/srv/2.0";

	/**
	 * The <code>{@value}</code> URL.
	 * The usual prefix for this namespace is {@code "mds"}.
	 *
	 * @category ISO
	 */
	public static final String MDS = "http://standards.iso.org/iso/19115/-3/mds/1.0";
	
	/**
	 * The <code>{@value}</code> URL.
	 * The usual prefix for this namespace is {@code "md1"}.
	 *
	 * @category ISO
	 */
	public static final String MD1 = "http://standards.iso.org/iso/19115/-3/md1/1.0";
	
	/**
	 * The <code>{@value}</code> URL.
	 * The usual prefix for this namespace is {@code "mda"}.
	 *
	 * @category ISO
	 */
	public static final String MDA = "http://standards.iso.org/iso/19115/-3/mda/1.0";
	
	/**
	 * The <code>{@value}</code> URL.
	 * The usual prefix for this namespace is {@code "mdt"}.
	 *
	 * @category ISO
	 */
	public static final String MDT = "http://standards.iso.org/iso/19115/-3/mdt/1.0";
	
	/**
	 * The <code>{@value}</code> URL.
	 * The usual prefix for this namespace is {@code "md2"}.
	 *
	 * @category ISO
	 */
	public static final String MD2 = "http://standards.iso.org/iso/19115/-3/md2/1.0";
	
	/**
	 * The <code>{@value}</code> URL.
	 * The usual prefix for this namespace is {@code "gcx"}.
	 * Replaced by Namespaces.GMX
	 *
	 * @category ISO
	 */
	public static final String GCX = "http://standards.iso.org/iso/19115/-3/gcx/1.0";
	
	/* ---------------------- */
	/* Metadata version codes */
	/* ---------------------- */
	
    public static final Version ISO_19115_3 = new Version("2014");
    
    public static final Version ISO_19139 = new Version("2003");
	
	/* -------------------------------------------------- */
	/* Namespaces added prior to ISO 19115-3 integration. */
	/* -------------------------------------------------- */
	
	/**
	 * The <code>{@value}</code> URL, used in ISO 19139.
	 * The usual prefix for this namespace is {@code "gfc"}.
	 *
	 * @category ISO
	 */
	public static final String GFC = "http://www.isotc211.org/2005/gfc";

	/**
	 * The <code>{@value}</code> URL, used in ISO 19139.
	 * The usual prefix for this namespace is {@code "gmd"}.
	 *
	 * @category ISO
	 */
	public static final String GMD = "http://www.isotc211.org/2005/gmd";
	

	/**
	 * The <code>{@value}</code> URL.
	 * The usual prefix for this namespace is {@code "gml"}.
	 * The 3.2 version is equivalent to ISO 19136.
	 *
	 * <p>History</p>
	 * <table class="sis">
	 *   <caption>Change log</caption>
	 *   <tr><th>SIS version</th> <th>URL</th></tr>
	 *   <tr><td>0.3</td>         <td>http://www.opengis.net/gml</td></tr>
	 * </table>
	 *
	 * @category OGC
	 */
	public static final String GML = "http://www.opengis.net/gml/3.2";

	/**
	 * The <code>{@value}</code> URL.
	 * The usual prefix for this namespace is {@code "csw"}.
	 *
	 * @category OGC
	 */
	public static final String CSW = "http://www.opengis.net/cat/csw/2.0.2";

	/**
	 * The <code>{@value}</code> URL.
	 * The usual prefix for this namespace is {@code "xsi"}.
	 * This is also defined by {@link XMLConstants#W3C_XML_SCHEMA_INSTANCE_NS_URI}.
	 *
	 * @category W3C
	 * @see XMLConstants#W3C_XML_SCHEMA_INSTANCE_NS_URI
	 */
	public static final String XSI = XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI;

	/**
	 * The <code>{@value}</code> URL.
	 * The usual prefix for this namespace is {@code "xlink"}.
	 *
	 * @category W3C
	 */
	public static final String XLINK = "http://www.w3.org/1999/xlink";

	/**
	 * URLs for which the prefix to use directly follows them.
	 */
	private static final String[] GENERIC_URLS = {
			"http://www.isotc211.org/2005/",
			"http://www.opengis.net/",
			"http://www.w3.org/1999/",
			"http://www.cnig.gouv.fr/2005/",
			"http://purl.org/",
			"http://standards.iso.org/iso/19115/-3/",
			"http://standards.iso.org/iso/19111/",
			"http://standards.iso.org/iso/19157/-2/",
			"http://standards.iso.org/19110/"
	};

	/**
	 * A map of (<var>URLs</var>, <var>prefix</var>). Stores URLs for which
	 * the prefix to use can not be easily inferred from the URL itself.
	 */
	private static final Map<String,String> SPECIFIC_URLS;
	static {
		final Map<String,String> p = new HashMap<String,String>(40);
		p.put(XMLConstants.W3C_XML_SCHEMA_NS_URI,                          "xs");
		p.put(XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI,                "xsi");
		p.put("http://www.w3.org/2004/02/skos/core#",                    "skos");
		p.put("http://www.w3.org/1999/02/22-rdf-syntax-ns#",              "rdf");
		p.put("http://www.w3.org/1998/Math/MathML",                       "mml");
		p.put("http://www.opengis.net/sensorML/1.0",                     "sml1");
		p.put("http://www.opengis.net/sensorML/1.0.1",                    "sml");
		p.put("http://www.opengis.net/swe/1.0",                          "swe1");
		p.put("http://www.opengis.net/cat/csw/2.0.2",                     "csw");
		p.put("http://www.opengis.net/cat/wrs/1.0",                       "wrs");
		p.put("http://www.opengis.net/cat/wrs",                         "wrs09");
		p.put("http://www.opengis.net/ows-6/utds/0.3",                   "utds");
		p.put("http://www.opengis.net/citygml/1.0",                      "core");
		p.put("http://www.opengis.net/citygml/building/1.0",            "build");
		p.put("http://www.opengis.net/citygml/cityfurniture/1.0",   "furniture");
		p.put("http://www.opengis.net/citygml/transportation/1.0",         "tr");
		p.put("http://www.purl.org/dc/elements/1.1/",                     "dc2");
		p.put("http://www.purl.org/dc/terms/",                           "dct2");
		p.put("http://purl.org/dc/terms/",                                "dct");
		p.put("http://www.inspire.org",                                   "ins");
		p.put("http://inspira.europa.eu/networkservice/view/1.0",  "inspire_vs");
		p.put("urn:oasis:names:tc:ciq:xsdschema:xAL:2.0",                 "xal");
		p.put("urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0",              "rim");
		p.put("urn:oasis:names:tc:ebxml-regrep:rim:xsd:2.5",            "rim25");
		p.put("urn:oasis:names:tc:xacml:2.0:context:schema:os", "xacml-context");
		p.put("urn:oasis:names:tc:xacml:2.0:policy:schema:os",   "xacml-policy");
		p.put("urn:us:gov:ic:ism:v2",                                   "icism");
		SPECIFIC_URLS = p;
	}

	/**
	 * Returns the preferred prefix for the given namespace URI.
	 *
	 * @param  namespace    The namespace URI for which the prefix needs to be found.
	 *                      Can not be {@code null}.
	 * @param  defaultValue The default prefix to returned if the given {@code namespace}
	 *                      is not recognized, or {@code null}.
	 * @return The prefix inferred from the namespace URI, or {@code null} if the given namespace
	 *         is unrecognized and the {@code defaultValue} is null.
	 */
	public static String getPreferredPrefix(String namespace, final String defaultValue) {
		ArgumentChecks.ensureNonNull("namespace", namespace);
		String prefix = SPECIFIC_URLS.get(namespace);
		if (prefix != null) {
			return prefix;
		}
		namespace = namespace.toLowerCase(Locale.ROOT);
		for (final String baseURL : GENERIC_URLS) {
			if (namespace.startsWith(baseURL)) {
				final int startAt = baseURL.length();
				final int endAt = namespace.indexOf('/', startAt);
				if (endAt >= 0) {
					prefix = namespace.substring(startAt, endAt);
				} else {
					prefix = namespace.substring(startAt);
				}
				return prefix;
			}
		}
		return defaultValue;
	}
}
