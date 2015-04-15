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

package de.osmembrane.model.statusbar;

import java.util.Observer;

import de.osmembrane.model.settings.AbstractSettings;

/**
 * The {@link StatusbarObserverObject} is used by the {@link AbstractSettings}
 * and informs all registered {@link Observer}s with this object.
 * 
 * @author jakob_jarosch
 */
public class StatusbarObserverObject {

    private StatusbarEntry entry;

    /**
     * Creates a new {@link StatusbarObserverObject}.
     * 
     * @param entry
     *            which has been changed.
     */
    public StatusbarObserverObject(StatusbarEntry entry) {
        this.entry = entry;
    }

    /**
     * Returns the changed entry.
     * 
     * @return changed entry
     */
    public StatusbarEntry getEntry() {
        return entry;
    }
}
