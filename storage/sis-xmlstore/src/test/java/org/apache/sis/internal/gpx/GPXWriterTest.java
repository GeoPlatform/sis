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

import java.io.File;
import java.net.URI;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.io.IOException;
import javax.xml.stream.XMLStreamException;
import com.esri.core.geometry.Point;
import org.opengis.metadata.extent.GeographicBoundingBox;
import org.apache.sis.metadata.iso.extent.DefaultGeographicBoundingBox;
import org.apache.sis.storage.DataStoreException;
import org.apache.sis.storage.StorageConnector;
import org.apache.sis.test.TestCase;
import org.junit.Test;

import static org.junit.Assert.*;

// Branch-dependent imports
import org.opengis.feature.Feature;


/**
 * GPX Writer tests.
 *
 * @author  Johann Sorel (Geomatys)
 * @since   0.7
 * @version 0.7
 * @module
 */
public final strictfp class GPXWriterTest extends TestCase{


    private static GPXReader create(final File resource) throws DataStoreException, IOException, XMLStreamException {
        StorageConnector storage = new StorageConnector(resource);
        return new GPXReader(storage.getStorage(), storage);
    }

    /**
     * Test writing gpx metadata.
     *
     * @throws Exception
     */
    @Test
    public void testWritingMetadata() throws Exception {
        final File f = new File("output.xml");
        f.deleteOnExit();
        if (f.exists()) f.delete();
        final GPXWriter110 writer = new GPXWriter110("Apache SIS", f);

        final Person person = new Person();
        person.name = "Jean-Pierre";
        person.email = "jean-pierre@test.com";
        person.link = new URI("http://son-site.com");

        final Copyright copyright = new Copyright();
        copyright.author = "GNU";
        copyright.year = 2010;
        copyright.license = new URI("http://gnu.org");

        final GeographicBoundingBox bounds = new DefaultGeographicBoundingBox(-10, 20, -30, 40);

        final Metadata metaData = new Metadata();
        metaData.name = "name";
        metaData.description = "description";
        metaData.author = person;
        metaData.copyright = copyright;
        metaData.links.addAll(Arrays.asList(new URI("http://adress1.org"),new URI("http://adress2.org")));
        metaData.time = Instant.now();
        metaData.keywords = "test,sample";
        metaData.bounds = bounds;

        writer.writeStartDocument();
        writer.write(metaData, null, null, null);
        writer.writeEndDocument();
        writer.close();

        try (GPXReader reader = create(f)) {
            assertEquals(metaData, reader.getMetadata());
        }

        if (f.exists()) f.delete();
    }

    /**
     * Test writing the various gpx feature types.
     *
     * @throws Exception
     */
    @Test
    public void testWritingFeatures() throws Exception {
        final Types types = Types.DEFAULT;

        final File f = new File("output.xml");
        f.deleteOnExit();
        if (f.exists()) f.delete();
        final GPXWriter110 writer = new GPXWriter110("Apache SIS", f);

        //way points -----------------------------------------------------------
        Feature point1 = types.wayPoint.newInstance();
        point1.setPropertyValue("@identifier", 0);
        point1.setPropertyValue("@geometry", new Point(-10, 10));
        point1.setPropertyValue("ele", 15.6);
        point1.setPropertyValue("time", LocalDate.now());
        point1.setPropertyValue("magvar", 31.7);
        point1.setPropertyValue("geoidheight", 45.1);
        point1.setPropertyValue("name", "fds");
        point1.setPropertyValue("cmt", "fdrt");
        point1.setPropertyValue("desc", "ffe");
        point1.setPropertyValue("src", "aaz");
        point1.setPropertyValue("link", Collections.singletonList(new URI("http://test.com")));
        point1.setPropertyValue("sym", "fdsg");
        point1.setPropertyValue("type", "klj");
        point1.setPropertyValue("fix", "yy");
        point1.setPropertyValue("sat", 12);
        point1.setPropertyValue("hdop", 45.2);
        point1.setPropertyValue("vdop", 16.7);
        point1.setPropertyValue("pdop", 14.3);
        point1.setPropertyValue("ageofdgpsdata", 78.9);
        point1.setPropertyValue("dgpsid", 6);
        Feature point2 = types.wayPoint.newInstance();
        point2.setPropertyValue("@identifier", 1);
        point2.setPropertyValue("@geometry", new Point(-15, 15));
        point2.setPropertyValue("ele", 15.6);
        point2.setPropertyValue("time", LocalDate.now());
        point2.setPropertyValue("magvar", 31.7);
        point2.setPropertyValue("geoidheight", 45.1);
        point2.setPropertyValue("name", "fds");
        point2.setPropertyValue("cmt", "fdrt");
        point2.setPropertyValue("desc", "ffe");
        point2.setPropertyValue("src", "aaz");
        point2.setPropertyValue("link", Collections.singletonList(new URI("http://test.com")));
        point2.setPropertyValue("sym", "fdsg");
        point2.setPropertyValue("type", "klj");
        point2.setPropertyValue("fix", "yy");
        point2.setPropertyValue("sat", 12);
        point2.setPropertyValue("hdop", 45.2);
        point2.setPropertyValue("vdop", 16.7);
        point2.setPropertyValue("pdop", 14.3);
        point2.setPropertyValue("ageofdgpsdata", 78.9);
        point2.setPropertyValue("dgpsid", 6);
        Feature point3 = types.wayPoint.newInstance();
        point3.setPropertyValue("@identifier", 2);
        point3.setPropertyValue("@geometry", new Point(-20, 20));
        point3.setPropertyValue("ele", 15.6);
        point3.setPropertyValue("time", LocalDate.now());
        point3.setPropertyValue("magvar", 31.7);
        point3.setPropertyValue("geoidheight", 45.1);
        point3.setPropertyValue("name", "fds");
        point3.setPropertyValue("cmt", "fdrt");
        point3.setPropertyValue("desc", "ffe");
        point3.setPropertyValue("src", "aaz");
        point3.setPropertyValue("link", Collections.singletonList(new URI("http://test.com")));
        point3.setPropertyValue("sym", "fdsg");
        point3.setPropertyValue("type", "klj");
        point3.setPropertyValue("fix", "yy");
        point3.setPropertyValue("sat", 12);
        point3.setPropertyValue("hdop", 45.2);
        point3.setPropertyValue("vdop", 16.7);
        point3.setPropertyValue("pdop", 14.3);
        point3.setPropertyValue("ageofdgpsdata", 78.9);
        point3.setPropertyValue("dgpsid", 6);

        final List<Feature> wayPoints = new ArrayList<>();
        wayPoints.add(point1);
        wayPoints.add(point2);
        wayPoints.add(point3);

        //routes ---------------------------------------------------------------
        final Feature route1 = types.route.newInstance();
        route1.setPropertyValue("@identifier", 0);
        route1.setPropertyValue("name", "tt");
        route1.setPropertyValue("cmt", "cc");
        route1.setPropertyValue("desc", "des");
        route1.setPropertyValue("src", "src");
        route1.setPropertyValue("link", Collections.singletonList(new URI("http://test.com")));
        route1.setPropertyValue("number", 15);
        route1.setPropertyValue("type", "test");
        route1.setPropertyValue("rtept", wayPoints);
        final Feature route2 = types.route.newInstance();
        route2.setPropertyValue("@identifier", 1);
        route2.setPropertyValue("name", "tt2");
        route2.setPropertyValue("cmt", "cc2");
        route2.setPropertyValue("desc", "des2");
        route2.setPropertyValue("src", "src2");
        route2.setPropertyValue("link", Collections.singletonList(new URI("http://test2.com")));
        route2.setPropertyValue("number", 15);
        route2.setPropertyValue("type", "test2");
        route2.setPropertyValue("rtept", wayPoints);

        final List<Feature> routes = new ArrayList<>();
        routes.add(route1);
        routes.add(route2);

        //tracks ---------------------------------------------------------------
        final List<Feature> segments = new ArrayList<>();
        final Feature seg1 = types.trackSegment.newInstance();
        seg1.setPropertyValue("@identifier", 0);
        seg1.setPropertyValue("trkpt", wayPoints);
        final Feature seg2 = types.trackSegment.newInstance();
        seg2.setPropertyValue("@identifier", 1);
        seg2.setPropertyValue("trkpt", wayPoints);
        final Feature seg3 = types.trackSegment.newInstance();
        seg3.setPropertyValue("@identifier", 2);
        seg3.setPropertyValue("trkpt", wayPoints);

        final Feature track1 = types.track.newInstance();
        track1.setPropertyValue("@identifier", 0);
        track1.setPropertyValue("name", "tc");
        track1.setPropertyValue("cmt", "cc");
        track1.setPropertyValue("desc", "des");
        track1.setPropertyValue("src", "src");
        track1.setPropertyValue("link", Collections.singletonList(new URI("http://test4.com")));
        track1.setPropertyValue("number", 15);
        track1.setPropertyValue("type", "test");
        track1.setPropertyValue("trkseg", segments);
        final Feature track2 = types.track.newInstance();
        track2.setPropertyValue("@identifier", 1);
        track2.setPropertyValue("name", "tc2");
        track2.setPropertyValue("cmt", "cc2");
        track2.setPropertyValue("desc", "des2");
        track2.setPropertyValue("src", "src2");
        track2.setPropertyValue("link", Collections.singletonList(new URI("http://test5.com")));
        track2.setPropertyValue("number", 15);
        track2.setPropertyValue("type", "test2");
        track2.setPropertyValue("trkseg", segments);

        final List<Feature> tracks = new ArrayList<>();
        tracks.add(track1);
        tracks.add(track2);


        writer.writeStartDocument();
        writer.write(null, wayPoints, routes, tracks);
        writer.writeEndDocument();
        writer.close();

        final GPXReader reader = create(f);

        //testing on toString since JTS geometry always fail on equals method.
        assertEquals(point1.toString(), reader.next().toString());
        assertEquals(point2.toString(), reader.next().toString());
        assertEquals(point3.toString(), reader.next().toString());
        assertEquals(route1.toString(), reader.next().toString());
        assertEquals(route2.toString(), reader.next().toString());
        assertEquals(track1.toString(), reader.next().toString());
        assertEquals(track2.toString(), reader.next().toString());
        assertFalse(reader.hasNext());

        reader.close();

        if (f.exists()) f.delete();
    }


}