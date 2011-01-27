package de.osmembrane.resources;

import java.awt.Color;
import java.util.Locale;

import de.osmembrane.model.pipeline.AbstractFunctionPrototype;
import de.osmembrane.model.pipeline.Pipeline;

/**
 * Constants for OSMembrane.
 * 
 * @author jakob_jarosch
 */
public class Constants {
	
	/**
	 * Path to the language files.
	 */
	public static final String RESOURCE_BUNDLE_PATH = "de.osmembrane.resources.language.Locale";
	
	/**
	 * Is used by I18N for a default translation of the XMLHasDescription objects.
	 */
	public static final Locale DEFAULT_LOCALE = Locale.ENGLISH;
	
	/**
	 * Path to the XML resource for {@link AbstractFunctionPrototype}.
	 */
	public static final String XML_RESOURCE_PATH = "/de/osmembrane/resources/xml/osmosis-structure.xml";

	/**
	 * Default Backup filename (for {@link Pipeline}.
	 */
	public static final String DEFAULT_BACKUP_FILE = System.getProperty("user.home") + "/" + "backup.osmembrane";
	
	/**
	 * The default color for a function-group with a non parsable color.
	 */
	public static final Color DEFAULT_FUNCTIONGROUP_COLOR = new Color(0.8f, 0.8f, 0.8f);
	
	/**
	 * Color definitions for the various stream types
	 */
	public static final Color ENTITY_STREAM_TYPE_COLOR = new Color(0.5f, 0.5f, 1.0f);
	
	/**
	 * Color definitions for the various stream types
	 */
	public static final Color CHANGE_STREAM_TYPE_COLOR = new Color(1.0f, 0.5f, 0.5f);
	
	/**
	 * Color definitions for the various stream types
	 */
	public static final Color DATASET_STREAM_TYPE_COLOR = new Color(0.5f, 1.0f, 0.5f);
	
	/**
	 * Icons default folder.
	 * 
	 * 	 Preset-Icons latest version is available under http://josm.openstreetmap.de/svn/trunk/images/presets/
	 */
	public static final String ICONS_PATH = "/de/osmembrane/resources/images/icons/";
	
	/**
	 * Preset-Icons default folder.
	 * 

	 */
	
	/**
	 * The OSMembrane file extension.
	 */
	public static final String OSMEMBRANE_EXTENSION = ".osmembrane";

	/**
	 * Maximum count of undo steps until the first one is removed from the undoStack.
	 */
	public static final int MAXIMUM_UNDO_STEPS = 100;

	/**
	 * The Preset-File for node key-value-lists and way key-value-lists.
	 * 
	 * Latest version from http://josm.openstreetmap.de/svn/trunk/data/defaultpresets.xml
	 */
	public static final String XML_PRESET_PATH = "/de/osmembrane/resources/xml/defaultpresets.xml";
}
