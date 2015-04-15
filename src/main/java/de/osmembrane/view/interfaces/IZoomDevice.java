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

/**
 * An interface that can be implemented for the use of receiving zooming actions
 * invoked from the Controller.
 * 
 * @author tobias_kuhn
 * 
 */
public interface IZoomDevice {

    /**
     * Zooms in
     */
    public void zoomIn();

    /**
     * Zooms out
     */
    public void zoomOut();

    /**
     * Resets the view to standard
     */
    public void resetView();

    /**
     * Shows the entire pipeline
     */
    public void showEntireView();

}
