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

import java.util.Observable;

/**
 * A entry of the {@link AbstractStatusbar}.
 * 
 * @author jakob_jarosch
 */
public class StatusbarEntry extends Observable {

    private String message;
    private boolean progressbarUsed;
    private double progress;

    /**
     * @see StatusbarEntry#StatusbarEntry(String, boolean)
     */
    public StatusbarEntry(String message) {
        this(message, false);
    }

    /**
     * Creates a new {@link StatusbarEntry} with a given message and progressbar
     * enabled or not.
     * 
     * @param message
     *            message which should be displayed in the statusbar
     * @param progressbarUsed
     *            true if the statusbar should be used, otherwise false
     */
    public StatusbarEntry(String message, boolean progressbarUsed) {
        this.message = message;
        this.progressbarUsed = progressbarUsed;
    }

    /**
     * Sets a new statusbar-message.
     */
    public void setMessage(String message) {
        this.message = message;

        setChanged();
        notifyObservers();
    }

    /**
     * @return statusbar message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @return true if the progressbar is used, otherwise false
     */
    public boolean isProgressbarUsed() {
        return progressbarUsed;
    }

    /**
     * Sets the new progress for the {@link StatusbarEntry}.
     * 
     * @param progress
     *            new progress to be set.
     */
    public void setProgress(double progress) {
        this.progress = progress;

        setChanged();
        notifyObservers();
    }

    /**
     * @return the current progress of the {@link StatusbarEntry}
     */
    public double getProgress() {
        return progress;
    }
}
