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
package org.apache.sis.metadata.iso.identification;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.sis.internal.jaxb.MetadataInfo;
import org.apache.sis.internal.jaxb.code.PT_Locale;
import org.apache.sis.internal.metadata.OtherLocales;
import org.apache.sis.internal.util.CheckedArrayList;
import org.apache.sis.internal.util.CollectionsExt;
import org.opengis.metadata.citation.Citation;
import org.opengis.metadata.extent.Extent;
import org.opengis.metadata.identification.DataIdentification;
import org.opengis.metadata.identification.TopicCategory;
import org.opengis.util.InternationalString;


/**
 * Information required to identify a dataset.
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
 * @author  Cullen Rombach		(Image Matters)
 * @since   0.3
 * @version 0.8
 * @module
 */
@XmlType(name = "MD_DataIdentification_Type", propOrder = {
		"xmlLanguages",				// ISO 19139
		"xmlLanguageStrings",		// ISO 19139
		"xmlCharacterSets",			// ISO 19139
		"xmlDefaultLocale",			// ISO 19115-3
		"xmlOtherLocales",			// ISO 19115-3
		"xmlTopicCategory",			// Here in ISO 19139 (see AbstractIdentification for ISO 19115-3)
		"environmentDescription",
		"xmlExtent",				// Here in ISO 19139 (see AbstractIdentification for ISO 19115-3)
		"supplementalInformation"
})
@XmlRootElement(name = "MD_DataIdentification")
public class DefaultDataIdentification extends AbstractIdentification implements DataIdentification {
	/**
	 * Serial number for compatibility with different versions.
	 */
	private static final long serialVersionUID = 6104637930243499851L;

	/**
	 * Language(s) used within the dataset.
	 */
	private Collection<Locale> languages;

	/**
	 * Full name of the character coding standard used for the dataset.
	 */
	private Collection<Charset> characterSets;

	/**
	 * Description of the dataset in the producers processing environment, including items
	 * such as the software, the computer operating system, file name, and the dataset size
	 */
	private InternationalString environmentDescription;

	/**
	 * Any other descriptive information about the dataset.
	 */
	private InternationalString supplementalInformation;

	/**
	 * Constructs an initially empty data identification.
	 */
	public DefaultDataIdentification() {
	}

	/**
	 * Creates a data identification initialized to the specified values.
	 *
	 * @param citation      The citation data for the resource(s), or {@code null} if none.
	 * @param abstracts     A brief narrative summary of the content of the resource(s), or {@code null} if none.
	 * @param language      The language used within the dataset, or {@code null} if none.
	 * @param topicCategory The main theme of the dataset, or {@code null} if none.
	 */
	public DefaultDataIdentification(final Citation citation,
			final CharSequence abstracts,
			final Locale language,
			final TopicCategory topicCategory)
	{
		super(citation, abstracts);
		languages = singleton(language, Locale.class);
		super.setTopicCategories(singleton(topicCategory, TopicCategory.class));
	}

	/**
	 * Constructs a new instance initialized with the values from the specified metadata object.
	 * This is a <cite>shallow</cite> copy constructor, since the other metadata contained in the
	 * given object are not recursively copied.
	 *
	 * @param object The metadata to copy values from, or {@code null} if none.
	 *
	 * @see #castOrCopy(DataIdentification)
	 */
	public DefaultDataIdentification(final DataIdentification object) {
		super(object);
		if (object != null) {
			languages                  = copyCollection(object.getLanguages(), Locale.class);
			characterSets              = copyCollection(object.getCharacterSets(), Charset.class);
			environmentDescription     = object.getEnvironmentDescription();
			supplementalInformation    = object.getSupplementalInformation();
		}
	}

	/**
	 * Returns a SIS metadata implementation with the values of the given arbitrary implementation.
	 * This method performs the first applicable action in the following choices:
	 *
	 * <ul>
	 *   <li>If the given object is {@code null}, then this method returns {@code null}.</li>
	 *   <li>Otherwise if the given object is already an instance of
	 *       {@code DefaultDataIdentification}, then it is returned unchanged.</li>
	 *   <li>Otherwise a new {@code DefaultDataIdentification} instance is created using the
	 *       {@linkplain #DefaultDataIdentification(DataIdentification) copy constructor}
	 *       and returned. Note that this is a <cite>shallow</cite> copy operation, since the other
	 *       metadata contained in the given object are not recursively copied.</li>
	 * </ul>
	 *
	 * @param  object The object to get as a SIS implementation, or {@code null} if none.
	 * @return A SIS implementation containing the values of the given object (may be the
	 *         given object itself), or {@code null} if the argument was null.
	 */
	public static DefaultDataIdentification castOrCopy(final DataIdentification object) {
		if (object == null || object instanceof DefaultDataIdentification) {
			return (DefaultDataIdentification) object;
		}
		return new DefaultDataIdentification(object);
	}

	/**
	 * Returns the language(s) used for documenting metadata.
	 * The first element in iteration order is the default language.
	 * All other elements, if any, are alternate language(s) used within the resource.
	 *
	 * <p>Unless an other locale has been specified with the {@link org.apache.sis.xml.XML#LOCALE} property,
	 * this {@code DefaultMetadata} instance and its children will use the first locale returned by this method
	 * for marshalling {@link org.opengis.util.InternationalString} and {@link org.opengis.util.CodeList} instances
	 * in ISO 19115-2 compliant XML documents.
	 *
	 * @return Language(s) used for documenting metadata.
	 *
	 * @since 0.5
	 */
	@Override
	public Collection<Locale> getLanguages() {
		return languages = nonNullCollection(languages, Locale.class);
	}

	/**
	 * Sets the language(s) used for documenting metadata.
	 * The first element in iteration order shall be the default language.
	 * All other elements, if any, are alternate language(s) used within the resource.
	 *
	 * @param newValues The new languages.
	 *
	 * @see org.apache.sis.xml.XML#LOCALE
	 *
	 * @since 0.5
	 */
	public void setLanguages(final Collection<Locale> newValues) {
		languages = writeCollection(newValues, languages, Locale.class);
		languages = writeCollection(newValues, languages, Locale.class);
		// The "magic" applying this language to every child
		// is performed by the 'beforeMarshal(Marshaller)' method.
	}

	/**
	 * Gets the languages for this identification (used in ISO 19139 format).
	 * This method is used for unmarshalling only.
	 * @see {@link #getLanguages}
	 */
	@XmlElement(name = "language")
	private Collection<Locale> getXmlLanguages() {
		if(MetadataInfo.isUnmarshalling()) {
			return getLanguages();
		}
		return new CheckedArrayList<>(Locale.class);
	}
	
	/**
	 * Gets the language for this record as a string (used in ISO 19139 format).
	 * This method pair is used for marshalling in JAXB only.
	 * @see {@link #getLanguage}
	 */
	@XmlElement(name = "language")
	private Collection<String> getXmlLanguageStrings() {
		Collection<String> formattedOutputs = new ArrayList<String>();
		for(Locale language : getLanguages()) {
			String lang = language.getISO3Language();
			String country = language.getISO3Country();
			String formattedOutput = lang + "; " + country;
			formattedOutputs.add(formattedOutput);
		}
		return MetadataInfo.is2014() ? new CheckedArrayList<>(String.class) : formattedOutputs;
	}

	/**
	 * Gets the default locale for this record (used in ISO 19115-3 format).
	 * @see {@link #getLanguages}
	 */
	@XmlElement(name = "defaultLocale")
	@XmlJavaTypeAdapter(PT_Locale.class)
	private Locale getXmlDefaultLocale() {
		return MetadataInfo.is2003() ? null : CollectionsExt.first(getLanguages());
	}

	/**
	 * Sets the default locale for this record (used in ISO 19115-3 format).
	 * @see {@link #setLanguages}
	 */
	@SuppressWarnings("unused")
	private void setXmlDefaultLocale(Locale newValue) {
		checkWritePermission();
        setLanguages(OtherLocales.setFirst(languages, newValue));
	}

	/**
	 * Gets the other locales for this record (used in ISO 19115-3 format).
	 * Used to write in
	 * @see {@link #getLanguages}
	 */
	@XmlElement(name = "otherLocale")
	@XmlJavaTypeAdapter(PT_Locale.class)
	private Collection<Locale> getXmlOtherLocales() {
		if(MetadataInfo.isUnmarshalling()) {
			return OtherLocales.filter(getLanguages());
		}
		return MetadataInfo.is2003() ? new CheckedArrayList<>(Locale.class) : OtherLocales.filter(getLanguages());
	}

	/**
	 * Returns the character coding standard used for the dataset.
	 *
	 * @return Character coding standard(s) used.
	 */
	@Override
	public Collection<Charset> getCharacterSets() {
		return characterSets = nonNullCollection(characterSets, Charset.class);
	}

	/**
	 * Sets the character coding standard used for the dataset.
	 *
	 * @param newValues The new character sets.
	 */
	public void setCharacterSets(final Collection<? extends Charset> newValues) {
		characterSets = writeCollection(newValues, characterSets, Charset.class);
	}

	/**
	 * Gets the character sets for this identification (used in ISO 19139 format).
	 * @see {@link #getCharacterSets}
	 */
	@XmlElement(name = "characterSet")
	private Collection<Charset> getXmlCharacterSets() {
		if(MetadataInfo.isUnmarshalling()) {
			return getCharacterSets();
		}
		return MetadataInfo.is2014() ? new CheckedArrayList<>(Charset.class) : getCharacterSets();
	}

	/**
	 * Returns a description of the resource in the producer's processing environment. This includes
	 * items such as the software, the computer operating system, file name, and the dataset size.
	 *
	 * @return Description of the resource in the producer's processing environment, or {@code null}.
	 */
	@Override
	@XmlElement(name = "environmentDescription")
	public InternationalString getEnvironmentDescription() {
		return environmentDescription;
	}

	/**
	 * Sets the description of the resource in the producers processing environment.
	 *
	 * @param newValue The new environment description.
	 */
	public void setEnvironmentDescription(final InternationalString newValue)  {
		checkWritePermission();
		environmentDescription = newValue;
	}

	/**
	 * Any other descriptive information about the resource.
	 *
	 * @return Other descriptive information, or {@code null}.
	 */
	@Override
	@XmlElement(name = "supplementalInformation")
	public InternationalString getSupplementalInformation() {
		return supplementalInformation;
	}

	/**
	 * Sets any other descriptive information about the resource.
	 *
	 * @param newValue The new supplemental information.
	 */
	public void setSupplementalInformation(final InternationalString newValue) {
		checkWritePermission();
		supplementalInformation = newValue;
	}

	/**
	 * Gets the topic categories for this identification (used in ISO 19139 format).
	 * @see {@link #getTopicCategories}
	 */
	@XmlElement(name = "topicCategory")
	private Collection<TopicCategory> getXmlTopicCategory() {
		if(MetadataInfo.isUnmarshalling()) {
			return getTopicCategories();
		}
		return MetadataInfo.is2014() ? new CheckedArrayList<>(TopicCategory.class) : getTopicCategories();
	}

	/**
	 * Gets the character sets for this identification (used in ISO 19139 format).
	 * @see {@link #getExtent}
	 */
	@XmlElement(name = "extent")
	private Collection<Extent> getXmlExtent() {
		if(MetadataInfo.isUnmarshalling()) {
			return getExtents();
		}
		return MetadataInfo.is2014() ? new CheckedArrayList<>(Extent.class) : getExtents();
	}
}
