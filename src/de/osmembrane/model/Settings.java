package de.osmembrane.model;

import de.osmembrane.model.settings.AbstractSetting;

public class Settings extends de.osmembrane.model.java.util.Observable implements ISettings {

	public void initiate(String aSettingsFilename) {
		throw new UnsupportedOperationException();
	}

	public AbstractSettings[] getSettings() {
		throw new UnsupportedOperationException();
	}

	public void getSetting(Class<AbstractSetting> aSetting) {
		throw new UnsupportedOperationException();
	}

	public void addSetting(AbstractSetting aSetting) {
		throw new UnsupportedOperationException();
	}

	public void deleteSetting(Class<AbstractSetting> aSetting) {
		throw new UnsupportedOperationException();
	}
}