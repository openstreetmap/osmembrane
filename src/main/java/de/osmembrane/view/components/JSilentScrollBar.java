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

package de.osmembrane.view.components;

import java.awt.Adjustable;
import java.awt.event.AdjustmentListener;

import javax.swing.JScrollBar;

/**
 * A JScrollBar with the ability to not continously call its own
 * attributeChanged-Listener after setValue().
 * 
 * Kind of a dirty hack, but this seems to be a bug in Swing ({@link Adjustable}
 * specifies that an AdjustmentEvent should *NOT* be fired!) and there is not
 * really an alternative.
 * 
 * @see Adjustable#setValue(int)
 * @see JScrollBar#setValue(int)
 * 
 * @author tobias_kuhn
 * 
 */
public class JSilentScrollBar extends JScrollBar {

    private static final long serialVersionUID = -8976816464093270469L;

    /**
     * @see JScrollBar#JScrollBar()
     */
    public JSilentScrollBar() {
        super();
    }

    /**
     * @see JScrollBar#JScrollBar(int)
     */
    public JSilentScrollBar(int orientation) {
        super(orientation);
    }

    /**
     * @see JScrollBar#JScrollBar(int, int, int, int, int)
     */
    public JSilentScrollBar(int orientation, int value, int extent, int min,
            int max) {
        super(orientation, value, extent, min, max);
    }

    /**
     * Removes all dirty listeners and returns them. Guarantees no events.
     * 
     * @return the listeners removed
     */
    private AdjustmentListener[] removeDirtyListeners() {
        AdjustmentListener[] als = getAdjustmentListeners();
        for (AdjustmentListener al : als) {
            removeAdjustmentListener(al);
        }
        return als;
    }

    /**
     * Adds the dirty listeners saved in als
     * 
     * @param als
     *            the dirty listeners previously removed
     */
    private void addDirtyListeners(AdjustmentListener[] als) {
        for (AdjustmentListener al : als) {
            addAdjustmentListener(al);
        }
    }

    /**
     * Equivalent to {@link JScrollBar#setValue(int)}, only silently
     */
    public void setValueSilently(int value) {
        AdjustmentListener[] als = removeDirtyListeners();
        setValue(value);
        addDirtyListeners(als);
    }

    /**
     * Equivalent to {@link JScrollBar#setMinimum(int)}, only silently
     */
    public void setMinimumSilently(int minimum) {
        AdjustmentListener[] als = removeDirtyListeners();
        setMinimum(minimum);
        addDirtyListeners(als);
    }

    /**
     * Equivalent to {@link JScrollBar#setMinimum(int)}, only silently
     */
    public void setMaximumSilently(int maximum) {
        AdjustmentListener[] als = removeDirtyListeners();
        setMaximum(maximum);
        addDirtyListeners(als);
    }

}
