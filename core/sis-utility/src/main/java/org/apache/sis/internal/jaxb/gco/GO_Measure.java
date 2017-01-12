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
package org.apache.sis.internal.jaxb.gco;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.apache.sis.internal.jaxb.gml.Measure;


/**
 * The ISO-19103 {@code Measure} with a unit of measure defined in the {@code gco} namespace
 * associated to the {@code http://www.isotc211.org/2005/gco} URL.
 *
 * <p>This class is identical to {@link GO_Distance} except for the name of the element,
 * which is {@code "Measure"}.</p>
 *
 * @author  Cédric Briançon (Geomatys)
 * @author  Martin Desruisseaux (Geomatys)
 * @since   0.3
 * @version 0.8
 * @module
 */
@XmlType(name = "Measure_PropertyType")
public final class GO_Measure extends XmlAdapter<GO_Measure, Measure> {
    /**
     * A proxy representation of the {@code <gco:Measure>} element.
     */
    @XmlElement(name = "Measure")
    private Measure measure;

    /**
     * Empty constructor used only by JAXB.
     */
    public GO_Measure() {
    }

    /**
     * Constructs an adapter for the given value before marshalling.
     *
     * @param value The value.
     *
     */
    private GO_Measure(final Measure value) {
        measure = value;
        measure.asXPointer = true;
    }

    /**
     * Allows JAXB to generate a Measure object using the value found in the adapter.
     *
     * @param value The value wrapped in an adapter.
     * @return The Measure object extracted from the adapter.
     */
    @Override
    public Measure unmarshal(final GO_Measure value) {
        if (value != null) {
            final Measure measure = value.measure;
            if (measure != null) {
                return measure;
            }
        }
        return null;
    }

    /**
     * Allows JAXB to change the result of the marshalling process, according to the
     * ISO-19139 standard and its requirements about {@code measures}.
     *
     * @param value The Measure value we want to integrate into a {@code <gco:Measure>} element.
     * @return A {@code <gco:Measure>} element, with an {@code uom} attribute.
     */
    @Override
    public GO_Measure marshal(final Measure value) {
        return (value != null) ? new GO_Measure(value) : null;
    }
}
