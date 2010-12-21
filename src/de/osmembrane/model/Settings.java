package de.osmembrane.model;

import de.osmembrane.model.settings.AbstractSetting;
import java.util.Observable;

public class Settings extends Observable implements ISettings {

	public void initiate(String settingsFilename) {
		throw new UnsupportedOperationException();
	}

	public AbstractSetting[] getSettings() {
		throw new UnsupportedOperationException();
	}

	public void getSetting(Class<AbstractSetting> setting) {
		throw new UnsupportedOperationException();
	}

	public void addSetting(AbstractSetting setting) {
		throw new UnsupportedOperationException();
	}

	public void deleteSetting(Class<AbstractSetting> setting) {
		throw new UnsupportedOperationException();
	}
}