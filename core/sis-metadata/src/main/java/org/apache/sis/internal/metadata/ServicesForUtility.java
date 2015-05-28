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
package org.apache.sis.internal.metadata;

import java.util.Arrays;
import org.opengis.metadata.citation.Role;
import org.opengis.metadata.citation.Citation;
import org.opengis.metadata.citation.PresentationForm;
import org.apache.sis.internal.simple.SimpleCitation;
import org.apache.sis.internal.simple.SimpleIdentifier;
import org.apache.sis.internal.util.Constants;
import org.apache.sis.internal.util.MetadataServices;
import org.apache.sis.metadata.iso.citation.Citations;
import org.apache.sis.metadata.iso.citation.DefaultCitation;
import org.apache.sis.metadata.iso.citation.DefaultOrganisation;
import org.apache.sis.metadata.iso.citation.DefaultResponsibility;
import org.apache.sis.util.iso.SimpleInternationalString;


/**
 * Implements the metadata services needed by the {@code "sis-utility"} module.
 *
 * @author  Martin Desruisseaux (Geomatys)
 * @since   0.6
 * @version 0.6
 * @module
 */
public final class ServicesForUtility extends MetadataServices {
    /**
     * Creates a new instance. This constructor is invoked by reflection only.
     */
    public ServicesForUtility() {
    }

    /**
     * Returns the constant defined in the {@link Citations} class for the given name.
     *
     * @param  name The name of one of the citation constants defined in the {@code Citations} class.
     * @return The requested citation, or {@code null} if there is no constant for the given name.
     */
    @Override
    public Citation getCitationConstant(final String name) {
        final Citation c = Citations.fromName(name);
        /*
         * The fact that the following line uses the citation class as a non-public criterion for identifying
         * when the Citations.fromName(String) method found no match is documented in that Citations.fromName
         * method body. If we do not rely anymore on this criterion, please update the Citations.fromName(…)
         * comment accordingly.
         */
        return (c.getClass() != SimpleCitation.class) ? c : null;
    }

    /**
     * Returns the build-in citation for the given primary key, or {@code null}.
     *
     * @param  key The primary key of the desired citation.
     * @return The requested citation, or {@code null} if unknown.
     *
     * @todo The content is hard-coded for now. But the plan for a future version is to fetch richer information
     *       from a database, including for example the responsible party and the URL. However that method would
     *       need to make sure that the given key is present in the alternate titles, since we rely on that when
     *       checking for code spaces.
     */
    @Override
    public Citation createCitation(final String key) {
        String   title;              // Title of the Citation to create.
        int      storeKeyAs = 1;     // 0 to not store the key, 1 to store as alternate title, 2 to store as identifier.
        Citation authority  = null;  // If the key is stored as an identifier (storeKeyAs = 2), the authority to use.
        switch (key) {
            case "ISO"          : title = "International Organization for Standardization"; break;
            case Constants.OGC  : title = "Open Geospatial Consortium"; break;
            case Constants.IOGP : title = "International Association of Oil & Gas producers"; break;
            case Constants.EPSG : title = "EPSG Geodetic Parameter Dataset"; storeKeyAs = 2; authority = Citations.OGP; break;
            case Constants.SIS  : title = "Apache Spatial Information System"; storeKeyAs = 2; break;
            case "ISBN"         : title = "International Standard Book Number"; break;
            case "ISSN"         : title = "International Standard Serial Number"; break;
            case "Proj4"        : title = "Proj.4"; storeKeyAs = 0; break;
            case "S57"          : title = "S-57"; storeKeyAs = 0; break;
            default: return super.createCitation(key);
        }
        final DefaultCitation c = new DefaultCitation(title);
        switch (storeKeyAs) {
            case 1: c.getAlternateTitles().add(new SimpleInternationalString(key)); break;
            case 2: c.getIdentifiers().add(new SimpleIdentifier(authority, key, false)); break;
        }
        /*
         * Additional information other than the given 'key' argument. All identifiers added here shall also be
         * understood by Citations.fromName(String) method. Constants are declared below this method for making
         * easier to track those special cases.
         */
        switch (key) {
            /*
             * More complete information is provided as an ISO 19115 structure
             * in EPSG Surveying and Positioning Guidance Note Number 7, part 1.
             */
            case Constants.EPSG: {
                final DefaultOrganisation organisation = new DefaultOrganisation();
                organisation.setName(Citations.OGP.getTitle());
                c.getCitedResponsibleParties().add(new DefaultResponsibility(Role.PRINCIPAL_INVESTIGATOR, null, organisation));
                c.getPresentationForms().add(PresentationForm.TABLE_DIGITAL);
                break;
            }
            /*
             * "OGP" and "IOGP" are used as value in the GML 'codeSpace' attribute.
             * From this point of view, we can see them as identifiers.
             */
            case Constants.IOGP: {
                c.setIdentifiers(Arrays.asList(new SimpleIdentifier[] {
                    new SimpleIdentifier(null, Constants.IOGP, false),
                    new SimpleIdentifier(null, OGP, true)   // Existed before "IOGP".
                }));
                break;
            }
        }
        c.freeze();
        return c;
    }

    /**
     * The legacy name of {@linkplain org.apache.sis.metadata.iso.citation.Citations#IOGP},
     * to be declared as an additional identifier by {@link #createCitation(String)}.
     *
     * <p>Search for usages of this constant in order to locate where a special processing
     * is done for managing the relationship between OGP, IOGP and EPSG identifiers.</p>
     */
    public static final String OGP = "OGP";
}