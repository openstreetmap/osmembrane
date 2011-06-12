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

import java.awt.Point;

import de.osmembrane.view.frames.MainFrame;
import de.osmembrane.view.frames.MainFrameGlassPane;

/**
 * Interface for {@link MainFrame}
 * 
 * @author tobias_kuhn
 * 
 */
public interface IMainFrame extends IView {

    /**
     * @return the currently selected object on the pipeline of the main frame.
     */
    public Object getSelected();

    /**
     * Sets the explanation hint in the inspector panel to hint
     * 
     * @param hint
     *            the hint to display
     */
    public void setHint(String hint);

    /**
     * @return the glass pane in front of the {@link MainFrame}'s contents that
     *         can display data there (mainly Library-to-Pipeline drag & drop)
     */
    public MainFrameGlassPane getMainGlassPane();

    /**
     * Finds out whether drag & drop can be finished at at.
     * 
     * @param at
     *            {@link Point} on the getMainGlassPane() to check for
     * @return whether or not the point is valid drag and drop target
     */
    public boolean isDragAndDropTarget(Point at);

    /**
     * Returns the {@link IZoomDevice} capable of performing zooms
     * 
     * @return the corresponding IZoomDevice or null, if none present
     */
    public IZoomDevice getZoomDevice();

    /**
     * Maximizes the window.
     */
    public void maximizeWindow();
}
