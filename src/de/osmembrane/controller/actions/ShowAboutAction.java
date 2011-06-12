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

import javax.swing.AbstractAction;
import javax.swing.Action;

import de.osmembrane.resources.Resource;
import de.osmembrane.tools.I18N;
import de.osmembrane.tools.IconLoader.Size;
import de.osmembrane.view.ViewRegistry;
import de.osmembrane.view.dialogs.AboutDialog;

/**
 * Action to show the about dialog
 * 
 * @author tobias_kuhn
 * 
 */
public class ShowAboutAction extends AbstractAction {

    private static final long serialVersionUID = 1015846096381941393L;

    /**
     * Creates a new {@link ShowAboutAction}
     */
    public ShowAboutAction() {
        putValue(
                Action.NAME,
                I18N.getInstance().getString(
                        "Controller.Actions.ShowAbout.Name"));
        putValue(
                Action.SHORT_DESCRIPTION,
                I18N.getInstance().getString(
                        "Controller.Actions.ShowAbout.Description"));
        putValue(Action.SMALL_ICON,
                Resource.PROGRAM_ICON.getImageIcon("about.png", Size.SMALL));
        putValue(Action.LARGE_ICON_KEY,
                Resource.PROGRAM_ICON.getImageIcon("about.png", Size.NORMAL));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ViewRegistry.getInstance().get(AboutDialog.class).showWindow();
    }
}
