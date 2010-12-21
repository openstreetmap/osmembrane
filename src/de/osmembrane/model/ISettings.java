package de.osmembrane.model;

import de.osmembrane.model.settings.AbstractSetting;

public interface ISettings {

	public void initiate(String aSettingsFilename);

	public AbstractSettings[] getSettings();

	public void getSetting(Class<AbstractSetting> aSetting);

	public void addSetting(AbstractSetting aSetting);

	public void deleteSetting(Class<AbstractSetting> aSetting);
}