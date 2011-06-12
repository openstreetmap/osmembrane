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

package de.osmembrane.view.interfaces;

import java.util.Locale;

import de.osmembrane.model.settings.SettingType;

/**
 * Interface for SettingsDialog.
 * 
 * @author tobias_kuhn
 * 
 */
public interface ISettingsDialog extends IView {

    /**
     * Sets the available locales to locales.
     * 
     * @param locales
     *            the available locales
     */
    public void setLocales(Locale[] locales);

    /**
     * @return whether or not the changes made in the dialog should be applied
     *         to the model
     */
    public boolean shallApplyChanges();

    /**
     * Returns the set value for type.
     * 
     * @param type
     *            the {@link SettingType}
     * @return the value for type
     */
    public Object getValue(SettingType type);

    /**
     * Sets the value of type in the SettingsDialog to value.
     * 
     * @param type
     *            the {@link SettingType}
     * @param value
     *            the value for type
     */
    public void setValue(SettingType type, Object value);

}
