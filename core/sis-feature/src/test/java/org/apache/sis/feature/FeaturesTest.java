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
package org.apache.sis.feature;

import org.apache.sis.internal.feature.FeatureTypeBuilder;
import org.apache.sis.test.DependsOn;
import org.apache.sis.test.TestCase;
import org.junit.Test;

import static org.junit.Assert.*;
import org.opengis.feature.Feature;
import org.opengis.feature.FeatureType;
import org.opengis.feature.InvalidPropertyValueException;


/**
 * Tests {@link Features}.
 *
 * @author  Martin Desruisseaux (Geomatys)
 * @author  Johann Sorel (Geomatys)
 * @since   0.5
 * @version 0.7
 * @module
 */
@DependsOn(SingletonAttributeTest.class)
public final strictfp class FeaturesTest extends TestCase {
    /**
     * Tests {@link Features#cast(AttributeType, Class)}.
     */
    @Test
    public void testCastAttributeType() {
        final DefaultAttributeType<String> parliament = DefaultAttributeTypeTest.parliament();
        assertSame(parliament, Features.cast(parliament, String.class));
        try {
            Features.cast(parliament, CharSequence.class);
            fail("Shall not be allowed to cast to a different type.");
        } catch (ClassCastException e) {
            final String message = e.getMessage();
            assertTrue(message, message.contains("parliament"));
            assertTrue(message, message.contains("String"));
            assertTrue(message, message.contains("CharSequence"));
        }
    }

    /**
     * Tests {@link Features#cast(Attribute, Class)}.
     */
    @Test
    public void testCastAttributeInstance() {
        final AbstractAttribute<String> parliament = SingletonAttributeTest.parliament();
        assertSame(parliament, Features.cast(parliament, String.class));
        try {
            Features.cast(parliament, CharSequence.class);
            fail("Shall not be allowed to cast to a different type.");
        } catch (ClassCastException e) {
            final String message = e.getMessage();
            assertTrue(message, message.contains("parliament"));
            assertTrue(message, message.contains("String"));
            assertTrue(message, message.contains("CharSequence"));
        }
    }

    /**
     * Tests {@link Features#validate(org.opengis.feature.Feature) }.
     */
    @Test
    public void testValidate(){

        final FeatureTypeBuilder ftb = new FeatureTypeBuilder();
        ftb.setName("myScope","myName");
        ftb.addProperty("name", String.class,1,1,null);
        final FeatureType type = ftb.build();

        final Feature feature = type.newInstance();

        //should not pass validation
        try{
            Features.validate(feature);
            fail("Feature is unvalid, property name is missing, validation should have raised an exception.");
        }catch(InvalidPropertyValueException ex){
            //ok
        }

        //should pass validation
        feature.setPropertyValue("name", "hubert");
        Features.validate(feature);

    }

}
