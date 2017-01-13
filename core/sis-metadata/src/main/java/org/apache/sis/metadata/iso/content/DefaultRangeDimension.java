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
package org.apache.sis.metadata.iso.content;

import java.util.Collection;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import org.apache.sis.internal.jaxb.MetadataInfo;
import org.apache.sis.internal.util.CheckedArrayList;
import org.apache.sis.metadata.iso.ISOMetadata;
import org.opengis.metadata.Identifier;
import org.opengis.metadata.content.RangeDimension;
import org.opengis.metadata.content.SampleDimension;
import org.opengis.util.InternationalString;
import org.opengis.util.MemberName;


/**
 * Information on the range of each dimension of a cell measurement value.
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
 * @author  Rémi Maréchal 		(Geomatys)
 * @author  Cullen Rombach		(Image Matters)
 * @since   0.3
 * @version 0.8
 * @module
 */
@XmlType(name = "MD_RangeDimension_Type", propOrder = {
    "sequenceIdentifier",
    "xmlDescription",		// ISO 19115-3
    "xmlDescriptor",		// ISO 19139
	"xmlNames"				// ISO 19115-3
})
@XmlRootElement(name = "MD_RangeDimension")
@XmlSeeAlso(DefaultBand.class)
public class DefaultRangeDimension extends ISOMetadata implements RangeDimension {
    /**
     * Serial number for inter-operability with different versions.
     */
    private static final long serialVersionUID = 4517148689016920767L;

    /**
     * Unique name or number that identifies attributes included in the coverage.
     */
    private MemberName sequenceIdentifier;

    /**
     * Description of the attribute.
     */
    private InternationalString description;

    /**
     * Identifiers for each attribute included in the resource. These identifiers
     * can be use to provide names for the attribute from a standard set of names.
     */
    private Collection<Identifier> names;

    /**
     * Constructs an initially empty range dimension.
     */
    public DefaultRangeDimension() {
    }

    /**
     * Constructs a new instance initialized with the values from the specified metadata object.
     * This is a <cite>shallow</cite> copy constructor, since the other metadata contained in the
     * given object are not recursively copied.
     *
     * @param object The metadata to copy values from, or {@code null} if none.
     *
     * see #castOrCopy(RangeDimension)
     */
    public DefaultRangeDimension(final RangeDimension object) {
        super(object);
        if (object != null) {
            sequenceIdentifier = object.getSequenceIdentifier();
            description        = object.getDescription();
            names              = copyCollection(object.getNames(), Identifier.class);
        }
    }

    /**
     * Returns a SIS metadata implementation with the values of the given arbitrary implementation.
     * This method performs the first applicable action in the following choices:
     *
     * <ul>
     *   <li>If the given object is {@code null}, then this method returns {@code null}.</li>
     *   <li>Otherwise if the given object is an instance of {@link SampleDimension}, then this method
     *       delegates to the {@code castOrCopy(…)} method of the corresponding SIS subclass.</li>
     *   <li>Otherwise if the given object is already an instance of
     *       {@code DefaultRangeDimension}, then it is returned unchanged.</li>
     *   <li>Otherwise a new {@code DefaultRangeDimension} instance is created using the
     *       {@linkplain #DefaultRangeDimension(RangeDimension) copy constructor}
     *       and returned. Note that this is a <cite>shallow</cite> copy operation, since the other
     *       metadata contained in the given object are not recursively copied.</li>
     * </ul>
     *
     * @param  object The object to get as a SIS implementation, or {@code null} if none.
     * @return A SIS implementation containing the values of the given object (may be the
     *         given object itself), or {@code null} if the argument was null.
     */
    public static DefaultRangeDimension castOrCopy(final RangeDimension object) {
        if (object instanceof SampleDimension) {
            return DefaultSampleDimension.castOrCopy((SampleDimension) object);
        }
        // Intentionally tested after the sub-interfaces.
        if (object == null || object instanceof DefaultRangeDimension) {
            return (DefaultRangeDimension) object;
        }
        return new DefaultRangeDimension(object);
    }

    /**
     * Returns a unique name or number that identifies attributes included in the coverage.
     *
     * @return Unique name or number, or {@code null}.
     */
    @Override
    @XmlElement(name = "sequenceIdentifier")
    public MemberName getSequenceIdentifier() {
        return sequenceIdentifier;
    }

    /**
     * Sets the number that uniquely identifies instances of bands of wavelengths on which a sensor operates.
     *
     * @param newValue The new sequence identifier.
     */
    public void setSequenceIdentifier(final MemberName newValue) {
        checkWritePermission();
        sequenceIdentifier = newValue;
    }

    /**
     * Returns the description of the attribute.
     *
     * @return Description of the attribute, or {@code null}.
     *
     * @since 0.5
     */
    @Override
    public InternationalString getDescription() {
        return description;
    }

    /**
     * Sets the description of the attribute.
     *
     * @param newValue The new description.
     *
     * @since 0.5
     */
    public void setDescription(final InternationalString newValue) {
        checkWritePermission();
        description = newValue;
    }
    
    /**
	 * Gets the description for this range dimension (used in ISO 19115-3 format).
	 * see {@link #getDescription}
	 */
	@XmlElement(name = "description")
	private InternationalString getXmlDescription() {
		return MetadataInfo.is2003() ? null : getDescription();
	}

	/**
	 * Sets the description for this range dimension (used in ISO 19115-3 format).
	 * see {@link #setDescription}
	 */
	@SuppressWarnings("unused")
	private void setXmlDescription(final InternationalString newValue) {
		setDescription(newValue);
	}

    /**
     * Returns the description of the range of a cell measurement value.
     * This method fetches the value from the {@linkplain #getDescription() description}.
     *
     * @return Description of the range of a cell measurement value, or {@code null}.
     *
     * @deprecated As of ISO 19115:2014, renamed {@link #getDescription()}.
     */
    @Override
    @Deprecated
    public InternationalString getDescriptor() {
        return getDescription();
    }

    /**
     * Sets the description of the range of a cell measurement value.
     * This method stores the value in the {@linkplain #setDescription(InternationalString) description}.
     *
     * @param newValue The new descriptor.
     *
     * @deprecated As of ISO 19115:2014, renamed {@link #setDescription(InternationalString)}.
     */
    @Deprecated
    public void setDescriptor(final InternationalString newValue) {
        setDescription(newValue);
    }
    
    /**
	 * Gets the descriptor for this range dimension (used in ISO 19139 format).
	 * see {@link #getDescriptor}
	 */
	@XmlElement(name = "descriptor")
	private InternationalString getXmlDescriptor() {
		return MetadataInfo.is2014() ? null : getDescriptor();
	}

	/**
	 * Sets the descriptor for this range dimension (used in ISO 19139 format).
	 * see {@link #setDescription}
	 */
	@SuppressWarnings("unused")
	private void setXmlDescriptor(final InternationalString newValue) {
		setDescriptor(newValue);
	}

    /**
     * Returns the identifiers for each attribute included in the resource.
     * These identifiers can be use to provide names for the attribute from a standard set of names.
     *
     * @return Identifiers for each attribute included in the resource.
     *
     * @since 0.5
     */
    @Override
    public Collection<Identifier> getNames() {
        return names = nonNullCollection(names, Identifier.class);
    }

    /**
     * Sets the identifiers for each attribute included in the resource.
     *
     * @param newValues The new identifiers for each attribute.
     *
     * @since 0.5
     */
    public void setNames(final Collection<? extends Identifier> newValues) {
        names = writeCollection(newValues, names, Identifier.class);
    }
    
    /**
	 * Gets the names for this range dimension (used in ISO 19115-3 format).
	 * see {@link #getNames}
	 */
	@XmlElement(name = "name")
	private Collection<Identifier> getXmlNames() {
		if(MetadataInfo.isUnmarshalling()) {
			return getNames();
		}
		return MetadataInfo.is2003() ? new CheckedArrayList<>(Identifier.class) : getNames();
	}
}
