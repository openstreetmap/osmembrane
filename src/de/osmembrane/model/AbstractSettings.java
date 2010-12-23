package de.osmembrane.model;

import java.util.Observable;

import de.osmembrane.model.settings.AbstractSetting;

public abstract class AbstractSettings extends Observable {

	public abstract void initiate(String settingsFilename);

	public abstract AbstractSetting[] getSettings();

	public abstract void getSetting(Class<AbstractSetting> setting);

	public abstract void addSetting(AbstractSetting setting);

	public abstract void deleteSetting(Class<AbstractSetting> setting);
}