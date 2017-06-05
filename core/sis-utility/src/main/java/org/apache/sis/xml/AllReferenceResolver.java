package org.apache.sis.xml;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.sis.util.Version;
import org.apache.sis.xml.MarshalContext;
import org.apache.sis.xml.MarshallerPool;
import org.apache.sis.xml.ReferenceResolver;
import org.apache.sis.xml.XLink;
import org.apache.sis.xml.XML;

/**
 * This class is used to resolve external references in ISO metadata when unmarshalling.
 * It will <em>always</em> attempt to resolve external references, and will return null
 * if it fails to do so.
 * 
 * @author Cullen Rombach  (Image Matters)
 */
public class AllReferenceResolver extends ReferenceResolver {

	private Version metadataVersion;
	
	private MarshallerPool pool;

	/**
	 * Constructor.
	 * @param metadataVersion The version of metadata to unmarshal (e.g. ISO 19139)
	 * @throws JAXBException
	 */
	public AllReferenceResolver(Version metadataVersion) throws JAXBException {
		super();
		this.metadataVersion = metadataVersion;
	}

	@Override
	public <T> T resolve(MarshalContext context, Class<T> type, XLink link) {
		T content = super.resolve(context, type, link);

		try {
			if (content == null) {
				// Cast the URI in the XLink to a URL.
				URL href = link.getHRef().toURL();

				// Used for storing a redirect URL if necessary.
				URL newHref = href;

				// Flag to see if a redirect is necessary.
				Boolean redirect = true;

				while(redirect) {
					// Only check for redirects on HTTP and HTTPS connections.
					if(newHref.getProtocol().contains("http")) {
						// Open a connection to the URL to check for a redirect if it uses HTTP protocol.
						HttpURLConnection connection = (HttpURLConnection) newHref.openConnection();

						// Check HTTP status for a possible redirect.
						int status = connection.getResponseCode();

						// If we need to redirect, do so.
						if(status == HttpURLConnection.HTTP_MOVED_TEMP
								|| status == HttpURLConnection.HTTP_MOVED_PERM
								|| status == HttpURLConnection.HTTP_SEE_OTHER) {

							// If we need to redirect, get the new URL.
							newHref = new URL(connection.getHeaderField("Location"));
						}
						// Otherwise, flag that no redirect is necessary.
						else {
							redirect = false;
						}
					}
					// If the link doesn't use HTTP protocol, don't check for a redirect.
					else {
						redirect = false;
					}
				}

				// Build an Unmarshaller (and the MarshallerPool it belongs to).
				Unmarshaller unmarshaller = acquireUnmarshaller(metadataVersion);

				// If there was a redirect, unmarshal the new URL.
				if(!newHref.equals(href)) {
					content = type.cast(unmarshaller.unmarshal(newHref));
					pool.recycle(unmarshaller);
				}
				// Otherwise, unmarshal the original URL.
				else if (href != null) {
					content = type.cast(unmarshaller.unmarshal(href));
					pool.recycle(unmarshaller);
				}
			}
			
		// If any sort of exception occurred, just return null and don't resolve the reference.
		} catch (MalformedURLException e) {
			return null;
		} catch (JAXBException e) {
			return null;
		} catch (IOException e) {
			return null;
		}

		return content;
	}

	/**
	 * Get an unmarshaller with its own AllReferenceResolver that operates on
	 * the given metadata version.
	 * @param metadataVersion
	 * @return a new Unmarshaller.
	 * @throws JAXBException
	 */
	private Unmarshaller acquireUnmarshaller(Version metadataVersion) throws JAXBException {
		pool = new MarshallerPool(null);
		Unmarshaller unmarshaller = pool.acquireUnmarshaller();
		// Set metadata version for unmarshalling.
		unmarshaller.setProperty(XML.METADATA_VERSION, metadataVersion);
		// Set resolver for external references.
		unmarshaller.setProperty(XML.RESOLVER, new AllReferenceResolver(metadataVersion));
		return unmarshaller;
	}
}
