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

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import de.osmembrane.Application;
import de.osmembrane.exceptions.ControlledException;
import de.osmembrane.exceptions.ExceptionSeverity;
import de.osmembrane.resources.Constants;
import de.osmembrane.resources.Resource;
import de.osmembrane.tools.I18N;
import de.osmembrane.tools.IconLoader.Size;

/**
 * Action to explain the question "what does this thing do?"
 * 
 * @author tobias_kuhn
 * 
 */
public class ShowHelpAction extends AbstractAction {

    private static final long serialVersionUID = 1015846096381941393L;

    /**
     * Creates a new {@link ShowHelpAction}
     */
    public ShowHelpAction() {
        putValue(Action.NAME,
                I18N.getInstance()
                        .getString("Controller.Actions.ShowHelp.Name"));
        putValue(
                Action.SHORT_DESCRIPTION,
                I18N.getInstance().getString(
                        "Controller.Actions.ShowHelp.Description"));
        putValue(Action.SMALL_ICON,
                Resource.PROGRAM_ICON.getImageIcon("help.png", Size.SMALL));
        putValue(Action.LARGE_ICON_KEY,
                Resource.PROGRAM_ICON.getImageIcon("help.png", Size.NORMAL));
        putValue(Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Desktop d = Desktop.getDesktop();
        try {
            d.open(Constants.HELP_FILE_PATH);
        } catch (Exception e1) {
            Application.handleException(new ControlledException(this,
                    ExceptionSeverity.WARNING, I18N.getInstance().getString(
                            "Controller.Actions.ShowHelp.FileNotFound",
                            Constants.HELP_FILE_PATH)));
        }
    }
}
