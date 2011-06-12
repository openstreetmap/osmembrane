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

package de.osmembrane;

import de.osmembrane.view.dialogs.ExceptionDialog;

/**
 * Class necessary to be instantiated when an EDT exception occurs to forward it
 * to the {@link ExceptionDialog}.
 * 
 * @see java.awt.EventDispatchThread#handleException
 * 
 * @author tobias_kuhn
 * 
 */
public class EDTExceptionHandler {

    /**
     * Handles exceptions thrown by the EDT of Swing
     * 
     * @param t
     *            the {@link Throwable} to catch
     */
    public void handle(Throwable t) {
        Application.handleException(t);
    }

}
