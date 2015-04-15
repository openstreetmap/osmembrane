/*
 * This file is part of the OSMembrane project.
 * More informations under www.osmembrane.de
 * 
 * The project is licensed under the GNU GENERAL PUBLIC LICENSE 3.0.
 * for more details about the license see http://www.osmembrane.de/license/
 * 
 * Source: $HeadURL$ ($Revision$)
 * Last changed: $Date$
 */

package de.osmembrane.model.settings;

import java.util.Locale;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import de.osmembrane.Application;
import de.osmembrane.resources.Constants;
import de.osmembrane.tools.I18N;

/**
 * The type of a setting-entry.
 * 
 * @author jakob_jarosch
 */
public enum SettingType {
    /**
     * The default path to Osmosis binaries.
     * 
     * can be casted to a {@link String}
     */
    DEFAULT_OSMOSIS_PATH(String.class, Constants.DEFAULT_OSMOSIS_PATH),

    /**
     * The default path to JOSM binaries.
     * 
     * can be casted to a {@link String}
     */
    DEFAULT_JOSM_PATH(String.class, Constants.DEFAULT_JOSM_PATH),

    /**
     * The active language as a locale.
     * 
     * can be casted to a {@link Locale}
     */
    ACTIVE_LANGUAGE(Locale.class, Locale.getDefault()),

    /**
     * The default zoom size.
     * 
     * can be casted to a {@link Double}
     */
    DEFAULT_ZOOM_SIZE(Double.class, Constants.DEFAULT_ZOOM_SIZE),

    /**
     * Use short names for tasks if available.
     * 
     * can be casted to a {@link Boolean}
     */
    USE_SHORT_TASK_NAMES_IF_AVAILABLE(Boolean.class, false),

    /**
     * Export the default values or not.
     * 
     * can be casted to a {@link Boolean}
     */
    EXPORT_PARAMETERS_WITH_DEFAULT_VALUES(Boolean.class, false),

    /**
     * Maximum count of undo-steps.
     * 
     * can be casted to a {@link Integer}
     */
    MAXIMUM_UNDO_STEPS(Integer.class, Constants.MAXIMUM_UNDO_STEPS),

    /**
     * Coordinate raster size.
     * 
     * can be casted to a {@link Integer}
     */
    PIPELINE_RASTER_SIZE(Integer.class, Constants.DEFAULT_PIPELINE_RASTER_SIZE),

    /**
     * The working directory for execution and preview.
     * 
     * can be casted to a {@link String}
     */
    DEFAULT_WORKING_DIRECTORY(String.class, Constants.DEFAULT_WORKING_DIRECTORY),

    /**
     * Show the startup screen or not.
     * 
     * can be casted to a {@link Boolean}
     */
    SHOW_STARTUP_SCREEN(Boolean.class, Constants.DEFAULT_SHOW_STARTUP_SCREEN),

    /**
     * Should the Software update or not?
     * 
     * can be casted to a {@link SettingsTypeUpdateInterval}<br/>
     * 0: never<br/>
     * 1: once a day<br/>
     * 2: once a week<br/>
     */
    UPDATE_INTERVAL(SettingsTypeUpdateInterval.class,
            Constants.DEFAULT_UPDATE_INTERVAL),

    /**
     * The last date when OSMembrane tried to update. Timestamp since
     * 01.01.1980.
     */
    LAST_UPDATE_LOOKUP(Long.class, 0L),

    /**
     * The active pluggable Java Look & Feel as a name specified by the
     * {@link LookAndFeelInfo}.
     * 
     * can be casted to a {@link String}
     */
    ACTIVE_PLAF(String.class, Constants.DEFAULT_PLAF_NAME);

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
     * @param value
     *            value which should be updated
     */
    public void doRequiredActions(Object value) {
        switch (this) {
        case ACTIVE_LANGUAGE:
            I18N.getInstance().setLocale((Locale) value);
            break;

        case ACTIVE_PLAF:
            try {
                String pLaF = (String) value;
                for (LookAndFeelInfo info : UIManager
                        .getInstalledLookAndFeels()) {
                    if (info.getName().equals(pLaF)) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (Exception e) {
                // if setLookAndFeel() failed
                Application.handleException(e);
            }
            break;
        }
    }

    /**
     * Tries to parse the object to the equivalent Type-Object.
     * 
     * @param value
     *            value which should be parsed
     * @return the parsed value as an object
     * @throws UnparsableFormatException
     *             if the value was not parsable
     */
    public Object parse(Object value) throws UnparsableFormatException {
        Object returnValue;

        if (value == null) {
            throw new UnparsableFormatException(this);
        }

        try {
            if (this.getType() == Integer.class) {
                returnValue = (Integer) Integer.parseInt(value.toString());
            } else if (this.getType() == Double.class) {
                returnValue = (Double) Double.parseDouble(value.toString());
            } else if (this.getType() == Locale.class) {
                returnValue = (Locale) value;
            } else if (this.getType() == Boolean.class) {
                returnValue = (Boolean) Boolean.parseBoolean(value.toString());
            } else {
                returnValue = (String) value.toString();
            }
        } catch (Exception e) {
            throw new UnparsableFormatException(this);
        }

        return formatValue(returnValue);
    }

    /**
     * Does automatic formation.
     * 
     * @param value
     *            value which should be formatted
     * @return formatted value
     */
    private Object formatValue(Object value) {
        switch (this) {

        }

        return value;
    }
}
