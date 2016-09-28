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
package org.apache.sis.internal.referencing;

import java.net.URL;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import javax.annotation.Generated;
import org.opengis.util.InternationalString;
import org.apache.sis.util.resources.KeyConstants;
import org.apache.sis.util.resources.IndexedResourceBundle;
import org.apache.sis.util.resources.ResourceInternationalString;


/**
 * Warning and error messages that are specific to the {@code sis-referencing} module.
 *
 * @author  Martin Desruisseaux (IRD, Geomatys)
 * @since   0.8
 * @version 0.8
 * @module
 */
public final class Resources extends IndexedResourceBundle {
    /**
     * Resource keys. This class is used when compiling sources, but no dependencies to
     * {@code Keys} should appear in any resulting class files. Since the Java compiler
     * inlines final integer values, using long identifiers will not bloat the constant
     * pools of compiled classes.
     *
     * @author  Martin Desruisseaux (IRD, Geomatys)
     * @since   0.8
     * @module
     */
    @Generated("org.apache.sis.util.resources.IndexedResourceCompiler")
    public static final class Keys extends KeyConstants {
        /**
         * The unique instance of key constants handler.
         */
        static final Keys INSTANCE = new Keys();

        /**
         * For {@link #INSTANCE} creation only.
         */
        private Keys() {
        }

        /**
         * Ambiguity between inverse flattening and semi minor axis length for “{0}”. Using inverse
         * flattening.
         */
        public static final short AmbiguousEllipsoid_1 = 0;

        /**
         * Can not compute the coordinate operation derivative.
         */
        public static final short CanNotComputeDerivative = 14;

        /**
         * Can not concatenate transforms “{0}” and “{1}”.
         */
        public static final short CanNotConcatenateTransforms_2 = 15;

        /**
         * Can not map an axis from the specified coordinate system to the “{0}” direction.
         */
        public static final short CanNotMapAxisToDirection_1 = 16;

        /**
         * Target dimension {0} depends on excluded source dimensions.
         */
        public static final short CanNotSeparateTargetDimension_1 = 17;

        /**
         * Can not transform envelope to a geodetic reference system.
         */
        public static final short CanNotTransformEnvelopeToGeodetic = 18;

        /**
         * Can not use the {0} geodetic parameters: {1}
         */
        public static final short CanNotUseGeodeticParameters_2 = 19;

        /**
         * Axis directions {0} and {1} are colinear.
         */
        public static final short ColinearAxisDirections_2 = 20;

        /**
         * This result indicates if a datum shift method has been applied.
         */
        public static final short ConformanceMeansDatumShift = 1;

        /**
         * This parameter is shown for completeness, but should never have a value different than {0}
         * for this projection.
         */
        public static final short ConstantProjParameterValue_1 = 2;

        /**
         * Coordinate conversion of transformation from system “{0}” to “{1}” has not been found.
         */
        public static final short CoordinateOperationNotFound_2 = 21;

        /**
         * Origin of temporal datum shall be a date.
         */
        public static final short DatumOriginShallBeDate = 22;

        /**
         * Code “{0}” is deprecated and replaced by code {1}. Reason is: {2}
         */
        public static final short DeprecatedCode_3 = 3;

        /**
         * There is no factory for version {1} of “{0}” authority. Fallback on default version for
         * objects creation.
         */
        public static final short FallbackDefaultFactoryVersion_2 = 4;

        /**
         * {0} geodetic dataset version {1} on “{2}” version {3}.
         */
        public static final short GeodeticDataBase_4 = 5;

        /**
         * More than one service provider of type ‘{0}’ are declared for “{1}”. Only the first provider
         * (an instance of ‘{2}’) will be used.
         */
        public static final short IgnoredServiceProvider_3 = 6;

        /**
         * Coordinate system of class ‘{0}’ can not have axis in the {1} direction.
         */
        public static final short IllegalAxisDirection_2 = 23;

        /**
         * Dimensions of “{0}” operation can not be ({1} → {2}).
         */
        public static final short IllegalOperationDimension_3 = 24;

        /**
         * This operation can not be applied to values of class ‘{0}’.
         */
        public static final short IllegalOperationForValueClass_1 = 25;

        /**
         * Unit of measurement “{1}” is not valid for “{0}” values.
         */
        public static final short IllegalUnitFor_2 = 26;

        /**
         * Incompatible coordinate system types.
         */
        public static final short IncompatibleCoordinateSystemTypes = 27;

        /**
         * Datum of “{1}” shall be “{0}”.
         */
        public static final short IncompatibleDatum_2 = 28;

        /**
         * Inverse operation uses this parameter value with opposite sign.
         */
        public static final short InverseOperationUsesOppositeSign = 7;

        /**
         * Inverse operation uses the same parameter value.
         */
        public static final short InverseOperationUsesSameSign = 8;

        /**
         * Latitudes {0} and {1} are opposite.
         */
        public static final short LatitudesAreOpposite_2 = 29;

        /**
         * Loading datum shift file “{0}”.
         */
        public static final short LoadingDatumShiftFile_1 = 9;

        /**
         * The “{1}” parameter could have been omitted. But it has been given a value of {2} which does
         * not match the definition of the “{0}” ellipsoid.
         */
        public static final short MismatchedEllipsoidAxisLength_3 = 10;

        /**
         * No coordinate operation from “{0}” to “{1}” because of mismatched factories.
         */
        public static final short MismatchedOperationFactories_2 = 11;

        /**
         * Expected the “{0}” prime meridian but found “{1}”.
         */
        public static final short MismatchedPrimeMeridian_2 = 30;

        /**
         * The transform has {2} {0,choice,0#source|1#target} dimension{2,choice,1#|2#s}, while {1} was
         * expected.
         */
        public static final short MismatchedTransformDimension_3 = 31;

        /**
         * Despite its name, this parameter is effectively “{0}”.
         */
        public static final short MisnamedParameter_1 = 12;

        /**
         * No authority was specified for code “{0}”. The expected syntax is “AUTHORITY:CODE”.
         */
        public static final short MissingAuthority_1 = 32;

        /**
         * No horizontal dimension found in “{0}”.
         */
        public static final short MissingHorizontalDimension_1 = 33;

        /**
         * Not enough dimension in ‘MathTransform’ input or output coordinates for the interpolation
         * points.
         */
        public static final short MissingInterpolationOrdinates = 34;

        /**
         * No spatial or temporal dimension found in “{0}”
         */
        public static final short MissingSpatioTemporalDimension_1 = 35;

        /**
         * No temporal dimension found in “{0}”
         */
        public static final short MissingTemporalDimension_1 = 36;

        /**
         * No vertical dimension found in “{0}”
         */
        public static final short MissingVerticalDimension_1 = 37;

        /**
         * No convergence.
         */
        public static final short NoConvergence = 38;

        /**
         * No convergence for points {0} and {1}.
         */
        public static final short NoConvergenceForPoints_2 = 39;

        /**
         * No code “{2}” from authority “{0}” found for object of type ‘{1}’.
         */
        public static final short NoSuchAuthorityCode_3 = 40;

        /**
         * No operation method found for name or identifier “{0}”.
         */
        public static final short NoSuchOperationMethod_1 = 41;

        /**
         * Non invertible {0}×{1} matrix.
         */
        public static final short NonInvertibleMatrix_2 = 42;

        /**
         * Can not invert the “{0}” operation.
         */
        public static final short NonInvertibleOperation_1 = 43;

        /**
         * Transform is not invertible.
         */
        public static final short NonInvertibleTransform = 44;

        /**
         * Unit conversion from “{0}” to “{1}” is non-linear.
         */
        public static final short NonLinearUnitConversion_2 = 45;

        /**
         * The “{0}” sequence is not monotonic.
         */
        public static final short NonMonotonicSequence_1 = 46;

        /**
         * Axis directions {0} and {1} are not perpendicular.
         */
        public static final short NonPerpendicularDirections_2 = 47;

        /**
         * Scale is not uniform.
         */
        public static final short NonUniformScale = 48;

        /**
         * Matrix is not skew-symmetric.
         */
        public static final short NotASkewSymmetricMatrix = 49;

        /**
         * Transform is not affine.
         */
        public static final short NotAnAffineTransform = 50;

        /**
         * This parameter borrowed from the “{0}” projection is not formally a parameter of this
         * projection.
         */
        public static final short NotFormalProjectionParameter_1 = 13;

        /**
         * Matrix is singular.
         */
        public static final short SingularMatrix = 51;

        /**
         * Unexpected dimension for a coordinate system of type ‘{0}’.
         */
        public static final short UnexpectedDimensionForCS_1 = 52;

        /**
         * Authority “{0}” is unknown.
         */
        public static final short UnknownAuthority_1 = 53;

        /**
         * Axis direction “{0}” is unknown.
         */
        public static final short UnknownAxisDirection_1 = 54;

        /**
         * This affine transform is unmodifiable.
         */
        public static final short UnmodifiableAffineTransform = 55;

        /**
         * Dimensions have not been specified.
         */
        public static final short UnspecifiedDimensions = 56;
    }

    /**
     * Constructs a new resource bundle loading data from the given UTF file.
     *
     * @param resources  the path of the binary file containing resources, or {@code null} if
     *        there is no resources. The resources may be a file or an entry in a JAR file.
     */
    public Resources(final URL resources) {
        super(resources);
    }

    /**
     * Returns the handle for the {@code Keys} constants.
     *
     * @return a handler for the constants declared in the inner {@code Keys} class.
     */
    @Override
    protected KeyConstants getKeyConstants() {
        return Keys.INSTANCE;
    }

    /**
     * Returns resources in the given locale.
     *
     * @param  locale  the locale, or {@code null} for the default locale.
     * @return resources in the given locale.
     * @throws MissingResourceException if resources can't be found.
     */
    public static Resources getResources(final Locale locale) throws MissingResourceException {
        return getBundle(Resources.class, locale);
    }

    /**
     * Returns resources in the locale specified in the given property map. This convenience method looks
     * for the {@link #LOCALE_KEY} entry. If the given map is null, or contains no entry for the locale key,
     * or the value is not an instance of {@link Locale}, then this method fallback on the default locale.
     *
     * @param  properties  the map of properties, or {@code null} if none.
     * @return resources in the given locale.
     * @throws MissingResourceException if resources can't be found.
     */
    public static Resources getResources(final Map<?,?> properties) throws MissingResourceException {
        return getResources(getLocale(properties));
    }

    /**
     * Gets a string for the given key from this resource bundle or one of its parents.
     *
     * @param  key  the key for the desired string.
     * @return the string for the given key.
     * @throws MissingResourceException if no object for the given key can be found.
     */
    public static String format(final short key) throws MissingResourceException {
        return getResources((Locale) null).getString(key);
    }

    /**
     * Gets a string for the given key are replace all occurrence of "{0}"
     * with values of {@code arg0}.
     *
     * @param  key   the key for the desired string.
     * @param  arg0  value to substitute to "{0}".
     * @return the formatted string for the given key.
     * @throws MissingResourceException if no object for the given key can be found.
     */
    public static String format(final short  key,
                                final Object arg0) throws MissingResourceException
    {
        return getResources((Locale) null).getString(key, arg0);
    }

    /**
     * Gets a string for the given key are replace all occurrence of "{0}",
     * "{1}", with values of {@code arg0}, {@code arg1}.
     *
     * @param  key   the key for the desired string.
     * @param  arg0  value to substitute to "{0}".
     * @param  arg1  value to substitute to "{1}".
     * @return the formatted string for the given key.
     * @throws MissingResourceException if no object for the given key can be found.
     */
    public static String format(final short  key,
                                final Object arg0,
                                final Object arg1) throws MissingResourceException
    {
        return getResources((Locale) null).getString(key, arg0, arg1);
    }

    /**
     * Gets a string for the given key are replace all occurrence of "{0}",
     * "{1}", with values of {@code arg0}, {@code arg1}, etc.
     *
     * @param  key   the key for the desired string.
     * @param  arg0  value to substitute to "{0}".
     * @param  arg1  value to substitute to "{1}".
     * @param  arg2  value to substitute to "{2}".
     * @return the formatted string for the given key.
     * @throws MissingResourceException if no object for the given key can be found.
     */
    public static String format(final short  key,
                                final Object arg0,
                                final Object arg1,
                                final Object arg2) throws MissingResourceException
    {
        return getResources((Locale) null).getString(key, arg0, arg1, arg2);
    }

    /**
     * Gets a string for the given key are replace all occurrence of "{0}",
     * "{1}", with values of {@code arg0}, {@code arg1}, etc.
     *
     * @param  key   the key for the desired string.
     * @param  arg0  value to substitute to "{0}".
     * @param  arg1  value to substitute to "{1}".
     * @param  arg2  value to substitute to "{2}".
     * @param  arg3  value to substitute to "{3}".
     * @return the formatted string for the given key.
     * @throws MissingResourceException if no object for the given key can be found.
     */
    public static String format(final short  key,
                                final Object arg0,
                                final Object arg1,
                                final Object arg2,
                                final Object arg3) throws MissingResourceException
    {
        return getResources((Locale) null).getString(key, arg0, arg1, arg2, arg3);
    }

    /**
     * The international string to be returned by {@link formatInternational}.
     */
    private static final class International extends ResourceInternationalString {
        private static final long serialVersionUID = 4553487496835099424L;

        International(short key)                           {super(key);}
        International(short key, Object args)              {super(key, args);}
        @Override protected KeyConstants getKeyConstants() {return Keys.INSTANCE;}
        @Override protected IndexedResourceBundle getBundle(final Locale locale) {
            return getResources(locale);
        }
    }

    /**
     * Gets an international string for the given key. This method does not check for the key
     * validity. If the key is invalid, then a {@link MissingResourceException} may be thrown
     * when a {@link InternationalString#toString(Locale)} method is invoked.
     *
     * @param  key  the key for the desired string.
     * @return an international string for the given key.
     */
    public static InternationalString formatInternational(final short key) {
        return new International(key);
    }

    /**
     * Gets an international string for the given key. This method does not check for the key
     * validity. If the key is invalid, then a {@link MissingResourceException} may be thrown
     * when a {@link InternationalString#toString(Locale)} method is invoked.
     *
     * <div class="note"><b>API note:</b>
     * This method is redundant with the one expecting {@code Object...}, but avoid the creation
     * of a temporary array. There is no risk of confusion since the two methods delegate their
     * work to the same {@code format} method anyway.</div>
     *
     * @param  key  the key for the desired string.
     * @param  arg  values to substitute to "{0}".
     * @return an international string for the given key.
     */
    public static InternationalString formatInternational(final short key, final Object arg) {
        return new International(key, arg);
    }

    /**
     * Gets an international string for the given key. This method does not check for the key
     * validity. If the key is invalid, then a {@link MissingResourceException} may be thrown
     * when a {@link InternationalString#toString(Locale)} method is invoked.
     *
     * @param  key   the key for the desired string.
     * @param  args  values to substitute to "{0}", "{1}", <i>etc</i>.
     * @return an international string for the given key.
     */
    public static InternationalString formatInternational(final short key, final Object... args) {
        return new International(key, args);
    }
}
