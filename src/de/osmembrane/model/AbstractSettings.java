package de.osmembrane.model;

import java.io.Serializable;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.Map.Entry;

import de.osmembrane.model.settings.AbstractSetting;

/**
 * Interface for accessing the {@link Settings} through the {@link ModelProxy}.
 * 
 * @author jakob_jarosch
 */
public abstract class AbstractSettings extends Observable implements
		Serializable, Observer {

	private static final long serialVersionUID = 2010122714350001L;
	
	/**
	 * Initiates the Settings-model with a given file.
	 * 
	 * @param settingsFilename
	 *            file from where the settings should be loaded
	 */
	public abstract void initiate(String settingsFilename);

	/**
	 * Gives back an array of {@link AbstractSetting}.
	 * 
	 * @return array of {@link AbstractSetting}.
	 */
	public abstract Set<Entry<Class<? extends AbstractSetting>, AbstractSetting>> getSettings();

	/**
	 * Gives back a specific {@link AbstractSetting}. If Setting does not exist
	 * it will be created (if possible, otherwise NULL).
	 * 
	 * @param setting
	 *            class-name of {@link AbstractSetting} should be returned.
	 * @return wanted AbstractSetting
	 */
	public abstract AbstractSetting getSetting(
			Class<? extends AbstractSetting> setting);

	/**
	 * Removes an {@link AbstractSetting} from the model.
	 * 
	 * @param setting
	 *            which should be removed
	 */
	public abstract void deleteSetting(Class<? extends AbstractSetting> setting);
}