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
package org.apache.sis.internal.gpx;

import java.net.URI;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.time.temporal.Temporal;
import com.esri.core.geometry.Point;
import org.opengis.util.LocalName;
import org.opengis.util.NameFactory;
import org.opengis.util.FactoryException;
import org.apache.sis.referencing.CommonCRS;
import org.apache.sis.feature.AbstractIdentifiedType;
import org.apache.sis.feature.DefaultAttributeType;
import org.apache.sis.feature.DefaultFeatureType;
import org.apache.sis.feature.DefaultAssociationRole;
import org.apache.sis.feature.FeatureOperations;
import org.apache.sis.feature.builder.FeatureTypeBuilder;
import org.apache.sis.feature.builder.AttributeRole;
import org.apache.sis.internal.feature.AttributeConvention;
import org.apache.sis.internal.system.DefaultFactories;
import org.apache.sis.util.iso.DefaultNameFactory;
import org.apache.sis.util.Static;

import static org.apache.sis.internal.gpx.GPXConstants.*;

// Branch-dependent imports
import org.opengis.feature.FeatureType;
import org.opengis.feature.PropertyType;


/**
 * Feature types that may appear in GPX files. All values defined in this class are immutable and can be shared
 * by many {@link GPXReader} instances. There is usually only one {@code Types} instance for a running JVM, but
 * we nevertheless allows definition of alternative {@code Types} with names created by different factories.
 *
 * @author  Johann Sorel (Geomatys)
 * @since   0.8
 * @version 0.8
 * @module
 */
final class Types extends Static {
    /**
     * Waypoint GPX feature type.
     */
    final FeatureType wayPoint;

    /**
     * Track GPX feature type.
     */
    final FeatureType track;

    /**
     * Route GPX feature type.
     */
    final FeatureType route;

    /**
     * Track segment GPX feature type.
     */
    final FeatureType trackSegment;

    /**
     * A system-wide instance for {@code FeatureType} instances created using the {@link DefaultNameFactory}.
     * This is normally the only instance used in an application.
     */
    static final Types DEFAULT;
    static {
        try {
            DEFAULT = new Types(DefaultFactories.forBuildin(NameFactory.class, DefaultNameFactory.class), null);
        } catch (FactoryException e) {
            throw new AssertionError(e);    // Should never happen with DefaultNameFactory implementation.
        }
    }

    /**
     * Creates new {@code FeatureTypes} with feature names and property names created using the given factory.
     *
     * @param  factory   the factory to use for creating names, or {@code null} for the default factory.
     * @param  locale    the locale to use for formatting error messages, or {@code null} for the default locale.
     * @throws FactoryException if an error occurred while creating an "envelope bounds" operation.
     */
    Types(final NameFactory factory, final Locale locale) throws FactoryException {
        final LocalName     geomName = AttributeConvention.GEOMETRY_PROPERTY;
        final Map<String,?> geomInfo = Collections.singletonMap(AbstractIdentifiedType.NAME_KEY, geomName);
        final Map<String,?> envpInfo = Collections.singletonMap(AbstractIdentifiedType.NAME_KEY, AttributeConvention.ENVELOPE_PROPERTY);
        /*
         * The parent of all FeatureTypes to be created in this constructor.
         * This parent has a single property, "@identifier" of type Integer,
         * which is not part of GPX specification.
         *
         * http://www.topografix.com:GPXEntity
         * ┌─────────────┬─────────┬─────────────┐
         * │ Name        │ Type    │ Cardinality │
         * ├─────────────┼─────────┼─────────────┤
         * │ @identifier │ Integer │   [1 … 1]   │      SIS-specific property
         * └─────────────┴─────────┴─────────────┘
         */
        FeatureTypeBuilder builder = new FeatureTypeBuilder(null, factory, locale);
        builder.setDefaultScope(GPX_NAMESPACE).setName("GPXEntity").setAbstract(true);
        builder.addAttribute(Integer.class).setName(AttributeConvention.IDENTIFIER_PROPERTY);
        final FeatureType parent = builder.build();
        /*
         * http://www.topografix.com:WayPoint ⇾ GPXEntity
         * ┌───────────────┬──────────┬────────────────────────┬─────────────┐
         * │ Name          │ Type     │ XML type               │ Cardinality │
         * ├───────────────┼──────────┼────────────────────────┼─────────────┤
         * │ @identifier   │ Integer  │                        │   [1 … 1]   │
         * │ @envelope     │ Envelope │                        │   [1 … 1]   │
         * │ @geometry     │ Point    │ (lat,lon) attributes   │   [1 … 1]   │
         * │ ele           │ Double   │ xsd:decimal            │   [0 … 1]   │
         * │ time          │ Temporal │ xsd:dateTime           │   [0 … 1]   │
         * │ magvar        │ Double   │ gpx:degreesType        │   [0 … 1]   │
         * │ geoidheight   │ Double   │ xsd:decimal            │   [0 … 1]   │
         * │ name          │ String   │ xsd:string             │   [0 … 1]   │
         * │ cmt           │ String   │ xsd:string             │   [0 … 1]   │
         * │ desc          │ String   │ xsd:string             │   [0 … 1]   │
         * │ src           │ String   │ xsd:string             │   [0 … 1]   │
         * │ link          │ URI      │ gpx:linkType           │   [0 … ∞]   │
         * │ sym           │ String   │ xsd:string             │   [0 … 1]   │
         * │ type          │ String   │ xsd:string             │   [0 … 1]   │
         * │ fix           │ String   │ gpx:fixType            │   [0 … 1]   │
         * │ sat           │ Integer  │ xsd:nonNegativeInteger │   [0 … 1]   │
         * │ hdop          │ Double   │ xsd:decimal            │   [0 … 1]   │
         * │ vdop          │ Double   │ xsd:decimal            │   [0 … 1]   │
         * │ pdop          │ Double   │ xsd:decimal            │   [0 … 1]   │
         * │ ageofdgpsdata │ Double   │ xsd:decimal            │   [0 … 1]   │
         * │ dgpsid        │ Integer  │ gpx:dgpsStationType    │   [0 … 1]   │
         * └───────────────┴──────────┴────────────────────────┴─────────────┘
         */
        builder = new FeatureTypeBuilder(null, factory, locale);
        builder.setDefaultScope(GPX_NAMESPACE).setName("WayPoint").setSuperTypes(parent);
        builder.addAttribute(Point.class).setName(geomName)
                .setCRS(CommonCRS.WGS84.normalizedGeographic())
                .addRole(AttributeRole.DEFAULT_GEOMETRY);
        builder.setDefaultCardinality(0, 1);
        builder.addAttribute(Double  .class).setName(TAG_WPT_ELE);
        builder.addAttribute(Temporal.class).setName(TAG_WPT_TIME);
        builder.addAttribute(Double  .class).setName(TAG_WPT_MAGVAR);
        builder.addAttribute(Double  .class).setName(TAG_WPT_GEOIHEIGHT);
        builder.addAttribute(String  .class).setName(TAG_NAME);
        builder.addAttribute(String  .class).setName(TAG_CMT);
        builder.addAttribute(String  .class).setName(TAG_DESC);
        builder.addAttribute(String  .class).setName(TAG_SRC);
        builder.addAttribute(URI     .class).setName(TAG_LINK).setMaximumOccurs(Integer.MAX_VALUE);
        builder.addAttribute(String  .class).setName(TAG_WPT_SYM);
        builder.addAttribute(String  .class).setName(TAG_TYPE);
        builder.addAttribute(String  .class).setName(TAG_WPT_FIX);
        builder.addAttribute(Integer .class).setName(TAG_WPT_SAT);
        builder.addAttribute(Double  .class).setName(TAG_WPT_HDOP);
        builder.addAttribute(Double  .class).setName(TAG_WPT_VDOP);
        builder.addAttribute(Double  .class).setName(TAG_WPT_PDOP);
        builder.addAttribute(Double  .class).setName(TAG_WPT_AGEOFGPSDATA);
        builder.addAttribute(Integer .class).setName(TAG_WPT_DGPSID);
        wayPoint = builder.build();
        /*
         * http://www.topografix.com:Route ⇾ GPXEntity
         * ┌─────────────┬──────────┬────────────────────────┬─────────────┐
         * │ Name        │ Type     │ XML type               │ Cardinality │
         * ├─────────────┼──────────┼────────────────────────┼─────────────┤
         * │ @identifier │ Integer  │                        │   [1 … 1]   │
         * │ @envelope   │ Envelope │                        │   [1 … 1]   │
         * │ @geometry   │ Polyline │                        │   [1 … 1]   │
         * │ name        │ String   │ xsd:string             │   [0 … 1]   │
         * │ cmt         │ String   │ xsd:string             │   [0 … 1]   │
         * │ desc        │ String   │ xsd:string             │   [0 … 1]   │
         * │ src         │ String   │ xsd:string             │   [0 … 1]   │
         * │ link        │ URI      │ gpx:linkType           │   [0 … ∞]   │
         * │ number      │ Integer  │ xsd:nonNegativeInteger │   [0 … 1]   │
         * │ type        │ String   │ xsd:string             │   [0 … 1]   │
         * │ rtept       │ WayPoint │ gpx:wptType            │   [0 … ∞]   │
         * └─────────────┴──────────┴────────────────────────┴─────────────┘
         */
        final DefaultAssociationRole attWayPoints = new DefaultAssociationRole(
                identification(factory, TAG_RTE_RTEPT), wayPoint, 0, Integer.MAX_VALUE);
        PropertyType[] properties = {
                wayPoint.getProperty(TAG_NAME),
                wayPoint.getProperty(TAG_CMT),
                wayPoint.getProperty(TAG_DESC),
                wayPoint.getProperty(TAG_SRC),
                wayPoint.getProperty(TAG_LINK),
                new DefaultAttributeType<>(identification(factory, TAG_NUMBER), Integer.class, 0, 1, null),
                wayPoint.getProperty(TAG_TYPE),
                attWayPoints,
                new GroupPointsAsPolylineOperation(geomInfo, TAG_RTE_RTEPT, geomName.toString()),
                null
        };
        properties[properties.length - 1] = FeatureOperations.envelope(envpInfo, null, properties);
        route = new DefaultFeatureType(identification(factory, "Route"), false,
                new FeatureType[] {parent}, properties);
        /*
         * http://www.topografix.com:TrackSegment ⇾ GPXEntity
         * ┌─────────────┬──────────┬─────────────┬─────────────┐
         * │ Name        │ Type     │ XML type    │ Cardinality │
         * ├─────────────┼──────────┼─────────────┼─────────────┤
         * │ @identifier │ Integer  │             │   [1 … 1]   │
         * │ @envelope   │ Envelope │             │   [1 … 1]   │
         * │ @geometry   │ Polyline │             │   [1 … 1]   │
         * │ trkpt       │ WayPoint │ gpx:wptType │   [0 … ∞]   │
         * └─────────────┴──────────┴─────────────┴─────────────┘
         */
        final DefaultAssociationRole attTrackPoints = new DefaultAssociationRole(
                identification(factory, TAG_TRK_SEG_PT), wayPoint, 0, Integer.MAX_VALUE);
        properties = new PropertyType[] {
                attTrackPoints,
                new GroupPointsAsPolylineOperation(geomInfo, TAG_TRK_SEG_PT, geomName.toString()),
                null
        };
        properties[properties.length - 1] = FeatureOperations.envelope(envpInfo, null, properties);
        trackSegment = new DefaultFeatureType(identification(factory, "TrackSegment"), false,
                new FeatureType[] {parent}, properties);
        /*
         * http://www.topografix.com:Track ⇾ GPXEntity
         * ┌─────────────┬──────────────┬────────────────────────┬─────────────┐
         * │ Name        │ Type         │ XML type               │ Cardinality │
         * ├─────────────┼──────────────┼────────────────────────┼─────────────┤
         * │ @identifier │ Integer      │                        │   [1 … 1]   │
         * │ @envelope   │ Envelope     │                        │   [1 … 1]   │
         * │ @geometry   │ Polyline     │                        │   [1 … 1]   │
         * │ name        │ String       │ xsd:string             │   [0 … 1]   │
         * │ cmt         │ String       │ xsd:string             │   [0 … 1]   │
         * │ desc        │ String       │ xsd:string             │   [0 … 1]   │
         * │ src         │ String       │ xsd:string             │   [0 … 1]   │
         * │ link        │ URI          │ gpx:linkType           │   [0 … ∞]   │
         * │ number      │ Integer      │ xsd:nonNegativeInteger │   [0 … 1]   │
         * │ type        │ String       │ xsd:string             │   [0 … 1]   │
         * │ trkseg      │ TrackSegment │ gpx:trksegType         │   [0 … ∞]   │
         * └─────────────┴──────────────┴────────────────────────┴─────────────┘
         */
        final DefaultAssociationRole attTrackSegments = new DefaultAssociationRole(
                identification(factory, TAG_TRK_SEG), trackSegment, 0, Integer.MAX_VALUE);
        properties = new PropertyType[] {
                route.getProperty(TAG_NAME),
                route.getProperty(TAG_CMT),
                route.getProperty(TAG_DESC),
                route.getProperty(TAG_SRC),
                route.getProperty(TAG_LINK),
                route.getProperty(TAG_NUMBER),
                route.getProperty(TAG_TYPE),
                attTrackSegments,
                new GroupPolylinesOperation(geomInfo, TAG_TRK_SEG, geomName.toString()),
                null
        };
        properties[properties.length - 1] = FeatureOperations.envelope(envpInfo, null, properties);
        track = new DefaultFeatureType(identification(factory, "Track"), false,
                new FeatureType[] {parent}, properties);
    }

    private static Map<String,?> identification(final NameFactory factory, final String localPart) {
        return Collections.singletonMap(AbstractIdentifiedType.NAME_KEY,
                factory.createGenericName(null, GPX_NAMESPACE, localPart));
    }
}