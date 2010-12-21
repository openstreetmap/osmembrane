package de.osmembrane.model;

import de.osmembrane.model.settings.AbstractSetting;

public interface ISettings {

	public void initiate(String settingsFilename);

	public AbstractSetting[] getSettings();

	public void getSetting(Class<AbstractSetting> setting);

	public void addSetting(AbstractSetting setting);

	public void deleteSetting(Class<AbstractSetting> setting);
}