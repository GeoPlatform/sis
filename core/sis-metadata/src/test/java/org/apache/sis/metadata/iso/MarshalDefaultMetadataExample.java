package org.apache.sis.metadata.iso;

import static org.junit.Assert.assertNull;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.LogRecord;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.sis.internal.jaxb.gml.Measure;
import org.apache.sis.internal.jaxb.gmx.Anchor;
import org.apache.sis.internal.jaxb.metadata.replace.ReferenceSystemMetadata;
import org.apache.sis.measure.Units;
import org.apache.sis.metadata.iso.citation.DefaultAddress;
import org.apache.sis.metadata.iso.citation.DefaultCitation;
import org.apache.sis.metadata.iso.citation.DefaultCitationDate;
import org.apache.sis.metadata.iso.citation.DefaultContact;
import org.apache.sis.metadata.iso.citation.DefaultIndividual;
import org.apache.sis.metadata.iso.citation.DefaultOnlineResource;
import org.apache.sis.metadata.iso.citation.DefaultOrganisation;
import org.apache.sis.metadata.iso.citation.DefaultResponsibility;
import org.apache.sis.metadata.iso.citation.DefaultTelephone;
import org.apache.sis.metadata.iso.constraint.DefaultConstraints;
import org.apache.sis.metadata.iso.constraint.DefaultReleasability;
import org.apache.sis.metadata.iso.content.DefaultAttributeGroup;
import org.apache.sis.metadata.iso.content.DefaultCoverageDescription;
import org.apache.sis.metadata.iso.content.DefaultFeatureCatalogueDescription;
import org.apache.sis.metadata.iso.content.DefaultRangeDimension;
import org.apache.sis.metadata.iso.content.DefaultSampleDimension;
import org.apache.sis.metadata.iso.distribution.DefaultFormat;
import org.apache.sis.metadata.iso.extent.DefaultExtent;
import org.apache.sis.metadata.iso.extent.DefaultGeographicBoundingBox;
import org.apache.sis.metadata.iso.extent.DefaultTemporalExtent;
import org.apache.sis.metadata.iso.identification.DefaultAssociatedResource;
import org.apache.sis.metadata.iso.identification.DefaultBrowseGraphic;
import org.apache.sis.metadata.iso.identification.DefaultCoupledResource;
import org.apache.sis.metadata.iso.identification.DefaultDataIdentification;
import org.apache.sis.metadata.iso.identification.DefaultKeywordClass;
import org.apache.sis.metadata.iso.identification.DefaultKeywords;
import org.apache.sis.metadata.iso.identification.DefaultOperationMetadata;
import org.apache.sis.metadata.iso.identification.DefaultResolution;
import org.apache.sis.metadata.iso.identification.DefaultServiceIdentification;
import org.apache.sis.metadata.iso.identification.DefaultUsage;
import org.apache.sis.metadata.iso.maintenance.DefaultMaintenanceInformation;
import org.apache.sis.metadata.iso.maintenance.DefaultScope;
import org.apache.sis.metadata.iso.maintenance.DefaultScopeDescription;
import org.apache.sis.metadata.iso.spatial.DefaultDimension;
import org.apache.sis.metadata.iso.spatial.DefaultGeorectified;
import org.apache.sis.test.XMLTestCase;
import org.apache.sis.util.iso.DefaultRecordSchema;
import org.apache.sis.util.iso.SimpleInternationalString;
import org.apache.sis.util.logging.WarningListener;
import org.apache.sis.xml.MarshallerPool;
import org.apache.sis.xml.Namespaces;
import org.apache.sis.xml.XML;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opengis.annotation.Obligation;
import org.opengis.geometry.primitive.Point;
import org.opengis.metadata.Datatype;
import org.opengis.metadata.ExtendedElementInformation;
import org.opengis.metadata.Identifier;
import org.opengis.metadata.MetadataExtensionInformation;
import org.opengis.metadata.MetadataScope;
import org.opengis.metadata.citation.Address;
import org.opengis.metadata.citation.Citation;
import org.opengis.metadata.citation.CitationDate;
import org.opengis.metadata.citation.DateType;
import org.opengis.metadata.citation.OnLineFunction;
import org.opengis.metadata.citation.OnlineResource;
import org.opengis.metadata.citation.Party;
import org.opengis.metadata.citation.Responsibility;
import org.opengis.metadata.citation.Role;
import org.opengis.metadata.citation.TelephoneType;
import org.opengis.metadata.constraint.Constraints;
import org.opengis.metadata.content.AttributeGroup;
import org.opengis.metadata.content.ContentInformation;
import org.opengis.metadata.content.CoverageContentType;
import org.opengis.metadata.content.RangeDimension;
import org.opengis.metadata.distribution.Format;
import org.opengis.metadata.extent.Extent;
import org.opengis.metadata.extent.GeographicExtent;
import org.opengis.metadata.extent.TemporalExtent;
import org.opengis.metadata.identification.AssociatedResource;
import org.opengis.metadata.identification.AssociationType;
import org.opengis.metadata.identification.BrowseGraphic;
import org.opengis.metadata.identification.CoupledResource;
import org.opengis.metadata.identification.CouplingType;
import org.opengis.metadata.identification.DataIdentification;
import org.opengis.metadata.identification.DistributedComputingPlatform;
import org.opengis.metadata.identification.Identification;
import org.opengis.metadata.identification.InitiativeType;
import org.opengis.metadata.identification.KeywordType;
import org.opengis.metadata.identification.Keywords;
import org.opengis.metadata.identification.OperationMetadata;
import org.opengis.metadata.identification.Progress;
import org.opengis.metadata.identification.Resolution;
import org.opengis.metadata.identification.TopicCategory;
import org.opengis.metadata.identification.Usage;
import org.opengis.metadata.maintenance.MaintenanceFrequency;
import org.opengis.metadata.maintenance.MaintenanceInformation;
import org.opengis.metadata.maintenance.Scope;
import org.opengis.metadata.maintenance.ScopeCode;
import org.opengis.metadata.maintenance.ScopeDescription;
import org.opengis.metadata.spatial.CellGeometry;
import org.opengis.metadata.spatial.Dimension;
import org.opengis.metadata.spatial.DimensionNameType;
import org.opengis.metadata.spatial.PixelOrientation;
import org.opengis.metadata.spatial.SpatialRepresentation;
import org.opengis.metadata.spatial.SpatialRepresentationType;
import org.opengis.referencing.ReferenceSystem;
import org.opengis.temporal.Duration;
import org.opengis.util.InternationalString;
import org.opengis.util.RecordType;

/**
 * Simple test cases for marshalling a {@link DefaultMetadata} object to an XML file.
 * This class is used to test the ISO 19115-3 metadata standard implementation.
 * 
 * @author Cullen Rombach (Image Matters)
 */
public class MarshalDefaultMetadataExample extends XMLTestCase implements WarningListener<Object> {

	/**
	 * The marshaller used to handle marshalling the created DefaultMetadata object.
	 */
	private Marshaller marshaller;

	/**
	 * The pool from which the marshaller is pulled.
	 */
	private MarshallerPool pool;
	/**
	 * The resource key for the message of the warning that occurred while unmarshalling a XML fragment,
	 * or {@code null} if none.
	 */
	private Object resourceKey;

	/**
	 * The parameter of the warning that occurred while unmarshalling a XML fragment, or {@code null} if none.
	 */
	private Object[] parameters;

	/**
	 * The output file to which the DefaultMetadata object will be marshaled.
	 */
	private final File output191153 = new File("src/test/resources/org/apache/sis/metadata/iso/generated19115Metadata.xml");
	private final File output19139 = new File("src/test/resources/org/apache/sis/metadata/iso/generated19139Metadata.xml");

	@Before
	public void setUp() {
		try {
			pool = getMarshallerPool();
			marshaller  = pool.acquireMarshaller();
			marshaller.setEventHandler(new javax.xml.bind.helpers.DefaultValidationEventHandler());
			marshaller.setProperty(XML.WARNING_LISTENER, this);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Indented for readability.
	 * @throws JAXBException
	 * @throws URISyntaxException
	 */
	@SuppressWarnings("deprecation")
	@Test
	public void testMarshal() throws JAXBException, URISyntaxException {
		// Create DefaultMetadata object.
		DefaultMetadata md = new DefaultMetadata();

		// Metadata Identifier
		DefaultIdentifier id = new DefaultIdentifier("fileIdentifier");
		id.setCodeSpace("fileIdentifierNamespace");
		md.setMetadataIdentifier(id);

		// Languages
		Collection<Locale> languages = new ArrayList<Locale>();
		languages.add(new Locale("eng", "US"));
		languages.add(new Locale("ger", "DE"));
		md.setLanguages(languages);
		
		// Character Sets (characterEncoding)
		Collection<Charset> charSets = new ArrayList<Charset>();
		charSets.add(Charset.forName("utf8"));
		md.setCharacterSets(charSets);

		// Parent Metadata
		DefaultCitation parent = new DefaultCitation("parentMetadata");
			//... Parent ID
			DefaultIdentifier parentId = new DefaultIdentifier("parentMetadata");
			parentId.setCodeSpace("parentMetadataCodeSpace");
			Collection<Identifier> parentIds = new HashSet<Identifier>();
			parentIds.add(parentId);
			parent.setIdentifiers(parentIds);
			//.... Add parent metadata to root.
			md.setParentMetadata(parent);

		// mdb:metadataScope (hierarchyLevel and hierarchyLevelName in ISO 19139)
		Collection<MetadataScope> scopes = new HashSet<MetadataScope>();
		scopes.add(new DefaultMetadataScope(ScopeCode.DATASET, "hierarchyLevelName"));
		md.setMetadataScopes(scopes);

		// Contacts
		Collection<Responsibility> responsibilities = new HashSet<Responsibility>();
		DefaultResponsibility resp = new DefaultResponsibility(Role.POINT_OF_CONTACT, null, null);
		DefaultResponsibility resp2 = new DefaultResponsibility(Role.POINT_OF_CONTACT, null, null);
			//.... Create contact information for the parties.
			DefaultContact contact = new DefaultContact();
				//........ Telephone information (voice and fax)
				Collection<DefaultTelephone> phones = new HashSet<DefaultTelephone>();
				phones.add(new DefaultTelephone("555-867-5309", TelephoneType.VOICE));
				phones.add(new DefaultTelephone("555-555-5555", TelephoneType.FACSIMILE));
				contact.setPhones(phones); 
				//........ Address information
				Collection<Address> addresses = new HashSet<Address>();
				DefaultAddress address = new DefaultAddress();
					//............ delivery points
					Collection<InternationalString> deliveryPoints = new HashSet<InternationalString>();
					deliveryPoints.add(new SimpleInternationalString("deliveryPoint"));
					address.setDeliveryPoints(deliveryPoints);
					//............ Email addresses
					Collection<String> emails = new HashSet<String>();
					emails.add("test@example.com");
					address.setElectronicMailAddresses(emails);
					//............ Other, string-only address properties.
					address.setCity(new SimpleInternationalString("city"));
					address.setAdministrativeArea(new SimpleInternationalString("administrativeArea"));
					address.setPostalCode("postalCode");
					address.setCountry(new SimpleInternationalString("country"));
					//............ Add the addresses to the contacts.
					addresses.add(address);
					contact.setAddresses(addresses);
				//........ Online resources
				Collection<OnlineResource> onlineResources = new HashSet<OnlineResource>();
				DefaultOnlineResource onlineResource = new DefaultOnlineResource();
				onlineResource.setLinkage(new URI("http://example.com"));
				onlineResource.setProtocol("protocol");
				onlineResource.setApplicationProfile("applicationProfile");
				onlineResource.setName(new SimpleInternationalString("name"));
				onlineResource.setDescription(new SimpleInternationalString("description"));
				onlineResource.setFunction(OnLineFunction.DOWNLOAD);
				//............ Add the online resources to the contacts
				onlineResources.add(onlineResource);
				contact.setOnlineResources(onlineResources);
				//........ Hours of service
				Collection<InternationalString> hours = new HashSet<InternationalString>();
				hours.add(new SimpleInternationalString("Weekdays 9:00 AM - 5:00 PM"));
				contact.setHoursOfService(hours);
				//........ Contact instructions
				contact.setContactInstructions(new SimpleInternationalString("contactInstructions"));
				//........ Contact type
				contact.setContactType(new SimpleInternationalString("contactType"));
			//.... Create some DefaultIndividuals
			DefaultIndividual individual = new DefaultIndividual("individualName", "positionName", null);
			DefaultIndividual individual2 = new DefaultIndividual("individualName2", "positionName2", contact);
			//.... Create parties.
			Collection<Party> parties = new HashSet<Party>();
			Collection<Party> parties2 = new HashSet<Party>();
			//.... Create organization
			DefaultOrganisation org = new DefaultOrganisation("organisationName", null, individual, contact);
			// Sort organizations and individuals into parties collections.
			parties.add(org);
			parties2.add(individual2);
			//.... Add the created parties to the list of responsibilities.
			resp.setParties(parties);
			resp2.setParties(parties2);
			responsibilities.add(resp);
			responsibilities.add(resp2);
			//.... Assign the contacts to the metadata.
			md.setContacts(responsibilities);
		
		// Date info (date stamp in ISO 19139)
		Collection<CitationDate> dateInfo = new HashSet<CitationDate>();
		DefaultCitationDate date = new DefaultCitationDate(new Date(), DateType.CREATION);
		dateInfo.add(date);
		md.setDateInfo(dateInfo);
		
		// Metadata standard
		Collection<Citation> standards = new HashSet<Citation>();
		DefaultCitation standard = new DefaultCitation("metadataStandardName");
		standard.setEdition(new SimpleInternationalString("metadataStandardVersion"));
		standards.add(standard);
		md.setMetadataStandards(standards);
		
		// Spatial Representation Info
		Collection<SpatialRepresentation> repInfo = new HashSet<SpatialRepresentation>();
		DefaultGeorectified georectified = new DefaultGeorectified();
		georectified.setNumberOfDimensions(2);
			//.... Dimensions
			List<Dimension> dimensions = new ArrayList<Dimension>();
			DefaultDimension dim1 = new DefaultDimension(DimensionNameType.ROW, 7777);
			DefaultDimension dim2 = new DefaultDimension(DimensionNameType.COLUMN, 2233);
			Measure res1 = new Measure(10.0, Units.DEGREE);
			Measure res2 = new Measure(5.0, Units.METRE);
			dim1.setResolutionMeasure(res1);
			dim2.setResolutionMeasure(res2);
			dimensions.add(dim1);
			dimensions.add(dim2);
			georectified.setAxisDimensionProperties(dimensions);
			//.... Cell geometry
			georectified.setCellGeometry(CellGeometry.AREA);
			//... Corner points TODO: Figure out how to build a point object.
			List<Point> corners = new ArrayList<Point>();
			georectified.setCornerPoints(corners);
			//.... Point in pixel
			georectified.setPointInPixel(PixelOrientation.UPPER_RIGHT);
			//.... Add representation info to the metadata object
			repInfo.add(georectified);
			md.setSpatialRepresentationInfo(repInfo);
		
		// Reference System Information
		Collection<ReferenceSystem> refSystems = new HashSet<ReferenceSystem>();
		ReferenceSystemMetadata refSystem = new ReferenceSystemMetadata();
			//.... Citation
			DefaultCitation refCit = new DefaultCitation();
			refCit.setTitle(new SimpleInternationalString("refSystemCitationTitle"));
			refCit.setDates(dateInfo);
				//........ Responsibilities
				Collection<Responsibility> responsibilities2 = new HashSet<Responsibility>();
				DefaultOrganisation refCitOrg = new DefaultOrganisation();
				refCitOrg.setName(new SimpleInternationalString("orgName"));
				DefaultResponsibility refCitResp = new DefaultResponsibility(Role.PUBLISHER, null, refCitOrg);
				responsibilities2.add(refCitResp);
				refCit.setCitedResponsibleParties(responsibilities2);
			//.... Identifier
			DefaultIdentifier refId = new DefaultIdentifier("refSystemCode");
			refId.setAuthority(refCit);
			refId.setCodeSpace("refSystemCodeSpace");
			refId.setVersion("1.0");
			refId.setDescription(new SimpleInternationalString("refSystemDescription"));
			refSystem.setName(refId);
			//.... Add reference system information to metadata object
			refSystems.add(refSystem);
			md.setReferenceSystemInfo(refSystems);
		
		// Metadata extension information.
		Collection<MetadataExtensionInformation> extensions = new HashSet<MetadataExtensionInformation>();
		DefaultMetadataExtensionInformation extension = new DefaultMetadataExtensionInformation();
			//.... Extension online resource
			extension.setExtensionOnLineResource(onlineResource);
			//.... Extended element information
			Collection<ExtendedElementInformation> elementInfos = new HashSet<ExtendedElementInformation>();
			DefaultExtendedElementInformation elementInfo = new DefaultExtendedElementInformation();
			elementInfo.setName("extendedElementInfoName");
			elementInfo.setDefinition(new SimpleInternationalString("definition"));
			elementInfo.setObligation(Obligation.MANDATORY);
			elementInfo.setCondition(new SimpleInternationalString("condition"));
			elementInfo.setDataType(Datatype.META_CLASS);
			elementInfo.setMaximumOccurrence(1);
			elementInfo.setDomainValue(new SimpleInternationalString("domainValue"));
			elementInfo.setShortName("shortName");
			elementInfo.setDomainCode(1234);
				//........ Parent entity
				Collection<String> parentEntities = new HashSet<String>();
				parentEntities.add("parentEntity");
				elementInfo.setParentEntity(parentEntities);
				elementInfo.setRule(new SimpleInternationalString("rule"));
				elementInfo.setRationale(new SimpleInternationalString("rationale"));
				elementInfos.add(elementInfo);
				extension.setExtendedElementInformation(elementInfos);
				extensions.add(extension);
			//.... Add extension info to metadata object.
			md.setMetadataExtensionInfo(extensions);
		
		// Data identification info
		Collection<Identification> idInfos = new HashSet<Identification>();
		DefaultDataIdentification dataId = new DefaultDataIdentification();
		dataId.setAbstract(new SimpleInternationalString("abstract"));
		dataId.setPurpose(new SimpleInternationalString("purpose"));
			//.... Extents
			Collection<Extent> extents = new HashSet<Extent>();
			DefaultExtent extent = new DefaultExtent();
				//........ Description
				extent.setDescription(new SimpleInternationalString("description"));
				//........ Geographic elements
				Collection<GeographicExtent> geoElements = new HashSet<GeographicExtent>();
					//............ Bounding box
					DefaultGeographicBoundingBox boundingBox = new DefaultGeographicBoundingBox();
					boundingBox.setInclusion(true);
					boundingBox.setNorthBoundLatitude(11.11);
					boundingBox.setEastBoundLongitude(11.11);
					boundingBox.setSouthBoundLatitude(11.11);
					boundingBox.setWestBoundLongitude(11.11);
					geoElements.add(boundingBox);
				extent.setGeographicElements(geoElements);
				//........ Temporal elements
				Collection<TemporalExtent> tempElements = new HashSet<TemporalExtent>();
					//............ Extent
					DefaultTemporalExtent tempExtent = new DefaultTemporalExtent();
					//tempExtent.setBounds(new Date(2016, 1, 1),  new Date());
					tempElements.add(tempExtent);
				extent.setTemporalElements(tempElements);
				//........ Vertical elements
			extents.add(extent);
			dataId.setExtents(extents);
			//.... Resource constraints
			Collection<Constraints> resourceConstraints = new HashSet<Constraints>();
			DefaultConstraints constraint = new DefaultConstraints();
				//........ Responsible parties
				Collection<Responsibility> respParties = new HashSet<Responsibility>();
				respParties.add(new DefaultResponsibility());
				constraint.setResponsibleParties(respParties);
				//........ References
				Collection<Citation> emptyCitations = new HashSet<Citation>();
				emptyCitations.add(new DefaultCitation());
				constraint.setReferences(emptyCitations);
				//........ Graphics
				Collection<BrowseGraphic> graphics = new HashSet<BrowseGraphic>();
				DefaultBrowseGraphic graphic = new DefaultBrowseGraphic(new URI("filename.png"));
				graphic.setFileDescription(new SimpleInternationalString("description"));
				graphic.setFileType("fileType");
					//............ Image constraints
					Collection<Constraints> imageConstraints = new HashSet<Constraints>();
					imageConstraints.add(new DefaultConstraints());
					graphic.setImageConstraints(imageConstraints);
					//............ Linkages
					Collection<OnlineResource> bgLinkages = new HashSet<OnlineResource>();
					bgLinkages.add(new DefaultOnlineResource());
					graphic.setLinkages(bgLinkages);
					graphics.add(graphic);
					constraint.setGraphics(graphics);
				//........ Use limitations
				Collection<InternationalString> useLimitations = new HashSet<InternationalString>();
				useLimitations.add(new SimpleInternationalString("useLimitation"));
				constraint.setUseLimitations(useLimitations);
				//........ Releasability
				DefaultReleasability releasability = new DefaultReleasability();
				releasability.setStatement(new SimpleInternationalString("statement"));
				constraint.setReleasability(releasability);
				//........ Constraint application scope
				DefaultScope scope = new DefaultScope();
				scope.setLevel(ScopeCode.APPLICATION);
				constraint.setConstraintApplicationScope(scope);
				//........ Add resource constraints to dataId
				resourceConstraints.add(constraint);
				dataId.setResourceConstraints(resourceConstraints);
			//.... Points of contact.
			Collection<Responsibility> pocs = new HashSet<Responsibility>();
			pocs.add(new DefaultResponsibility());
			dataId.setPointOfContacts(pocs);
			//.... Spatial representation type
			Collection<SpatialRepresentationType> representations = new HashSet<SpatialRepresentationType>();
			representations.add(SpatialRepresentationType.GRID);
			dataId.setSpatialRepresentationTypes(representations);
			//.... Spatial resolution
			Collection<Resolution> resolutions = new HashSet<Resolution>();
			DefaultResolution resolution = new DefaultResolution();
			resolution.setDistance(56777.0);
			resolutions.add(resolution);
			dataId.setSpatialResolutions(resolutions);
			//.... Topic categories
			Collection<TopicCategory> topicCategories = new HashSet<TopicCategory>();
			topicCategories.add(TopicCategory.OCEANS);
			topicCategories.add(TopicCategory.FARMING);
			dataId.setTopicCategories(topicCategories);
			//.... Status
			Collection<Progress> statuses = new HashSet<Progress>();
			statuses.add(Progress.ACCEPTED);
			dataId.setStatus(statuses);
			//.... Citation
			DefaultCitation cit = new DefaultCitation();
			cit.setTitle(new SimpleInternationalString("citationTitle"));
			cit.setEdition(new SimpleInternationalString("edition"));
			cit.setEditionDate(new Date());
			cit.setCollectiveTitle(new SimpleInternationalString("collectiveTitle"));
				//........ Alternate titles
				Collection<InternationalString> alternateTitles = new HashSet<InternationalString>();
				alternateTitles.add(new SimpleInternationalString("alternateTitle"));
				alternateTitles.add(new Anchor(new URI("http://example.com"), "alternateTitle"));
				cit.setAlternateTitles(alternateTitles);
				//........ Dates
				Collection<CitationDate> citDates = new HashSet<CitationDate>();
				citDates.add(new DefaultCitationDate(new Date(), DateType.CREATION));
				cit.setDates(citDates);
				dataId.setCitation(cit);
			//.... TODO: Temporal resolution
			Collection<Duration> durations = new HashSet<Duration>();
			dataId.setTemporalResolutions(durations);
			//.... Resource maintenance
			Collection<MaintenanceInformation> resourceMaintenances = new HashSet<MaintenanceInformation>();
			DefaultMaintenanceInformation maintenanceInfo = new DefaultMaintenanceInformation();
			maintenanceInfo.setMaintenanceAndUpdateFrequency(MaintenanceFrequency.ANNUALLY);
				//........ Maintenance dates
				Collection<CitationDate> maintenanceDates = new HashSet<CitationDate>();
				DefaultCitationDate maintenanceDate = new DefaultCitationDate(new Date(), DateType.NEXT_UPDATE);
				maintenanceDates.add(maintenanceDate);
				maintenanceInfo.setMaintenanceDates(maintenanceDates);
				//........ Maintenance scopes
				Collection<Scope> maintenanceScopes = new HashSet<Scope>();
				DefaultScope maintenanceScope = new DefaultScope();
				maintenanceScope.setLevel(ScopeCode.APPLICATION);
					//............ Scope level descriptions
					Collection<ScopeDescription> scopeDescriptions = new HashSet<ScopeDescription>();
					DefaultScopeDescription scopeDescription = new DefaultScopeDescription();
					scopeDescription.setDataset("dataset");
					scopeDescriptions.add(scopeDescription);
					maintenanceScope.setLevelDescription(scopeDescriptions);
				maintenanceScopes.add(maintenanceScope);
				maintenanceInfo.setMaintenanceScopes(maintenanceScopes);
			resourceMaintenances.add(maintenanceInfo);
			dataId.setResourceMaintenances(resourceMaintenances);
			//.... Resource format (MD_Format)
			Collection<Format> resourceFormats = new HashSet<Format>();
			DefaultFormat resourceFormat = new DefaultFormat();
			resourceFormat.setName(new SimpleInternationalString("name"));
			resourceFormat.setAmendmentNumber(new SimpleInternationalString("amendmentNumber"));
			resourceFormat.setVersion(new SimpleInternationalString("version"));
			resourceFormat.setSpecification(new SimpleInternationalString("specification"));
			resourceFormat.setFileDecompressionTechnique(new SimpleInternationalString("decompressionTechnique"));
			resourceFormats.add(resourceFormat);
			dataId.setResourceFormats(resourceFormats);
			//.... Descriptive keywords (MD_Keywords)
			Collection<Keywords> descriptiveKeywords = new HashSet<Keywords>();
			DefaultKeywords keywords = new DefaultKeywords();
			keywords.setType(KeywordType.THEME);
			keywords.setThesaurusName(new DefaultCitation());
				//........ Keyword type
				DefaultKeywordClass keywordClass = new DefaultKeywordClass();
				keywordClass.setClassName(new SimpleInternationalString("name"));
				keywords.setKeywordClass(keywordClass);
				//........ Keywords
				Collection<InternationalString> words = new HashSet<InternationalString>();
				words.add(new SimpleInternationalString("keyword1"));
				words.add(new SimpleInternationalString("keyword2"));
				keywords.setKeywords(words);
			descriptiveKeywords.add(keywords);
			dataId.setDescriptiveKeywords(descriptiveKeywords);
			//.... Resource specific usage
			Collection<Usage> resourceSpecificUsages = new HashSet<Usage>();
			DefaultUsage usage = new DefaultUsage();
				//........ Specific usage
				usage.setSpecificUsage(new SimpleInternationalString("specificUsage"));
				//........ Usage date
				usage.setUsageDate(new Date());
				//........ Responses
				Collection<InternationalString> responses = new HashSet<InternationalString>();
				responses.add(new SimpleInternationalString("response"));
				usage.setResponses(responses);
				//........ Additional Documentation
				usage.setAdditionalDocumentation(emptyCitations);
				//........ Identified Issues
				usage.setIdentifiedIssues(emptyCitations);
			usage.setUserDeterminedLimitations(new SimpleInternationalString("userDeterminedLimitations"));
				//........ User contact info
				Collection<Responsibility> userContactInfo = new HashSet<Responsibility>();
				userContactInfo.add(new DefaultResponsibility());
				usage.setUserContactInfo(userContactInfo);
			resourceSpecificUsages.add(usage);
			dataId.setResourceSpecificUsages(resourceSpecificUsages);
			//.... Associated resources (AggregationInfo in 19139)
			Collection<AssociatedResource> associatedResources = new HashSet<AssociatedResource>();
			DefaultAssociatedResource associatedResource = new DefaultAssociatedResource();
				//........ Name (citation)
				DefaultCitation associatedResourceCitation = new DefaultCitation();
				associatedResource.setName(associatedResourceCitation);
				//........ Association type
				associatedResource.setAssociationType(AssociationType.DEPENDENCY);
				//........ Initiative type
				associatedResource.setInitiativeType(InitiativeType.EXPERIMENT);
				// Collevtice title
			associatedResources.add(associatedResource);
			dataId.setAssociatedResources(associatedResources);
			//.... Locales (ISO 19115-3) AKA Languages and CharacterSets (ISO 19139)
			dataId.setLanguages(languages);
			dataId.setCharacterSets(charSets);
			//.... Environment description
			dataId.setEnvironmentDescription(new SimpleInternationalString("environmentDescription"));
			//.... Supplemental information
			dataId.setSupplementalInformation(new SimpleInternationalString("supplementalInformation"));
			//.... Add identification info to metadata object.
			idInfos.add(dataId);
			
			
		// Service identification info
		DefaultServiceIdentification serviceId = new DefaultServiceIdentification();
			//.... Citation
			serviceId.setCitation(cit);
			//.... Abstract
			serviceId.setAbstract(new SimpleInternationalString("abstract"));
			//.... Point of contact
			serviceId.setPointOfContacts(pocs);
			//.... Extent
			serviceId.setExtents(extents);
			//.... Resource maintenance
			serviceId.setResourceMaintenances(resourceMaintenances);
			//.... Descriptive keywords
			serviceId.setDescriptiveKeywords(descriptiveKeywords);
			//.... Resource constraints
			serviceId.setResourceConstraints(resourceConstraints);
			// Associated resources
			serviceId.setAssociatedResources(associatedResources);
			//.... TODO: Service type
			//.... Service type version
			Collection<String> serviceTypeVersions = new HashSet<String>();
			serviceTypeVersions.add("serviceTypeVersion");
			serviceId.setServiceTypeVersions(serviceTypeVersions);
			//.... TODO: Coupled resources
			Collection<CoupledResource> coupledResources = new HashSet<CoupledResource>();
			DefaultCoupledResource coupledResource = new DefaultCoupledResource();
			coupledResources.add(coupledResource);
			serviceId.setCoupledResources(coupledResources);
			//.... Coupling type
			serviceId.setCouplingType(CouplingType.TIGHT);
			//.... Contains operations
			Collection<OperationMetadata> operationMetadatas = new HashSet<OperationMetadata>();
			DefaultOperationMetadata operationMetadata = new DefaultOperationMetadata();
				operationMetadata.setOperationName("operationName");
				operationMetadata.setOperationDescription(new SimpleInternationalString("operationDescription"));
				operationMetadata.setInvocationName(new SimpleInternationalString("invocationName"));
				//........ Distributed computing platforms
				Collection<DistributedComputingPlatform> distributedComputingPlatforms = new HashSet<DistributedComputingPlatform>();
				distributedComputingPlatforms.add(DistributedComputingPlatform.JAVA);
				operationMetadata.setDistributedComputingPlatforms(distributedComputingPlatforms);
				//........ Connect points
				Collection<OnlineResource> connectPoints = new HashSet<OnlineResource>();
				connectPoints.add(new DefaultOnlineResource());
				operationMetadata.setConnectPoints(connectPoints);
				//........ Parameters are unchanged according to crosswalk. Don't need to do this one.
				//operationMetadata.setParameters(newValues);
			operationMetadatas.add(operationMetadata);
			serviceId.setContainsOperations(operationMetadatas);
			//.... Operates on
			Collection<DataIdentification> dataIds = new HashSet<DataIdentification>();
			dataIds.add(dataId);
			serviceId.setOperatesOn(dataIds);
		idInfos.add(serviceId);
		md.setIdentificationInfo(idInfos);
		
		// Content Info
		Collection<ContentInformation> contentInfos = new HashSet<ContentInformation>();
			//.... Coverage Description
			DefaultCoverageDescription coverageDescription = new DefaultCoverageDescription();
				//........ Attribute description TODO: this doesn't work properly.
				DefaultRecordSchema schema = new DefaultRecordSchema(null, null, "MySchema");
				Map<CharSequence,Class<?>> members = new LinkedHashMap<>();
			 	members.put("city",        String.class);
			    members.put("latitude",    Double.class);
			    members.put("longitude",   Double.class);
				RecordType recordType = schema.createRecordType("MyRecordType", members);
				coverageDescription.setAttributeDescription(recordType);
				//........ Attribute group
				Collection<AttributeGroup> attributeGroups = new HashSet<AttributeGroup>();
				DefaultAttributeGroup attributeGroup = new DefaultAttributeGroup();
					//............ Content types
					Collection<CoverageContentType> contentTypes = new HashSet<CoverageContentType>();
					contentTypes.add(CoverageContentType.AUXILLARY_INFORMATION);
					attributeGroup.setContentTypes(contentTypes);
					//............ Attributes
					Collection<RangeDimension> rangeDimensions = new HashSet<RangeDimension>();
					DefaultRangeDimension rangeDimension = new DefaultRangeDimension();
						//................ Description / descriptor
						rangeDimension.setDescription(new SimpleInternationalString("descriptor"));
						//................ TODO: Sequence identifier
						/*DefaultMemberName memberName = DefaultNameFactory.createMemberName(NameSpace, CharSequence, TypeName);
						rangeDimension.setSequenceIdentifier(memberName);*/
						//................ Names
						Collection<Identifier> emptyIdentifiers = new HashSet<Identifier>();
						emptyIdentifiers.add(new DefaultIdentifier());
						rangeDimension.setNames(emptyIdentifiers);
					rangeDimensions.add(rangeDimension);
					DefaultSampleDimension sampleDimension = new DefaultSampleDimension();
						//................ Description / descriptor
						sampleDimension.setDescription(new SimpleInternationalString("descriptor"));
						//................ Min value
						sampleDimension.setMinValue(11.11);
						//................ Max value
						sampleDimension.setMaxValue(22.22);
						//................ Units
						sampleDimension.setUnits(Units.FAHRENHEIT);
						//................ Scale factor
						sampleDimension.setScaleFactor(1.0);
					rangeDimensions.add(sampleDimension);
					attributeGroup.setAttributes(rangeDimensions);
				attributeGroups.add(attributeGroup);
				coverageDescription.setDimensions(rangeDimensions);
				//coverageDescription.setAttributeGroups(attributeGroups);
			contentInfos.add(coverageDescription);
			//.... Feature Catalogue Description
			DefaultFeatureCatalogueDescription featureCatalogueDescription = new DefaultFeatureCatalogueDescription();
				//........ Included with dataset
				featureCatalogueDescription.setIncludedWithDataset(true);
				//........ Compliant
				featureCatalogueDescription.setCompliant(true);
			contentInfos.add(featureCatalogueDescription);
		md.setContentInfo(contentInfos);

		// Write the metadata to both ISO standards.
		System.out.println("_MARSHALLING 19115-3_");
		marshaller.setProperty(XML.METADATA_VERSION, Namespaces.ISO_19115_3);
		marshaller.marshal(md, output191153);
		System.out.println("_MARSHALLING 19139_");
		marshaller.setProperty(XML.METADATA_VERSION, Namespaces.ISO_19139);
		marshaller.marshal(md, output19139);
	}

	@After
	public void cleanUp() {
		pool.recycle(marshaller);
	}

	/**
	 * For internal {@code DefaultMetadata} usage.
	 *
	 * @return {@code Object.class}.
	 */
	@Override
	public Class<Object> getSourceClass() {
		return Object.class;
	}

	/**
	 * Invoked when a warning occurred while marshalling a test XML fragment. This method ensures that no other
	 * warning occurred before this method call (i.e. each test is allowed to cause at most one warning), then
	 * remember the warning parameters for verification by the test method.
	 *
	 * @param source  Ignored.
	 * @param warning The warning.
	 */
	@Override
	public void warningOccured(final Object source, final LogRecord warning) {
		assertNull(resourceKey);
		assertNull(parameters);
		// TODO: uncomment these and fix warnings
		//assertNotNull(resourceKey = warning.getMessage());
		//assertNotNull(parameters  = warning.getParameters());
	}


}
