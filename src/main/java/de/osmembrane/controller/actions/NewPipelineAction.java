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
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import de.osmembrane.controller.ActionRegistry;
import de.osmembrane.model.ModelProxy;
import de.osmembrane.resources.Resource;
import de.osmembrane.tools.HeadlessSafe;
import de.osmembrane.tools.I18N;
import de.osmembrane.tools.IconLoader.Size;

/**
 * Action to create a completely empty, new pipeline.
 * 
 * @author tobias_kuhn
 * 
 */
public class NewPipelineAction extends AbstractAction {

    private static final long serialVersionUID = -8713011583509026047L;

    /**
     * Creates a new {@link NewPipelineAction}
     */
    public NewPipelineAction() {
        putValue(
                Action.NAME,
                I18N.getInstance().getString(
                        "Controller.Actions.NewPipeline.Name"));
        putValue(
                Action.SHORT_DESCRIPTION,
                I18N.getInstance().getString(
                        "Controller.Actions.NewPipeline.Description"));
        putValue(Action.SMALL_ICON, Resource.PROGRAM_ICON.getImageIcon(
                "new_pipeline.png", Size.SMALL));
        putValue(Action.LARGE_ICON_KEY, Resource.PROGRAM_ICON.getImageIcon(
                "new_pipeline.png", Size.NORMAL));
        putValue(
                Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke(KeyEvent.VK_N,
                        HeadlessSafe.getMenuShortcutKeyMask()));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        /* Ask if the Pipeline should be saved, if that is not so. */
        if (!ModelProxy.getInstance().getPipeline().isSaved()) {
            int result = JOptionPane.showConfirmDialog(
                    null,
                    I18N.getInstance().getString(
                            "Controller.Actions.NewPipeline.NotSaved"),
                    I18N.getInstance().getString(
                            "Controller.Actions.NewPipeline.NotSaved.Title"),
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            if (result == JOptionPane.YES_OPTION) {
                ActionRegistry.getInstance().get(SavePipelineAction.class)
                        .actionPerformed(null);
                /* check again if it is saved */
                if (!ModelProxy.getInstance().getPipeline().isSaved()) {
                    return;
                }
            } else if (result == JOptionPane.CANCEL_OPTION
                    || result == JOptionPane.CLOSED_OPTION) {
                return;
            }
        }

        ModelProxy.getInstance().getPipeline().clear();

        ActionRegistry.getInstance().get(ResetViewAction.class)
                .actionPerformed(null);
    }
}
