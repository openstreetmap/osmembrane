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

package de.osmembrane.view;

import java.awt.GraphicsEnvironment;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import de.osmembrane.view.interfaces.IView;

/**
 * An abstract class interface to be used for dialog frame elements.
 * 
 * @author tobias_kuhn
 * 
 */
@SuppressWarnings("serial")
public abstract class AbstractFrame extends JFrame implements IView {

    /**
     * common constructor for all frame view elements
     */
    public AbstractFrame() {
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    }

    @Override
    public void showWindow() {
        setVisible(true);
    }

    @Override
    public void hideWindow() {
        setVisible(false);
    }

    @Override
    public void setWindowTitle(String title) {
        setTitle(title);
    }

    @Override
    public void centerWindow() {
        Point screenCenter = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getCenterPoint();
        Point edgeLeftTop = new Point(screenCenter.x - (getWidth() / 2),
                screenCenter.y - (getHeight() / 2));
        setLocation(edgeLeftTop.x, edgeLeftTop.y);
    }

    @Override
    public void bringToFront() {
        toFront();
    }
}
