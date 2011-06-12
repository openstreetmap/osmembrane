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

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import de.osmembrane.resources.Constants;

/**
 * Implementation of {@link AbstractStatusbar}.
 * 
 * @author jakob_jarosch
 */
public class Statusbar extends AbstractStatusbar {

    private List<StatusbarEntry> entries = new ArrayList<StatusbarEntry>();

    @Override
    public StatusbarEntry[] getStatusbarEntries() {
        return entries.toArray(new StatusbarEntry[entries.size()]);
    }

    @Override
    public void addStatusbarEntry(StatusbarEntry entry) {
        entry.addObserver(this);
        entries.add(entry);

        removeUnusedEntries();
    }

    @Override
    public boolean removeStatusbarEntry(StatusbarEntry entry) {
        entry.deleteObservers();

        return false;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof StatusbarEntry) {
            setChanged();
            notifyObservers(new StatusbarObserverObject((StatusbarEntry) o));
        }
    }

    /**
     * Removes all entries, which are unused an exceed the statusbar item limit.
     */
    private void removeUnusedEntries() {
        boolean removedOne = true;
        while (entries.size() > Constants.MAXIMUM_STATUSBAR_ENTRIES
                && removedOne) {
            removedOne = false;
            for (int i = (entries.size() - 1); i > 1; i++) {
                StatusbarEntry entry = entries.get(i);
                if (!entry.isProgressbarUsed() || entry.getProgress() >= 1.0) {
                    entries.remove(i);
                    removedOne = true;
                    break;
                }
            }
        }
    }
}
