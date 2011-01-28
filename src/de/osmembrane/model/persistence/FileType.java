package de.osmembrane.model.persistence;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import de.osmembrane.model.parser.BashParser;
import de.osmembrane.model.parser.CmdParser;
import de.osmembrane.model.parser.IParser;
import de.osmembrane.model.pipeline.AbstractPipeline;
import de.osmembrane.tools.I18N;

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
	CMD(".bat", CmdPersistence.class, CmdParser.class),

	/**
	 * OSMembrane filetype.
	 */
	OSMEMBRANE(".osmembrane", OSMembranePersistence.class, null);

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

	private FileType(String extension,
			Class<? extends AbstractPersistence> persistenceClass,
			Class<? extends IParser> parserClass) {
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
	 * Returns the FileFilter for the filetype.
	 * 
	 * @return FileFilter of the filetype.
	 */
	public FileFilter getFileFilter() {
		return new FileFilter() {

			@Override
			public boolean accept(File f) {
				return f.getName().toLowerCase().endsWith(getExtension());
			}

			@Override
			public String getDescription() {
				return getName() + " (*" + getExtension() + ")";
			}
		};
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
	 * Returns the matching {@link IParser} class for parsing a
	 * {@link AbstractPipeline}.
	 * 
	 * @return matching {@link IParser} class
	 */
	public Class<? extends IParser> getParserClass() {
		return parserClass;
	}

	public String getName() {
		return I18N.getInstance().getString(
				"Controller.Actions.FileType." + this.toString() + ".Name");
	}
}
