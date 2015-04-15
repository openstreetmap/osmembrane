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
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;

import de.osmembrane.view.interfaces.IView;

/**
 * An abstract class interface to be used for dialog view elements.
 * 
 * @author tobias_kuhn
 * 
 */
@SuppressWarnings("serial")
public abstract class AbstractDialog extends JDialog implements IView {

    /**
     * common constructor for all dialog view elements
     */
    public AbstractDialog(Window owner) {
        super(owner);
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        setModalityType(ModalityType.APPLICATION_MODAL);
        setResizable(false);

        // close dialog on escape
        this.getLayeredPane().getActionMap().put("close", new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                hideWindow();
            }
        });

        this.getLayeredPane()
                .getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "close");

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
