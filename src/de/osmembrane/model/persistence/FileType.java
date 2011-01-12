package de.osmembrane.model.persistence;

/**
 * Represents the different FileTypes.
 * 
 * @author jakob_jarosch
 */
public enum FileType {
	
	/**
	 * Bash normally used under UNIX systems.
	 */
	BASH(".sh", BashPersistence.class),
	
	/**
	 * CMD normally used under Windows systems.
	 */
	CMD(".bat", CmdPersistence.class);
	

	/**
	 * {@link FileType} as a string.
	 */
	private String extension;
	
	/**
	 * Matching persistence for the {@link FileType}.
	 */
	private Class<? extends AbstractPersistence> persistenceClass;
	
	
	private FileType(String extension, Class<? extends AbstractPersistence> persistenceClass) {
		this.extension = extension;
		this.persistenceClass = persistenceClass;
	}

	/**
	 * Returns the extension as a String.
	 * 
	 * @return extension as a String
	 */
	public String getExtension() {
		return extension;
	}
	
	/**
	 * Returns the matching {@link AbstractPersistence} class for load and save.
	 * 
	 * @return matching {@link AbstractPersistence} class.
	 */
	public Class<? extends AbstractPersistence> getPersistenceClass() {
		return persistenceClass;
	}
}
