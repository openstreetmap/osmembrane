package de.osmembrane.model.settings;

import java.io.Serializable;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

import de.osmembrane.model.ModelProxy;
import de.osmembrane.model.persistence.FileException;

/**
 * Interface for accessing the {@link Settings} through the {@link ModelProxy}.
 * 
 * @author jakob_jarosch
 */
public abstract class AbstractSettings extends Observable implements
		Serializable, Observer {

	private static final long serialVersionUID = 2010122714350001L;

	/**
	 * Initiates the Settings-model.
	 */
	public abstract void initiate();

	/**
	 * Returns a value.
	 * 
	 * @param type
	 *            type of the value which should be returned
	 * 
	 * @return the requested value or the default value for it
	 */
	public abstract Object getValue(SettingType type);

	/**
	 * Sets the value for a given type.
	 * 
	 * @param type
	 *            type which should be set
	 * 
	 * @param value
	 *            value which should be assigned to the type
	 */
	public abstract void setValue(SettingType type, Object value)
			throws UnparsableFormatException;

	/**
	 * Returns all available languages.
	 * 
	 * @return all available languages
	 */
	public abstract Locale[] getLanguages();

	/**
	 * Save the settings to its settings file.
	 * 
	 * @throws FileException
	 *             is thrown if the settings could not be saved
	 */
	public abstract void saveSettings() throws FileException;

	protected abstract void changedNotifyObservers(SettingsObserverObject soo);
}