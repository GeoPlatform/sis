package org.apache.sis.internal.jaxb;

/**
 * This class is used to determine which metadata standard will be used in the marshalling process.
 * As of its initial revision, supported standards are ISO 19139 (2003) and ISO 19115-3 (2014).
 * 
 * @author Cullen Rombach (Image Matters)
 */
public class MetadataInfo {
	
	private static Standard metadataStandard = Standard.ISO_2014;
	
	private static Process process = Process.UNMARSHAL;

	public static enum Standard { ISO_2003, ISO_2014 };
	
	public static enum Process { MARSHAL, UNMARSHAL };
	/**
	 * Checks whether the user has determined that ISO 19139 (2003) metadata
	 * is being worked with.
	 * @return true if format is ISO 19139
	 */
	public static boolean is2003() {
		return metadataStandard.equals(Standard.ISO_2003);
	}

	/**
	 * Checks whether the user has determined that ISO 19115-3 (2014) metadata
	 * is being worked with.
	 * @return true if format is ISO 19115-3
	 */
	public static boolean is2014() {
		return metadataStandard.equals(Standard.ISO_2014);
	}
	
	/**
	 * Checks if JAXB is currently marshalling.
	 * @return true if marshalling
	 */
	public static boolean isMarshalling() {
		return process.equals(Process.MARSHAL);
	}
	
	/**
	 * Checks is JAXB is currently unmarshalling.
	 * @return true if unmarshalling
	 */
	public static boolean isUnmarshalling() {
		return process.equals(Process.UNMARSHAL);
	}

	/**
	 * Set the metadata standard being worked with, chosen from the predefined Standard
	 * enum. Currently, supported options are ISO_2003 (ISO 19139) and
	 * ISO_2014 (ISO 19115-3). Default standard is ISO_2014. Should only be called
	 * internally.
	 * @param newStandard Metadata standard to work with.
	 */
	public static void setStandard(Standard newStandard) {
		metadataStandard = newStandard;
	}
	
	/**
	 * Set the process being done (marhsalling or unmarshalling). Supported options are
	 * MARSHAL and UNMARSHAL. Unmarshalling is the default, since a user is likely to read data
	 * before they write it. This method should only be called internally.
	 * @param newProcess Process to use.
	 */
	public static void setProcess(Process newProcess) {
		process = newProcess;
	}
}
