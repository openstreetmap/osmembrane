package de.osmembrane.model.settings;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Set;

import de.osmembrane.Application;
import de.osmembrane.model.persistence.AbstractPersistence;
import de.osmembrane.model.persistence.FileException;
import de.osmembrane.model.persistence.PersistenceFactory;
import de.osmembrane.model.persistence.SettingPersistence;
import de.osmembrane.resources.Constants;

/**
 * 
 * 
 * @author jakob_jarosch
 */
public class Settings extends AbstractSettings {

	Map<SettingType, Object> settingsMap = new HashMap<SettingType, Object>();

	@Override
	public void initiate() {
		AbstractPersistence persistence = PersistenceFactory.getInstance()
				.getPersistence(SettingPersistence.class);

		/*
		 * TODO Check if file exists, and when not try first to create a default
		 * one
		 */

		try {
			Object obj = persistence.load(Constants.DEFAULT_SETTINGS_FILE);

			/* is checked by persistence */
			@SuppressWarnings("unchecked")
			Map<SettingType, Object> settingsMap = (Map<SettingType, Object>) obj;
			this.settingsMap = settingsMap;
		} catch (FileException e) {
			/* TODO Implement the correct exception handling for Settings */
			Application.handleException(e);
		}
	}

	@Override
	public Object getValue(SettingType type) {
		Object result = settingsMap.get(type);
		if (result != null) {
			return result;
		} else {
			return type.getDefaultValue();
		}
	}

	@Override
	public void setValue(SettingType type, Object value) {
		settingsMap.put(type, value);

	}

	@Override
	public Locale[] getLanguages() {
		return Constants.AVAILABLE_LOCALES;
	}

	@Override
	public void update(Observable o, Object arg) {
		notifyObservers(settingsMap);
	}
}