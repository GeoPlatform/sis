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
package org.apache.sis.metadata.iso.citation;

import java.net.URI;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.sis.internal.jaxb.MetadataInfo;
import org.apache.sis.metadata.iso.ISOMetadata;
import org.opengis.metadata.citation.OnLineFunction;
import org.opengis.metadata.citation.OnlineResource;
import org.opengis.util.InternationalString;


/**
 * Information about on-line sources from which the dataset, specification, or
 * community profile name and extended metadata elements can be obtained.
 *
 * <p><b>Limitations:</b></p>
 * <ul>
 *   <li>Instances of this class are not synchronized for multi-threading.
 *       Synchronization, if needed, is caller's responsibility.</li>
 *   <li>Serialized objects of this class are not guaranteed to be compatible with future Apache SIS releases.
 *       Serialization support is appropriate for short term storage or RMI between applications running the
 *       same version of Apache SIS. For long term storage, use {@link org.apache.sis.xml.XML} instead.</li>
 * </ul>
 *
 * @author  Martin Desruisseaux (IRD, Geomatys)
 * @author  Touraïvane 			(IRD)
 * @author  Cédric Briançon 	(Geomatys)
 * @author  Cullen Rombach		(Image Matters)
 * @since   0.3
 * @version 0.8
 * @module
 */
@XmlType(name = "CI_OnlineResource_Type", propOrder = {
		"xmlLinkage",			// ISO 19115-3
		"xmlLinkageLegacy",		// ISO 19139
		"protocol",
		"applicationProfile",
		"name",
		"description",
		"function",
		"xmlProtocolRequest" 	// ISO 19115-3 only
})
@XmlRootElement(name = "CI_OnlineResource")
public class DefaultOnlineResource extends ISOMetadata implements OnlineResource {
	/**
	 * Serial number for inter-operability with different versions.
	 */
	private static final long serialVersionUID = 1413613911128890864L;

	/**
	 * Location (address) for on-line access using a Uniform Resource Locator address or
	 * similar addressing scheme such as "{@code http://www.statkart.no/isotc211}".
	 */
	protected URI linkage;

	/**
	 * The connection protocol to be used.
	 */
	protected String protocol;

	/**
	 * Name of an application profile that can be used with the online resource.
	 */
	protected String applicationProfile;

	/**
	 * Name of the online resources.
	 */
	protected InternationalString name;

	/**
	 * Detailed text description of what the online resource is/does.
	 */
	protected InternationalString description;

	/**
	 * Code for function performed by the online resource.
	 */
	protected OnLineFunction function;

	/**
	 * Request used to access the resource depending on the protocol.
	 * This is used mainly for POST requests.
	 */
	protected String protocolRequest;

	/**
	 * Creates an initially empty on line resource.
	 */
	public DefaultOnlineResource() {
	}

	/**
	 * Creates an on line resource initialized to the given URI.
	 *
	 * @param linkage The location for on-line access using a Uniform Resource Locator address,
	 *        or {@code null} if none.
	 */
	public DefaultOnlineResource(final URI linkage) {
		this.linkage = linkage;
	}

	/**
	 * Constructs a new instance initialized with the values from the specified metadata object.
	 * This is a <cite>shallow</cite> copy constructor, since the other metadata contained in the
	 * given object are not recursively copied.
	 *
	 * @param object The metadata to copy values from, or {@code null} if none.
	 *
	 * @see #castOrCopy(OnlineResource)
	 */
	public DefaultOnlineResource(final OnlineResource object) {
		super(object);
		if (object != null) {
			linkage            = object.getLinkage();
			protocol           = object.getProtocol();
			applicationProfile = object.getApplicationProfile();
			name               = object.getName();
			description        = object.getDescription();
			function           = object.getFunction();
			protocolRequest    = object.getProtocolRequest();
		}
	}

	/**
	 * Returns a SIS metadata implementation with the values of the given arbitrary implementation.
	 * This method performs the first applicable action in the following choices:
	 *
	 * <ul>
	 *   <li>If the given object is {@code null}, then this method returns {@code null}.</li>
	 *   <li>Otherwise if the given object is already an instance of
	 *       {@code DefaultOnlineResource}, then it is returned unchanged.</li>
	 *   <li>Otherwise a new {@code DefaultOnlineResource} instance is created using the
	 *       {@linkplain #DefaultOnlineResource(OnlineResource) copy constructor}
	 *       and returned. Note that this is a <cite>shallow</cite> copy operation, since the other
	 *       metadata contained in the given object are not recursively copied.</li>
	 * </ul>
	 *
	 * @param  object The object to get as a SIS implementation, or {@code null} if none.
	 * @return A SIS implementation containing the values of the given object (may be the
	 *         given object itself), or {@code null} if the argument was null.
	 */
	public static DefaultOnlineResource castOrCopy(final OnlineResource object) {
		if (object == null || object instanceof DefaultOnlineResource) {
			return (DefaultOnlineResource) object;
		}
		return new DefaultOnlineResource(object);
	}

	/**
	 * Returns the name of an application profile that can be used with the online resource.
	 * Returns {@code null} if none.
	 *
	 * @return Application profile that can be used with the online resource, or {@code null}.
	 */
	@Override
	@XmlElement(name = "applicationProfile")
	public String getApplicationProfile() {
		return applicationProfile;
	}

	/**
	 * Sets the name of an application profile that can be used with the online resource.
	 *
	 * @param newValue The new application profile.
	 */
	public void setApplicationProfile(final String newValue) {
		checkWritePermission();
		applicationProfile = newValue;
	}

	/**
	 * Name of the online resource. Returns {@code null} if none.
	 *
	 * @return Name of the online resource, or {@code null}.
	 */
	@Override
	@XmlElement(name = "name")
	public InternationalString getName() {
		return name;
	}

	/**
	 * Sets the name of the online resource.
	 *
	 * @param newValue The new name, or {@code null} if none.
	 */
	public void setName(final InternationalString newValue) {
		checkWritePermission();
		name = newValue;
	}

	/**
	 * Returns the detailed text description of what the online resource is/does.
	 *
	 * @return Text description of what the online resource is/does, or {@code null}.
	 */
	@Override
	@XmlElement(name = "description")
	public InternationalString getDescription() {
		return description;
	}

	/**
	 * Sets the detailed text description of what the online resource is/does.
	 *
	 * @param newValue The new description, or {@code null} if none.
	 */
	public void setDescription(final InternationalString newValue) {
		checkWritePermission();
		description = newValue;
	}

	/**
	 * Returns the code for function performed by the online resource.
	 *
	 * @return Function performed by the online resource, or {@code null}.
	 */
	@Override
	@XmlElement(name = "function")
	public OnLineFunction getFunction() {
		return function;
	}

	/**
	 * Sets the code for function performed by the online resource.
	 *
	 * @param newValue The new function, or {@code null} if none.
	 */
	public void setFunction(final OnLineFunction newValue) {
		checkWritePermission();
		function = newValue;
	}

	/**
	 * Returns the location (address) for on-line access using a Uniform Resource Locator address or
	 * similar addressing scheme.
	 * 
	 * A URI is only used in ISO 19139, so this method returns null unless the metadata
	 * being marshalled/unmarshalled is in ISO 19139 format.
	 *
	 * @return Location for on-line access using a Uniform Resource Locator address or similar scheme, or {@code null}.
	 */
	@Override
	public URI getLinkage() {
		return linkage;
	}

	/**
	 * Sets the location (address) for on-line access using a Uniform Resource Locator address or
	 * similar addressing scheme such as "{@code http://www.statkart.no/isotc211}".
	 * 
	 * A URI is only used in ISO 19139, so this method is only used if the metadata
	 * being marshalled/unmarshalled is in ISO 19139 format.
	 *
	 * @param newValue The new linkage, or {@code null} if none.
	 */
	public void setLinkage(final URI newValue) {
		checkWritePermission();
		linkage = newValue;
	}

	/**
	 * Gets the linkage for this online resource (used in ISO 19115-3 format).
	 * @see {@link #getLinkage}
	 */
	@XmlElement(name = "linkage", required = true)
	private String getXmlLinkage() {
		return (MetadataInfo.is2003() || linkage == null) ? null : linkage.toString();
	}

	/**
	 * Sets the linkage for this online resource (Used for both formats).
	 * @see {@link #setLinkage}
	 */
	@SuppressWarnings("unused")
	private void setXmlLinkage(final String newValue) {
		checkWritePermission();
		linkage = URI.create(newValue);
	}

	/**
	 * Gets the linkage for this online resource (used in ISO 19139 format).
	 * @see {@link #getLinkage}
	 */
	@XmlElement(name = "linkage", required = true)
	private URI getXmlLinkageLegacy() {
		return MetadataInfo.is2014() ? null : linkage;
	}

	/**
	 * Sets the linkage for this online resource (unused - placeholder only).
	 * @see {@link #setLinkage}
	 */
	@SuppressWarnings("unused")
	private void setXmlLinkageLegacy(final URI newValue) {
		checkWritePermission();
		linkage = newValue;
	}

	/**
	 * Returns the connection protocol to be used.
	 *
	 * <div class="note"><b>Example:</b>
	 * ftp, http get KVP, http POST, <i>etc</i>.
	 * </div>
	 *
	 * @return Connection protocol to be used, or {@code null}.
	 */
	@Override
	@XmlElement(name = "protocol")
	public String getProtocol() {
		return protocol;
	}

	/**
	 * Sets the connection protocol to be used.
	 *
	 * @param newValue The new protocol, or {@code null} if none.
	 */
	public void setProtocol(final String newValue) {
		checkWritePermission();
		protocol = newValue;
	}

	/**
	 * Returns the request used to access the resource depending on the protocol.
	 * This is used mainly for POST requests.
	 *
	 * <div class="note"><b>Example:</b>
	 * {@preformat xml
	 *     <GetFeature service="WFS" version="2.0.0"
	 *                 outputFormat="application/gml+xml;verson=3.2"
	 *                 xmlns="(…snip…)">
	 *         <Query typeNames="Roads"/>
	 *     </GetFeature>
	 * }
	 * </div>
	 *
	 * @return Request used to access the resource.
	 *
	 * @since 0.5
	 */
	@Override
	public String getProtocolRequest() {
		return protocolRequest;
	}

	/**
	 * Sets the request to be used.
	 *
	 * @param newValue The new request, or {@code null} if none.
	 *
	 * @since 0.5
	 */
	public void setProtocolRequest(final String newValue) {
		checkWritePermission();
		protocolRequest = newValue;
	}

	/**
	 * Gets the protocol request for this online resource (used in ISO 19115-3 format).
	 * @see {@link #getProtocolRequest}
	 */
	@XmlElement(name = "protocolRequest")
	private String getXmlProtocolRequest() {
		return MetadataInfo.is2003() ? null : getProtocolRequest();
	}

	/**
	 * Sets the protocol request for this online resource (used in ISO 19115-3 format).
	 * @see {@link #setProtocolRequest}
	 */
	@SuppressWarnings("unused")
	private void setXmlProtocolRequest(final String newValue) {
		setProtocolRequest(newValue);
	}
}
