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

import java.awt.Window;

import de.osmembrane.view.ViewRegistry;
import de.osmembrane.view.dialogs.ExceptionDialog;

/**
 * View elements (i.e. windows) to be organized by the {@link ViewRegistry}
 * <b>N.B.</b>: Exceptions thrown in an IView constructor are not guaranteed to
 * result in an {@link ExceptionDialog}. (the ExceptionDialog is a view itself)
 * 
 * @author tobias_kuhn
 * 
 */
public interface IView {

    /**
     * Shows the particular window
     */
    public void showWindow();

    /**
     * Hides the particular window
     */
    public void hideWindow();

    /**
     * Sets the title of the particular window
     * 
     * @param viewTitle
     *            the new title
     */
    public void setWindowTitle(String viewTitle);

    /**
     * Centers this particular frame on the screen.
     */
    public void centerWindow();

    /**
     * Brings this window to the front.
     */
    public void bringToFront();

    /**
     * @see Window#dispose()
     */
    public void dispose();
}
