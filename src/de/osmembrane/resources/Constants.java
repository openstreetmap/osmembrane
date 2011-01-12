package de.osmembrane.resources;

import java.util.Locale;

import de.osmembrane.model.AbstractFunctionPrototype;

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
	public static final String XML_RESOURCE_PATH = "src/de/osmembrane/resources/xml/osmosis-structure.xml";
}
