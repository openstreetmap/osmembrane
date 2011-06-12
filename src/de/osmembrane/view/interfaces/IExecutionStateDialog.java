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

import java.awt.event.WindowListener;

import javax.swing.JDialog;

import de.osmembrane.view.dialogs.ExecutionStateDialog;

/**
 * Interface for {@link ExecutionStateDialog}.
 * 
 * @author jakob_jarosch
 */
public interface IExecutionStateDialog extends IView {

    /**
     * Sets the shown state of the execution action.
     * 
     * @param state
     *            state to be set
     */
    public void setState(String state);

    /**
     * Sets the progress of a status bar.
     * 
     * @param progress
     *            0 <= progress <= 100 to be set
     */
    public void setProgress(int progress);

    /**
     * Adds a line to the output.
     * 
     * @param outputLine
     *            line which should be added
     */
    public void addOutputLine(String outputLine);

    /**
     * Sets the caption of the close button to caption
     * 
     * @param caption
     *            new caption of the close button
     */
    public void setCloseButtonCaption(String caption);

    /**
     * Clears the contents of the window.
     */
    public void clear();

    /**
     * @see JDialog#addWindowListener(WindowListener)
     */
    public void addWindowListener(WindowListener wl);

    /**
     * @see JDialog#removeWindowListener(WindowListener)
     */
    public void removeWindowListener(WindowListener wl);
}
