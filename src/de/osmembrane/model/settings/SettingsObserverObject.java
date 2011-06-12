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

/**
 * A observer-object which is passed through by the {@link Settings}-Model.
 * 
 * @author jakob_jarosch
 */
public class SettingsObserverObject {

    private SettingType changedEntry;
    private AbstractSettings settingsModel;

    /**
     * @see SettingsObserverObject#SettingsObserverObject(SettingType)
     */
    public SettingsObserverObject() {
        this.changedEntry = null;
    }

    /**
     * Creates a new {@link SettingsObserverObject} with given changed type of
     * the entry.
     * 
     * @param changedEntry
     *            type of changed entry
     */
    public SettingsObserverObject(SettingType changedEntry) {
        this.changedEntry = changedEntry;
    }

    /**
     * Returns the type of the changed entry.
     * 
     * @return type of the changed entry
     */
    public SettingType getChangedEntry() {
        return changedEntry;
    }

    /**
     * Sets the {@link Settings}-model.
     * 
     * @param settingsModel
     */
    public void setSettingsModel(Settings settingsModel) {
        this.settingsModel = settingsModel;
    }

    /**
     * Returns the {@link Settings}-model which is the initiator of the
     * notification.
     * 
     * @return the {@link Settings}-model
     */
    public AbstractSettings getSettingsModel() {
        return settingsModel;
    }
}
