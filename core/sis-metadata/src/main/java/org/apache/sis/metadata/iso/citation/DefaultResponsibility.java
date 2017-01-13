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

import java.util.Collection;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import org.apache.sis.internal.jaxb.MetadataInfo;
import org.apache.sis.internal.util.CheckedArrayList;
import org.apache.sis.metadata.iso.ISOMetadata;
import org.apache.sis.xml.Namespaces;
import org.opengis.metadata.citation.Party;
import org.opengis.metadata.citation.Responsibility;
import org.opengis.metadata.citation.Role;
import org.opengis.metadata.extent.Extent;


/**
 * Information about the party and their role.
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
 * @author  Rémi Maréchal 		(Geomatys)
 * @author  Martin Desruisseaux (Geomatys)
 * @author	Cullen Rombach		(Image Matters)
 * @since   0.5
 * @version 0.8
 * @module
 */
@SuppressWarnings("deprecation")
@XmlType(name = "CI_Responsibility_Type", propOrder = {
	"xmlRole",
	"xmlExtents",
	"xmlParties"
})
@XmlRootElement(name = "CI_Responsibility", namespace = Namespaces.CIT)
@XmlSeeAlso({
    DefaultResponsibleParty.class
})
public class DefaultResponsibility extends ISOMetadata implements Responsibility {
    /**
     * Serial number for inter-operability with different versions.
     */
    private static final long serialVersionUID = -8290895980682233572L;

    /**
     * Function performed by the responsible party.
     */
    private Role role;

    /**
     * Spatial or temporal extents of the role.
     */
    private Collection<Extent> extents;

    /**
     * Information about the parties.
     */
    private Collection<Party> parties;

    /**
     * Constructs an initially empty responsible party.
     */
    public DefaultResponsibility() {
    }

    /**
     * Constructs a responsibility initialized to the specified values.
     *
     * @param role   Function performed by the responsible party, or {@code null}.
     * @param extent Spatial or temporal extent of the role, or {@code null}.
     * @param party  Information about the party, or {@code null}.
     */
    public DefaultResponsibility(final Role role, final Extent extent, final Party party) {
        this.role    = role;
        this.extents = singleton(extent, Extent.class);
        this.parties = singleton(party, Party.class);
    }

    /**
     * Constructs a new instance initialized with the values from the specified metadata object.
     * This is a <cite>shallow</cite> copy constructor, since the other metadata contained in the
     * given object are not recursively copied.
     *
     * @param object The metadata to copy values from, or {@code null} if none.
     *
     * @see #castOrCopy(Responsibility)
     */
    public DefaultResponsibility(final Responsibility object) {
        super(object);
        if (object != null) {
            this.role    = object.getRole();
            this.extents = copyCollection(object.getExtents(), Extent.class);
            this.parties = copyCollection(object.getParties(), Party.class);
        }
    }

    /**
     * Returns a SIS metadata implementation with the values of the given arbitrary implementation.
     * This method performs the first applicable action in the following choices:
     *
     * <ul>
     *   <li>If the given object is {@code null}, then this method returns {@code null}.</li>
     *   <li>Otherwise if the given object is already an instance of
     *       {@code DefaultResponsibility}, then it is returned unchanged.</li>
     *   <li>Otherwise a new {@code DefaultResponsibility} instance is created using the
     *       {@linkplain #DefaultResponsibility(Responsibility) copy constructor}
     *       and returned. Note that this is a <cite>shallow</cite> copy operation, since the other
     *       Responsibility contained in the given object are not recursively copied.</li>
     * </ul>
     *
     * @param  object The object to get as a SIS implementation, or {@code null} if none.
     * @return A SIS implementation containing the values of the given object (may be the
     *         given object itself), or {@code null} if the argument was null.
     */
    public static DefaultResponsibility castOrCopy(final Responsibility object) {
        if (object == null || object instanceof DefaultResponsibility) {
            return (DefaultResponsibility) object;
        }
        return new DefaultResponsibility(object);
    }

    /**
     * Returns the function performed by the responsible party.
     *
     * @return Function performed by the responsible party.
     */
    @Override
    public Role getRole() {
        return role;
    }

    /**
     * Sets the function performed by the responsible party.
     *
     * @param newValue The new role, or {@code null} if none.
     */
    public void setRole(final Role newValue) {
        checkWritePermission();
        role = newValue;
    }
    
    /**
     * Gets the role for this responsibility (used in ISO 19115-3 format).
     * @see {@link #getRole}
     */
    @XmlElement(name = "role", required = true)
    private Role getXmlRole() {
    	return MetadataInfo.is2003() ? null : getRole();
    }
    
    /**
     * Sets the role for this responsibility (used in ISO 19115-3 format).
     * @see {@link #setRole}
     */
    @SuppressWarnings("unused")
	private void setXmlRole(final Role newValue) {
    	setRole(newValue);
    }

    /**
     * Returns the spatial or temporal extents of the role.
     *
     * @return The spatial or temporal extents of the role.
     */
    @Override
    public Collection<Extent> getExtents() {
        return extents = nonNullCollection(extents, Extent.class);
    }

    /**
     * Sets the spatial and temporal extents of the role.
     *
     * @param newValues The new spatial and temporal extents of the role.
     */
    public void setExtents(final Collection<? extends Extent> newValues) {
        extents = writeCollection(newValues, extents, Extent.class);
    }
    
    /**
     * Gets the extents for this responsibility (used in ISO 19115-3 format).
     * @see {@link #getExtents}
     */
    @XmlElement(name = "extent")
    private Collection<Extent> getXmlExtents() {
    	if(MetadataInfo.isUnmarshalling()) {
    		return getExtents();
    	}
    	return MetadataInfo.is2003() ? new CheckedArrayList<>(Extent.class) : getExtents();
    }
    
    /**
     * Sets the extents for this responsibility (used in ISO 19115-3 format).
     * @see {@link #setExtents}
     */
    @SuppressWarnings("unused")
	private void setXmlExtents(final Collection<? extends Extent> newValues) {
    	setExtents(newValues);
    }

    /**
     * Returns information about the parties.
     *
     * @return Information about the parties.
     */
    @Override
    public Collection<Party> getParties() {
        return parties = nonNullCollection(parties, Party.class);
    }

    /**
     * Sets information about the parties.
     *
     * @param newValues New information about the parties.
     */
    public void setParties(final Collection<? extends Party> newValues) {
        parties = writeCollection(newValues, parties, Party.class);
    }
    
    /**
     * Gets the parties for this responsibility (used in ISO 19115-3 format).
     * @see {@link #getParties}
     */
    @XmlElement(name = "party")
    private Collection<Party> getXmlParties() {
    	if(MetadataInfo.isUnmarshalling()) {
    		return getParties();
    	}
    	return MetadataInfo.is2003() ? new CheckedArrayList<>(Party.class) : getParties();
    }
    
    /**
     * Sets the parties for this responsibility (used in ISO 19115-3 format).
     * @see {@link #setParties}
     */
    @SuppressWarnings("unused")
	private void setXmlParties(final Collection<? extends Party> newValues) {
    	setParties(newValues);
    }
}
