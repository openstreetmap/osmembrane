package de.osmembrane.resources;

import java.awt.Color;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import de.osmembrane.Application;
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
	 * Default Backup filename (for {@link Pipeline}.
	 */
	public static final URL DEFAULT_BACKUP_FILE;
	
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
	 * Maximum count of undo steps until the first one is removed from the undoStack.
	 */
	public static final int MAXIMUM_UNDO_STEPS = 100;

	/**
	 * Seperator char of the bbox parameter
	 */
	public static final String BBOX_SEPERATOR = ",";
	
	/**
	 * static method
	 */
	static {
		URL url = null;
		try {
			url = new File(System.getProperty("user.home") + "/" + "backup.osmembrane").toURI().toURL();
		} catch (MalformedURLException e) {
			/* no that shouldn't be so! */
			Application.handleException(e);
		} finally {
			DEFAULT_BACKUP_FILE = url;
		}
	}
}
