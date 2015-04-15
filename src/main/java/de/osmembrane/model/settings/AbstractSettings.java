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

import java.io.Serializable;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

import de.osmembrane.model.ModelProxy;
import de.osmembrane.model.persistence.FileException;
import de.osmembrane.model.pipeline.AbstractFunction;

/**
 * Interface for accessing the {@link Settings} through the {@link ModelProxy}.
 * 
 * @author jakob_jarosch
 */
public abstract class AbstractSettings extends Observable implements
        Serializable, Observer {

    private static final long serialVersionUID = 2010122714350001L;

    /**
     * Initiates the Settings-model.
     */
    public abstract void initiate();

    /**
     * Save the settings to its settings file.
     * 
     * @throws FileException
     *             is thrown if the settings could not be saved
     */
    public abstract void saveSettings() throws FileException;

    /**
     * Returns a value.
     * 
     * @param type
     *            type of the value which should be returned
     * 
     * @return the requested value or the default value for it
     */
    public abstract Object getValue(SettingType type);

    /**
     * Sets the value for a given type.
     * 
     * @param type
     *            type which should be set
     * 
     * @param value
     *            value which should be assigned to the type
     */
    public abstract void setValue(SettingType type, Object value)
            throws UnparsableFormatException;

    /**
     * Returns all available languages.
     * 
     * @return all available languages
     */
    public abstract Locale[] getLanguages();

    /**
     * Saves a function with a given name to a preset.
     * 
     * @param name
     *            name of the preset
     * @param function
     *            which should be saved into the new preset
     */
    public abstract void saveFunctionPreset(String name,
            AbstractFunction function);

    /**
     * Returns all compatible presets to a function.
     * 
     * @param function
     *            function for which all available presets should be returned
     * @return all available presets for the function
     */
    public abstract AbstractFunctionPreset[] getAllFunctionPresets(
            AbstractFunction function);

    /**
     * Deletes a preset from the model.
     * 
     * @param preset
     *            preset which should be removed
     */
    public abstract boolean deleteFunctionPreset(AbstractFunctionPreset preset);

    /**
     * Notifies all observers.
     * 
     * @param soo
     *            a {@link SettingsObserverObject}
     */
    protected abstract void changedNotifyObservers(SettingsObserverObject soo);
}
