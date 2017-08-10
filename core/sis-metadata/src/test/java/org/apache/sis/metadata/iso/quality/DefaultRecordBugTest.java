package org.apache.sis.metadata.iso.quality;

import org.apache.sis.metadata.iso.DefaultMetadata;
import org.apache.sis.test.XMLTestCase;
import org.junit.Test;

import javax.xml.bind.JAXBException;

public class DefaultRecordBugTest extends XMLTestCase {

    private final String XML_FILE = "DefaultRecordBug.xml";

    @Test
    public void testDefaultRecordBug() throws JAXBException {
        DefaultMetadata metadata = unmarshalFile(DefaultMetadata.class, XML_FILE);
    }

}
