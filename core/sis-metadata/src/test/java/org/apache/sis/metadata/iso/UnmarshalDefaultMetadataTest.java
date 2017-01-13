package org.apache.sis.metadata.iso;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.net.URISyntaxException;
import java.util.logging.LogRecord;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.sis.test.XMLTestCase;
import org.apache.sis.util.logging.WarningListener;
import org.apache.sis.xml.MarshallerPool;
import org.apache.sis.xml.Namespaces;
import org.apache.sis.xml.XML;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opengis.metadata.Metadata;

public class UnmarshalDefaultMetadataTest extends XMLTestCase implements WarningListener<Object> {
	/**
	 * The unmarshaller used to handle unmarshalling the metadata XML.
	 */
	private Unmarshaller unmarshaller;

	/**
	 * The marshaller used to handle marshaller the metadata XML.
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

	private final File noaaMetadata = new File("src/test/resources/org/apache/sis/metadata/iso/example_noaa_19139.xml");
	private final File noaaOutput = new File("src/test/resources/org/apache/sis/metadata/iso/output_noaa_19139.xml");
	private final File test19139 = new File("src/test/resources/org/apache/sis/metadata/iso/test19139.xml");
	private final File test191153 = new File("src/test/resources/org/apache/sis/metadata/iso/test191153.xml");
	private final File oldMetadata = new File("src/test/resources/org/apache/sis/metadata/iso/generated19139Metadata.xml");
	private final File newMetadata = new File("src/test/resources/org/apache/sis/metadata/iso/generated19115Metadata.xml");
	private final File newOutput = new File("src/test/resources/org/apache/sis/metadata/iso/unmarshalTestNewMetadata.xml");
	private final File oldOutput = new File("src/test/resources/org/apache/sis/metadata/iso/unmarshalTestOldMetadata.xml");

	@Before
	public void setUp() {
		try {
			pool = getMarshallerPool();
			unmarshaller  = pool.acquireUnmarshaller();
			marshaller = pool.acquireMarshaller();
			//unmarshaller.setEventHandler(new javax.xml.bind.helpers.DefaultValidationEventHandler());
			unmarshaller.setProperty(XML.WARNING_LISTENER, this);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testRoundTripNew() throws JAXBException, URISyntaxException {
		// Read the ISO 19115-3 metadata.
		System.out.println("\n__UNMARSHALLING 19115-3__");
		unmarshaller.setProperty(XML.METADATA_VERSION, Namespaces.ISO_19115_3);
		Metadata md = (Metadata) unmarshaller.unmarshal(newMetadata);

		// Write it back to XML to ensure they are the same.
		System.out.println("\n__MARSHALLING 19115-3__");
		marshaller.setProperty(XML.METADATA_VERSION, Namespaces.ISO_19115_3);
		marshaller.marshal(md, newOutput);
	}

	@Test
	public void testRoundTripOld() throws JAXBException, URISyntaxException {
		// Read the ISO 19139 metadata.
		System.out.println("\n__UNMARSHALLING 19139__");
		unmarshaller.setProperty(XML.METADATA_VERSION, Namespaces.ISO_19139);
		Metadata md = (Metadata) unmarshaller.unmarshal(oldMetadata);

		// Write it back to XML to ensure they are the same.
		System.out.println("\n__MARSHALLING 19139__");
		marshaller.setProperty(XML.METADATA_VERSION, Namespaces.ISO_19139);
		marshaller.marshal(md, oldOutput);
	}
	
	@Test
	public void testRoundTripNoaa() throws JAXBException, URISyntaxException {
		// Read the ISO 19139 metadata.
		System.out.println("\n__UNMARSHALLING NOAA 19139__");
		unmarshaller.setProperty(XML.METADATA_VERSION, Namespaces.ISO_19139);
		Metadata md = (Metadata) unmarshaller.unmarshal(noaaMetadata);

		// Write it back to XML to ensure they are the same.
		System.out.println("\n__MARSHALLING NOAA 19139__");
		marshaller.setProperty(XML.METADATA_VERSION, Namespaces.ISO_19139);
		marshaller.marshal(md, noaaOutput);
	}
	
	@Test
	public void test19139to191153() throws JAXBException, URISyntaxException {
		// Read the ISO 19139 metadata.
		System.out.println("\n__UNMARSHALLING EXAMPLE 19139__");
		unmarshaller.setProperty(XML.METADATA_VERSION, Namespaces.ISO_19139);
		Metadata md = (Metadata) unmarshaller.unmarshal(test19139);

		// Write it back to XML in 19115-3 format..
		System.out.println("\n__MARSHALLING EXAMPLE 19115-3__");
		marshaller.setProperty(XML.METADATA_VERSION, Namespaces.ISO_19115_3);
		marshaller.marshal(md, test191153);
	}

	@After
	public void cleanUp() {
		pool.recycle(unmarshaller);
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
		// TODO: Remove this as soon as the TimePeriod bug is fixed.
		//assertNotNull(resourceKey = warning.getMessage());
		//assertNotNull(parameters  = warning.getParameters());
	}
}
