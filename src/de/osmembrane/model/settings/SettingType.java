package de.osmembrane.model.settings;

import java.util.Locale;

import de.osmembrane.resources.Constants;

public enum SettingType {
	DEFAULT_OSMOSIS_PATH(String.class, Constants.DEFAULT_OSMOSIS_PATH),
	DEFAULT_JOSM_PATH(String.class, Constants.DEFAULT_JOSM_PATH),
	ACTIVE_LANGUAGE(Locale.class, Constants.DEFAULT_LOCALE),
	DEFAULT_ZOOM_SIZE(Double.class, 1.0),
	USE_SHORT_TASK_NAMES_IF_AVAILABLE(Boolean.class, false),
	EXPORT_PARAMETERS_WITH_DEFAULT_VALUES(Boolean.class, false),
	MAXIMUM_UNDO_STEPS(Integer.class, Constants.MAXIMUM_UNDO_STEPS);
	
	private Class clazz;
	private Object defaultValue;
	
	SettingType(Class clazz, Object defaultValue) {
		this.clazz = clazz;
		this.defaultValue = defaultValue;
	}
	
	public Class getType() {
		return clazz;
	}
	
	public Object getDefaultValue() {
		return defaultValue;
	}
}
