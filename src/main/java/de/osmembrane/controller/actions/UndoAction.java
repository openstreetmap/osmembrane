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

import de.osmembrane.model.ModelProxy;
import de.osmembrane.resources.Resource;
import de.osmembrane.tools.HeadlessSafe;
import de.osmembrane.tools.I18N;
import de.osmembrane.tools.IconLoader.Size;

/**
 * Action to undo the last done edit in the pipeline.
 * 
 * @author tobias_kuhn
 * 
 */
public class UndoAction extends AbstractAction {

    private static final long serialVersionUID = 4603258297595882886L;

    /**
     * Creates a new {@link UndoAction}
     */
    public UndoAction() {
        putValue(Action.NAME,
                I18N.getInstance().getString("Controller.Actions.Undo.Name"));
        putValue(
                Action.SHORT_DESCRIPTION,
                I18N.getInstance().getString(
                        "Controller.Actions.Undo.Description"));
        putValue(Action.SMALL_ICON,
                Resource.PROGRAM_ICON.getImageIcon("undo.png", Size.SMALL));
        putValue(Action.LARGE_ICON_KEY,
                Resource.PROGRAM_ICON.getImageIcon("undo.png", Size.NORMAL));
        putValue(
                Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke(KeyEvent.VK_Z,
                        HeadlessSafe.getMenuShortcutKeyMask()));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ModelProxy.getInstance().getPipeline().undo();
    }
}
