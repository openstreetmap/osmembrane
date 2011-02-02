package de.osmembrane.model.settings;

import java.util.Locale;

import de.osmembrane.resources.Constants;
import de.osmembrane.tools.I18N;

/**
 * The type of a setting-entry.
 * 
 * @author jakob_jarosch
 */
public enum SettingType {
	/**
	 * The default path to osmosis binaries.
	 */
	DEFAULT_OSMOSIS_PATH(String.class, Constants.DEFAULT_OSMOSIS_PATH),
	/**
	 * The default path to JOSM binaries.
	 */
	DEFAULT_JOSM_PATH(String.class, Constants.DEFAULT_JOSM_PATH),

	/**
	 * The active language as a locale.
	 */
	ACTIVE_LANGUAGE(Locale.class, Locale.getDefault()),

	/**
	 * The default zoom size.
	 */
	DEFAULT_ZOOM_SIZE(Double.class, Constants.DEFAULT_ZOOM_SIZE),

	/**
	 * Use short names for tasks if available.
	 */
	USE_SHORT_TASK_NAMES_IF_AVAILABLE(Boolean.class, false),

	/**
	 * Export the default values or not.
	 */
	EXPORT_PARAMETERS_WITH_DEFAULT_VALUES(Boolean.class, false),

	/**
	 * Maximum count of undo-steps.
	 */
	MAXIMUM_UNDO_STEPS(Integer.class, Constants.MAXIMUM_UNDO_STEPS),
	
	/**
	 * Coordinate raster size.
	 */
	PIPELINE_RASTER_SIZE(Integer.class, Constants.DEFAULT_PIPELINE_RASTER_SIZE);

	private Class<?> clazz;
	private Object defaultValue;

	SettingType(Class<?> clazz, Object defaultValue) {
		this.clazz = clazz;
		this.defaultValue = defaultValue;
	}

	/**
	 * Returns the type if the value.
	 * 
	 * @return type of the value
	 */
	public Class<?> getType() {
		return clazz;
	}

	/**
	 * Returns the default value for a type.
	 * 
	 * @return default value for a type
	 */
	public Object getDefaultValue() {
		return defaultValue;
	}

	/**
	 * Do required actions if required, for example refresh the I18N-class.
	 * 
	 * @param value value which should be updated
	 */
	public void doRequiredActions(Object value) {
		switch (this) {
		case ACTIVE_LANGUAGE:
			I18N.getInstance().setLocale((Locale) value);
			break;
		}
	}
}
