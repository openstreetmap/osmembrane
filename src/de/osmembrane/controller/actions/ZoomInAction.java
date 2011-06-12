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

package de.osmembrane.controller.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import de.osmembrane.resources.Resource;
import de.osmembrane.tools.HeadlessSafe;
import de.osmembrane.tools.I18N;
import de.osmembrane.tools.IconLoader.Size;
import de.osmembrane.view.ViewRegistry;
import de.osmembrane.view.frames.MainFrame;
import de.osmembrane.view.interfaces.IMainFrame;

/**
 * Action to nonchalantly zoom in.
 * 
 * @author tobias_kuhn
 * 
 */
public class ZoomInAction extends AbstractAction {

    private static final long serialVersionUID = 5671603148720489921L;

    /**
     * Creates a new {@link ZoomInAction}
     */
    public ZoomInAction() {
        putValue(Action.NAME,
                I18N.getInstance().getString("Controller.Actions.ZoomIn.Name"));
        putValue(
                Action.SHORT_DESCRIPTION,
                I18N.getInstance().getString(
                        "Controller.Actions.ZoomIn.Description"));
        putValue(Action.SMALL_ICON,
                Resource.PROGRAM_ICON.getImageIcon("zoom_in.png", Size.SMALL));
        putValue(Action.LARGE_ICON_KEY,
                Resource.PROGRAM_ICON.getImageIcon("zoom_in.png", Size.NORMAL));
        putValue(
                Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke(KeyEvent.VK_PLUS,
                        HeadlessSafe.getMenuShortcutKeyMask()));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        IMainFrame mainFrame = ViewRegistry.getInstance().getCasted(
                MainFrame.class, IMainFrame.class);
        mainFrame.getZoomDevice().zoomIn();
    }

}
