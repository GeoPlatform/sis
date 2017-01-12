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
package org.apache.sis.internal.jaxb.code;

import java.util.Locale;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.apache.sis.xml.Namespaces;
import org.apache.sis.xml.ValueConverter;
import org.apache.sis.internal.jaxb.Context;
import org.apache.sis.internal.jaxb.gmd.CodeListUID;


/**
 * JAXB adapter for {@link Charset}, in order to integrate the value in an element
 * complying with ISO-19139 standard. See package documentation for more information about
 * the handling of {@code CodeList} in ISO-19139.
 *
 * @author  Cédric Briançon 	(Geomatys)
 * @author  Martin Desruisseaux (Geomatys)
 * @author  Cullen Rombach 		(Image Matters)
 * @since   0.3
 * @version 0.8
 * @module
 */
@XmlType(namespace = Namespaces.LAN)
public final class MD_CharacterSetCode extends XmlAdapter<MD_CharacterSetCode, Charset> {
    /**
     * The value of the {@link CodeList}.
     */
    private CodeListUID identifier;

    /**
     * Substitutes the adapter value read from an XML stream by the object which will
     * contains the value. JAXB calls automatically this method at unmarshalling time.
     *
     * @param  adapter The adapter for this metadata value.
     * @return A code list which represents the metadata value.
     */
    @Override
    public final Charset unmarshal(final MD_CharacterSetCode adapter) throws IllegalCharsetNameException {
        final Context context = Context.current();
        return Context.converter(context).toCharset(context, adapter.identifier.toString());
    }

    /**
     * Substitutes the code list by the adapter to be marshalled into an XML file
     * or stream. JAXB calls automatically this method at marshalling time.
     *
     * @param  value The code list value.
     * @return The adapter for the given code list.
     */
    @Override
    public final MD_CharacterSetCode marshal(final Charset value) {
        final Context context = Context.current();
        final ValueConverter converter = Context.converter(context);
        final String code = converter.toCharsetCode(context, value);
        if (code != null) {
            final Locale locale = context.getLocale();
            final MD_CharacterSetCode c = new MD_CharacterSetCode();
            c.identifier = new CodeListUID(context, "MD_CharacterSetCode", code,
                    (locale != null) ? converter.toLanguageCode(context, locale) : null,
                    (locale != null) ? value.displayName(locale) : value.displayName());
            return c;
        }
        return null;
    }

    /**
     * Invoked by JAXB on marshalling.
     *
     * @return The value to be marshalled.
     */
    @XmlElement(name = "MD_CharacterSetCode")
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
