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

import static org.apache.sis.internal.metadata.MetadataUtilities.ensurePositive;

import javax.measure.Unit;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import org.apache.sis.internal.jaxb.MetadataInfo;
import org.apache.sis.measure.ValueRange;
import org.opengis.metadata.content.Band;
import org.opengis.metadata.content.CoverageContentType;
import org.opengis.metadata.content.SampleDimension;
import org.opengis.metadata.content.TransferFunctionType;
import org.opengis.util.Record;
import org.opengis.util.RecordType;


/**
 * The characteristic of each dimension (layer) included in the resource.
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
 * @author  Remi Marechal 		(Geomatys)
 * @author  Martin Desruisseaux (Geomatys)
 * @author  Cullen Rombach		(Image Matters)
 * @version 0.5
 * @since   0.8
 * @module
 */
@XmlType(name = "MD_SampleDimension_Type", propOrder = {
    "maxValue",
    "minValue",
    "units",
	"xmlScaleFactor", 		// ISO 19115-3
	"xmlOffset",			// ISO 19115-3
	"xmlMeanValue",			// ISO 19115-3
	"xmlNumberOfValues",	// ISO 19115-3
	"xmlStandardDeviation",	// ISO 19115-3
	"xmlOtherPropertyType",	// ISO 19115-3
	"xmlOtherProperty",		// ISO 19115-3
	"xmlBitsPerValue"		// ISO 19115-3
})
@XmlRootElement(name = "MD_SampleDimension")
@XmlSeeAlso({DefaultBand.class, DefaultRangeDimension.class})
public class DefaultSampleDimension extends DefaultRangeDimension implements SampleDimension {
    /**
     * Serial number for inter-operability with different versions.
     */
    private static final long serialVersionUID = 4517148689016920767L;

    /**
     * Minimum value of data values in each dimension included in the resource.
     */
    private Double minValue;

    /**
     * Maximum value of data values in each dimension included in the resource.
     */
    private Double maxValue;

    /**
     * Mean value of data values in each dimension included in the resource.
     */
    private Double meanValue;

    /**
     * Number of values used in a thematicClassification resource.
     */
    private Integer numberOfValues;

    /**
     * Standard deviation of data values in each dimension included in the resource.
     */
    private Double standardDeviation;

    /**
     * Units of data in each dimension included in the resource.
     */
    private Unit<?> units;

    /**
     * Scale factor which has been applied to the cell value.
     */
    private Double scaleFactor;

    /**
     * Physical value corresponding to a cell value of zero.
     */
    private Double offset;

    /**
     * Type of transfer function to be used when scaling a physical value for a given element.
     */
    private TransferFunctionType transferFunctionType;

    /**
     * Maximum number of significant bits in the uncompressed representation
     * for the value in each band of each pixel.
     */
    private Integer bitsPerValue;

    /**
     * Smallest distance between which separate points can be distinguished, as specified in
     * instrument design.
     */
    private Double nominalSpatialResolution;

    /**
     * Type of other attribute description.
     */
    private RecordType otherPropertyType;

    /**
     * Instance of other/attributeType that defines attributes not explicitly
     * included in {@link CoverageContentType}.
     */
    private Record otherProperty;

    /**
     * Constructs an initially empty sample dimension.
     */
    public DefaultSampleDimension() {
    }

    /**
     * Constructs a new instance initialized with the values from the specified metadata object.
     * This is a <cite>shallow</cite> copy constructor, since the other metadata contained in the
     * given object are not recursively copied.
     *
     * <div class="note"><b>Note on properties validation:</b>
     * This constructor does not verify the property values of the given metadata (e.g. whether it contains
     * unexpected negative values). This is because invalid metadata exist in practice, and verifying their
     * validity in this copy constructor is often too late. Note that this is not the only hole, as invalid
     * metadata instances can also be obtained by unmarshalling an invalid XML document.
     * </div>
     *
     * @param object The metadata to copy values from, or {@code null} if none.
     *
     * @see #castOrCopy(SampleDimension)
     */
    public DefaultSampleDimension(final SampleDimension object) {
        super(object);
        if (object != null) {
            minValue                 = object.getMinValue();
            maxValue                 = object.getMaxValue();
            meanValue                = object.getMeanValue();
            numberOfValues           = object.getNumberOfValues();
            standardDeviation        = object.getStandardDeviation();
            units                    = object.getUnits();
            scaleFactor              = object.getScaleFactor();
            offset                   = object.getOffset();
            transferFunctionType     = object.getTransferFunctionType();
            bitsPerValue             = object.getBitsPerValue();
            nominalSpatialResolution = object.getNominalSpatialResolution();
            otherPropertyType        = object.getOtherPropertyType();
            otherProperty            = object.getOtherProperty();
        }
    }

    /**
     * Returns a SIS metadata implementation with the values of the given arbitrary implementation.
     * This method performs the first applicable action in the following choices:
     *
     * <ul>
     *   <li>If the given object is {@code null}, then this method returns {@code null}.</li>
     *   <li>Otherwise if the given object is is an instance of {@link Band}, then this
     *       method delegates to the {@code castOrCopy(…)} method of the corresponding SIS subclass.</li>
     *   <li>Otherwise if the given object is already an instance of
     *       {@code DefaultSampleDimension}, then it is returned unchanged.</li>
     *   <li>Otherwise a new {@code DefaultSampleDimension} instance is created using the
     *       {@linkplain #DefaultSampleDimension(SampleDimension) copy constructor}
     *       and returned. Note that this is a <cite>shallow</cite> copy operation, since the other
     *       metadata contained in the given object are not recursively copied.</li>
     * </ul>
     *
     * @param  object The object to get as a SIS implementation, or {@code null} if none.
     * @return A SIS implementation containing the values of the given object (may be the
     *         given object itself), or {@code null} if the argument was null.
     */
    public static DefaultSampleDimension castOrCopy(final SampleDimension object) {
        if (object instanceof Band) {
            return DefaultBand.castOrCopy((Band) object);
        }
        //-- Intentionally tested after the sub-interfaces.
        if (object == null || object instanceof DefaultSampleDimension) {
            return (DefaultSampleDimension) object;
        }
        return new DefaultSampleDimension(object);
    }

    /**
     * Returns the minimum value of data values in each dimension included in the resource.
     *
     * @return Minimum value of data values in each dimension included in the resource, or {@code null} if unspecified.
     */
    @Override
    @XmlElement(name = "minValue")
    public Double getMinValue() {
        return minValue;
    }

    /**
     * Sets the minimum value of data values in each dimension included in the resource.
     *
     * @param newValue The new new minimum value.
     */
    public void setMinValue(final Double newValue) {
        checkWritePermission();
        minValue = newValue;
    }

    /**
     * Returns the maximum value of data values in each dimension included in the resource.
     *
     * @return Maximum value of data values in each dimension included in the resource, or {@code null} if unspecified.
     */
    @Override
    @XmlElement(name = "maxValue")
    public Double getMaxValue() {
        return maxValue;
    }

    /**
     * Sets the maximum value of data values in each dimension included in the resource.
     *
     * @param newValue The new new maximum value.
     */
    public void setMaxValue(final Double newValue) {
        checkWritePermission();
        maxValue = newValue;
    }

    /**
     * Returns the mean value of data values in each dimension included in the resource.
     *
     * @return The mean value of data values in each dimension included in the resource, or {@code null} if none.
     */
    @Override
    public Double getMeanValue() {
        return meanValue;
    }

    /**
     * Sets the mean value of data values in each dimension included in the resource.
     *
     * @param newValue The new mean value of data values in each dimension included in the resource.
     */
    public void setMeanValue(final Double newValue) {
        checkWritePermission();
        meanValue = newValue;
    }
    
    /**
	 * Gets the mean value. Used by JAXB. (used in ISO 19115-3 format).
	 * @see {@link #getMeanValue}
	 */
	@XmlElement(name = "meanValue")
	private Double getXmlMeanValue() {
		return MetadataInfo.is2003() ? null : getMeanValue();
	}

	/**
	 * Sets the mean value. Used by JAXB. (used in ISO 19115-3 format).
	 * @see {@link #setMeanValue}
	 */
	@SuppressWarnings("unused")
	private void setXmlMeanValue(final Double newValue) {
		setMeanValue(newValue);
	}

    /**
     * Returns the number of values used in a thematic classification resource.
     *
     * @return The number of values used in a thematic classification resource, or {@code null} if none.
     */
    @Override
    @ValueRange(minimum = 0)
    public Integer getNumberOfValues() {
        return numberOfValues;
    }

    /**
     * Sets the number of values used in a thematic classification resource.
     *
     * @param newValue The new number of values used in a thematic classification resource.
     * @throws IllegalArgumentException if the given value is negative.
     */
    public void setNumberOfValues(final Integer newValue) {
        checkWritePermission();
        if (ensurePositive(DefaultSampleDimension.class, "numberOfValues", false, newValue)) {
            numberOfValues = newValue;
        }
    }
    
    /**
	 * Gets the number of values. Used by JAXB. (used in ISO 19115-3 format).
	 * @see {@link #getNumberOfValues}
	 */
    @ValueRange(minimum = 0)
	@XmlElement(name = "numberOfValues")
	private Integer getXmlNumberOfValues() {
		return MetadataInfo.is2003() ? null : getNumberOfValues();
	}

	/**
	 * Sets the number of values. Used by JAXB. (used in ISO 19115-3 format).
	 * @see {@link #setNumberOfValues}
	 */
	@SuppressWarnings("unused")
	private void setXmlNumberOfValues(final Integer newValue) {
		setNumberOfValues(newValue);
	}

    /**
     * Returns the standard deviation of data values in each dimension included in the resource.
     *
     * @return Standard deviation of data values in each dimension included in the resource, or {@code null} if none.
     */
    @Override
    public Double getStandardDeviation() {
        return standardDeviation;
    }

    /**
     * Sets the standard deviation of data values in each dimension included in the resource.
     *
     * @param newValue The new standard deviation of data values in each dimension included in the resource.
     */
    public void setStandardDeviation(final Double newValue) {
        checkWritePermission();
        standardDeviation = newValue;
    }
    
    /**
	 * Gets the standard deviation. Used by JAXB. (used in ISO 19115-3 format).
	 * @see {@link #getStandardDeviation}
	 */
	@XmlElement(name = "standardDeviation")
	private Double getXmlStandardDeviation() {
		return MetadataInfo.is2003() ? null : getStandardDeviation();
	}

	/**
	 * Sets the standard deviation. Used by JAXB. (used in ISO 19115-3 format).
	 * @see {@link #setStandardDeviation}
	 */
	@SuppressWarnings("unused")
	private void setXmlStandardDeviation(final Double newValue) {
		setStandardDeviation(newValue);
	}

    /**
     * Returns the units of data in the dimension.
     *
     * @return The units of data in the dimension, or {@code null} if unspecified.
     */
    @Override
    @XmlElement(name = "units")
    public Unit<?> getUnits() {
        return units;
    }

    /**
     * Sets the units of data in the dimension.
     *
     * @param newValue The new units of data in the dimension.
     */
    public void setUnits(final Unit<?> newValue) {
        checkWritePermission();
        units = newValue;
    }

    /**
     * Returns the scale factor which has been applied to the cell value.
     *
     * @return Scale factor which has been applied to the cell value, or {@code null} if none.
     */
    @Override
    public Double getScaleFactor() {
        return scaleFactor;
    }

    /**
     * Sets the scale factor which has been applied to the cell value.
     *
     * @param newValue The new scale factor which has been applied to the cell value.
     */
    public void setScaleFactor(final Double newValue) {
        checkWritePermission();
        scaleFactor = newValue;
    }
    
    /**
	 * Gets the scale factor. Used by JAXB. (used in ISO 19115-3 format).
	 * @see {@link #getScaleFactor}
	 */
	@XmlElement(name = "scaleFactor")
	private Double getXmlScaleFactor() {
		return MetadataInfo.is2003() ? null : getScaleFactor();
	}

	/**
	 * Sets the scale factor. Used by JAXB. (used in ISO 19115-3 format).
	 * @see {@link #setScaleFactor}
	 */
	@SuppressWarnings("unused")
	private void setXmlScaleFactor(final Double newValue) {
		setScaleFactor(newValue);
	}

    /**
     * Returns the physical value corresponding to a cell value of zero.
     *
     * @return The physical value corresponding to a cell value of zero, or {@code null} if none.
     */
    @Override
    public Double getOffset() {
        return offset;
    }

    /**
     * Sets the physical value corresponding to a cell value of zero.
     *
     * @param newValue The new physical value corresponding to a cell value of zero, or {@code null} if none..
     */
    public void setOffset(final Double newValue) {
        checkWritePermission();
        offset = newValue;
    }
    
    /**
	 * Gets the offset. Used by JAXB. (used in ISO 19115-3 format).
	 * @see {@link #getOffset}
	 */
	@XmlElement(name = "offset")
	private Double getXmlOffset() {
		return MetadataInfo.is2003() ? null : getOffset();
	}

	/**
	 * Sets the offset. Used by JAXB. (used in ISO 19115-3 format).
	 * @see {@link #setOffset}
	 */
	@SuppressWarnings("unused")
	private void setXmlOffset(final Double newValue) {
		setOffset(newValue);
	}

    /**
     * Returns type of transfer function to be used when scaling a physical value for a given element.
     *
     * @return Type of transfer function, or {@code null}.
     */
    @Override
    public TransferFunctionType getTransferFunctionType() {
        return transferFunctionType;
    }

    /**
     * Sets the type of transfer function to be used when scaling a physical value for a given element.
     *
     * @param newValue The new transfer function value.
     */
    public void setTransferFunctionType(final TransferFunctionType newValue) {
        checkWritePermission();
        transferFunctionType = newValue;
    }

    /**
     * Returns the maximum number of significant bits in the uncompressed representation
     * for the value in each band of each pixel.
     *
     * @return Maximum number of significant bits in the uncompressed representation
     *         for the value in each band of each pixel, or {@code null} if none.
     */
    @Override
    @ValueRange(minimum = 1)
    public Integer getBitsPerValue() {
        return bitsPerValue;
    }

    /**
     * Sets the maximum number of significant bits in the uncompressed representation
     * for the value in each band of each pixel.
     *
     * @param newValue The new maximum number of significant bits.
     * @throws IllegalArgumentException if the given value is zero or negative.
     */
    public void setBitsPerValue(final Integer newValue) {
        checkWritePermission();
        if (ensurePositive(DefaultSampleDimension.class, "bitsPerValue", true, newValue)) {
            bitsPerValue = newValue;
        }
    }
    
    /**
	 * Gets the bits per value. Used by JAXB. (used in ISO 19115-3 format).
	 * @see {@link #getBitsPerValue}
	 */
	@XmlElement(name = "bitsPerValue")
	private Integer getXmlBitsPerValue() {
		return MetadataInfo.is2003() ? null : getBitsPerValue();
	}

	/**
	 * Sets the bits per value. Used by JAXB. (used in ISO 19115-3 format).
	 * @see {@link #setBitsPerValue}
	 */
	@SuppressWarnings("unused")
	private void setXmlBitsPerValue(final Integer newValue) {
		setBitsPerValue(newValue);
	}

    /**
     * Returns the smallest distance between which separate points can be distinguished,
     * as specified in instrument design.
     *
     * @return Smallest distance between which separate points can be distinguished, or {@code null}.
     */
    @Override
    @ValueRange(minimum = 0, isMinIncluded = false)
    public Double getNominalSpatialResolution() {
        return nominalSpatialResolution;
    }

    /**
     * Sets the smallest distance between which separate points can be distinguished,
     * as specified in instrument design.
     *
     * @param newValue The new nominal spatial resolution.
     * @throws IllegalArgumentException if the given value is negative.
     */
    public void setNominalSpatialResolution(final Double newValue) {
        checkWritePermission();
        if (ensurePositive(DefaultSampleDimension.class, "nominalSpatialResolution", true, newValue)) {
            nominalSpatialResolution = newValue;
        }
    }

    /**
     * Returns type of other attribute description.
     *
     * @return Type of other attribute description, or {@code null} if none.
     */
    @Override
    public RecordType getOtherPropertyType() {
        return otherPropertyType;
    }

    /**
     * Sets a new type of other attribute description.
     *
     * @param newValue The new type of other attribute description.
     */
    public void setOtherPropertyType(final RecordType newValue) {
        checkWritePermission();
        otherPropertyType = newValue;
    }
    
    /**
	 * Gets the other property type. Used by JAXB. (used in ISO 19115-3 format).
	 * @see {@link #getOtherPropertyType}
	 */
	@XmlElement(name = "otherPropertyType")
	private RecordType getXmlOtherPropertyType() {
		return MetadataInfo.is2003() ? null : getOtherPropertyType();
	}

	/**
	 * Sets the other property type. Used by JAXB. (used in ISO 19115-3 format).
	 * @see {@link #setOtherPropertyType}
	 */
	@SuppressWarnings("unused")
	private void setXmlOtherPropertyType(final RecordType newValue) {
		setOtherPropertyType(newValue);
	}

    /**
     * Returns instance of other/attributeType that defines attributes not explicitly
     * included in {@link CoverageContentType}, or {@code null} if none.
     *
     * @return instance of other/attributeType that defines attributes, or {@code null} if none.
     */
    @Override
    public Record getOtherProperty() {
        return otherProperty;
    }

    /**
     * Sets a new instance of other/attributeType that defines attributes not explicitly
     * included in {@link CoverageContentType}.
     *
     * @param newValue The new instance of other/attributeType.
     */
    public void setOtherProperty(final Record newValue) {
        checkWritePermission();
        otherProperty = newValue;
    }
    
    /**
	 * Gets the other property. Used by JAXB. (used in ISO 19115-3 format).
	 * @see {@link #getOtherProperty}
	 */
	@XmlElement(name = "otherProperty")
	private Record getXmlOtherProperty() {
		return MetadataInfo.is2003() ? null : getOtherProperty();
	}

	/**
	 * Sets the other property. Used by JAXB. (used in ISO 19115-3 format).
	 * @see {@link #setOtherProperty}
	 */
	@SuppressWarnings("unused")
	private void setXmlOtherProperty(final Record newValue) {
		setOtherProperty(newValue);
	}
}
