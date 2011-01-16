package de.osmembrane.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Set;

import de.osmembrane.model.persistence.PersistenceFactory;
import de.osmembrane.model.settings.AbstractSetting;
import de.osmembrane.model.settings.SettingsObserverObject;

/**
 * 
 * 
 * @author jakob_jarosch
 */
public class Settings extends AbstractSettings {

	private static final long serialVersionUID = 2010122714350002L;

	/**
	 * Saves the instances from {@link AbstractSetting} of this object.
	 */
	private Map<Class<? extends AbstractSetting>, AbstractSetting> settings = new HashMap<Class<? extends AbstractSetting>, AbstractSetting>();

	private String settingsFilename;
	
	/**
	 * Initiates the Settings.
	 */
	public Settings() {
		/*
		 * Register the PersistenceFactory-Observer at the Settings, so all
		 * updates will be forwarded to the SettingPersistence.
		 */
		addObserver(PersistenceFactory.getInstance());
	}

	@Override
	public void initiate(String settingsFilename) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<Entry<Class<? extends AbstractSetting>, AbstractSetting>> getSettings() {
		return settings.entrySet();
	}

	@Override
	public AbstractSetting getSetting(Class<? extends AbstractSetting> setting) {
		if (!settings.containsKey(setting)) {
			try {
				AbstractSetting settingInstance = setting.newInstance();
				settingInstance.addObserver(this);

				settings.put(setting, settingInstance);

				/* Notify Observers of the new element */
				setChanged();
				notifyObservers(setting);
			} catch (Exception e) {
				/*
				 * If that happens, the method will later return NULL. Happens
				 * if constructor is not public or something like that.
				 */
			}
		}

		return settings.get(setting);
	}

	@Override
	public void deleteSetting(Class<? extends AbstractSetting> setting) {
		settings.remove(setting);
	}

	@Override
	public void update(Observable o, Object arg) {
		notifyObservers(new SettingsObserverObject(o.getClass(), settingsFilename, this));
	}
}