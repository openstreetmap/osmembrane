package de.osmembrane.model.persistence;

import de.osmembrane.model.parser.BashParser;
import de.osmembrane.model.parser.CmdParser;
import de.osmembrane.model.parser.IParser;
import de.osmembrane.model.pipeline.AbstractPipeline;

/**
 * Represents the different FileTypes.
 * 
 * @author jakob_jarosch
 */
public enum FileType {
	
	/**
	 * Bash normally used under UNIX systems.
	 */
	BASH(".sh", BashPersistence.class, BashParser.class),
	
	/**
	 * CMD normally used under Windows systems.
	 */
	CMD(".bat", CmdPersistence.class, CmdParser.class);
	

	/**
	 * {@link FileType} as a string.
	 */
	private String extension;
	
	/**
	 * Matching persistence for the {@link FileType}.
	 */
	private Class<? extends AbstractPersistence> persistenceClass;
	
	/**
	 * Matching parser for the {@link FileType}.
	 */
	private Class<? extends IParser> parserClass;
	
	
	private FileType(String extension, Class<? extends AbstractPersistence> persistenceClass, Class<? extends IParser> parserClass) {
		this.extension = extension;
		this.persistenceClass = persistenceClass;
		this.parserClass = parserClass;
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
	
	/**
	 * Returns the matching {@link IParser} class for parsing a {@link AbstractPipeline}.
	 * 
	 * @return matching {@link IParser} class
	 */
	public Class<? extends IParser> getParserClass() {
		return parserClass;
	}
}
