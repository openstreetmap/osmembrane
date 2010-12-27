package de.osmembrane.tools;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Observable;
import java.util.ResourceBundle;

import de.osmembrane.model.xml.XMLHasDescription;
import de.osmembrane.model.xml.XMLHasDescription.Description;
import de.osmembrane.resources.Constants;

/**
 * Internationalization for OSMembrane.
 * 
 * @author jakob_jarosch
 */
public class I18N extends Observable {

	/**
	 * Locale for the current instance.
	 */
	private Locale activeLocale;

	/**
	 * ResourceBundle where localized Strings are saved.
	 */
	private ResourceBundle resourceBundle;

	/**
	 * Default-Locale for {@see XMLHasDescription}.
	 */
	private Locale defaultLocale;

	/**
	 * Implements the Singleton pattern.
	 */
	private static I18N instance = new I18N();

	/**
	 * Creates a new ResourceBundle with the default locale of the system.
	 */
	private I18N() {
		/* use the system default locale */
		setLocale(Locale.getDefault());

		/*
		 * use a program default locale 'cause system default may not be
		 * translated
		 */
		setDefaultLocale(Constants.DEFAULT_LOCALE);
	}

	/**
	 * Getter for the Singleton pattern.
	 * 
	 * @return the one and only instance of I18N
	 */
	public static I18N getInstance() {
		return instance;
	}

	/**
	 * Updates the locale for the current instance.
	 * 
	 * @param locale
	 *            new locale for ResourceBundle.
	 */
	public void setLocale(Locale locale) {
		this.activeLocale = locale;
		Locale.setDefault(locale);
		this.resourceBundle = ResourceBundle.getBundle(
				Constants.RESOURCE_BUNDLE_PATH, this.activeLocale);

		/* notify the observers that possibly the language has been changed */
		setChanged();
		notifyObservers();
	}

	/**
	 * Updates the default locale for the current instance.
	 * 
	 * @param locale
	 *            new defaultLocale for {@see XMLHasDescription}
	 */
	public void setDefaultLocale(Locale locale) {
		this.defaultLocale = locale;
		
		/* notify the observers that possibly the language has been changed */
		setChanged();
		notifyObservers();
	}

	/**
	 * Gives a localized and formatted String for a given key back.
	 * 
	 * @param key
	 *            Key which needs a localized String
	 * @param values
	 *            Values that should be inserted into the String
	 * @return a localized and formatted String
	 */
	public String getString(String key, Object... values) {
		String msg;
			msg = resourceBundle.getString(key);
		
		if (values.length > 0) {
			return MessageFormat.format(msg, values);
		}
		
		return msg;
	}

	/**
	 * Gives a localized String for a given {@see XMLHasDescription} object
	 * back.
	 * 
	 * @param description
	 *            which should be localized
	 * @return String for the localized description or NULL if no localization
	 *         was found.
	 */
	public String getDescription(XMLHasDescription description) {
		/* First try to find activeLocale. */
		for (Description descriptionString : description.getDescription()) {
			if (descriptionString.getLang().equals(activeLocale.getLanguage())) {
				return descriptionString.getValue().trim();
			}
		}

		/*
		 * No translation for the active locale has been found, so try to use
		 * the defaultLocale.
		 */
		for (Description descriptionString : description.getDescription()) {
			if (descriptionString.getLang().equals(defaultLocale.getLanguage())) {
				return descriptionString.getValue().trim();
			}
		}

		/* No translation found, return NULL */
		return null;
	}
}