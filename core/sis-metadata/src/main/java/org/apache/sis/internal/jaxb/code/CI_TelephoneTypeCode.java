package org.apache.sis.internal.jaxb.code;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.sis.internal.jaxb.gmd.CodeListAdapter;
import org.apache.sis.internal.jaxb.gmd.CodeListUID;
import org.apache.sis.xml.Namespaces;
import org.opengis.metadata.citation.TelephoneType;

/**
 * JAXB adapter for {@link TelephoneType}, in order to integrate the value in an element respecting
 * the ISO-19139 standard. See package documentation for more information about the handling
 * of {@code CodeList} in ISO-19139.
 *
 * @author  Cullen Rombach	(Image Matters)
 * @since   0.8
 * @version 0.8
 * @module
 */
@XmlType(namespace = Namespaces.CIT)
public final class CI_TelephoneTypeCode extends CodeListAdapter<CI_TelephoneTypeCode, TelephoneType> {

	/**
     * Empty constructor for JAXB only.
     */
    public CI_TelephoneTypeCode() {
    }

    /**
     * Creates a new adapter for the given value.
     */
    private CI_TelephoneTypeCode(final CodeListUID value) {
        super(value);
    }

    /**
     * {@inheritDoc}
     *
     * @return The wrapper for the code list value.
     */
    @Override
    protected CI_TelephoneTypeCode wrap(final CodeListUID value) {
        return new CI_TelephoneTypeCode(value);
    }

    /**
     * {@inheritDoc}
     *
     * @return The code list class.
     */
    @Override
    protected Class<TelephoneType> getCodeListClass() {
        return TelephoneType.class;
    }

    /**
     * Invoked by JAXB on marshalling.
     *
     * @return The value to be marshalled.
     */
    @Override
    @XmlElement(name = "CI_TelephoneTypeCode")
    public CodeListUID getElement() {
        return identifier;
    }

    /**
     * Invoked by JAXB on unmarshalling.
     *
     * @param value The unmarshalled value.
     */
    public void setElement(final CodeListUID value) {
        identifier = value;
    }

}
