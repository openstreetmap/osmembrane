package de.osmembrane.model.settings;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Observable;

import org.omg.IOP.CodecPackage.FormatMismatch;

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

	private static final long serialVersionUID = 2011020217010001L;

	Map<SettingType, Object> settingsMap = new HashMap<SettingType, Object>();

	@Override
	public void initiate() {
		AbstractPersistence persistence = PersistenceFactory.getInstance()
				.getPersistence(SettingPersistence.class);

		/*
		 * register the persistence as observer for automatic saving of the
		 * settings
		 */
		addObserver(PersistenceFactory.getInstance());

		try {
			File file = new File(Constants.DEFAULT_SETTINGS_FILE.toString()
					.replace("file:", ""));

			if (!file.isFile()) {
				saveSettings();
			}

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
	public void setValue(SettingType type, Object value)
			throws UnparsableFormatException {
		if (!type.getType().isInstance(value)) {
			value = type.parse(value);
		}

		type.doRequiredActions(value);

		settingsMap.put(type, value);
		changedNotifyObservers(new SettingsObserverObject(type));

	}

	@Override
	public Locale[] getLanguages() {
		return Constants.AVAILABLE_LOCALES;
	}

	@Override
	public void saveSettings() throws FileException {
		AbstractPersistence persistence = PersistenceFactory.getInstance()
				.getPersistence(SettingPersistence.class);

		persistence.save(Constants.DEFAULT_SETTINGS_FILE, settingsMap);
	}

	@Override
	public void update(Observable o, Object arg) {
		notifyObservers(settingsMap);
	}

	@Override
	protected void changedNotifyObservers(SettingsObserverObject soo) {
		soo.setSettingsModel(this);
		setChanged();
		notifyObservers(soo);
	}
}