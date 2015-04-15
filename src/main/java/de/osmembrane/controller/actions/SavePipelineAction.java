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

import de.osmembrane.Application;
import de.osmembrane.controller.ActionRegistry;
import de.osmembrane.exceptions.ControlledException;
import de.osmembrane.exceptions.ExceptionSeverity;
import de.osmembrane.model.ModelProxy;
import de.osmembrane.model.persistence.FileException;
import de.osmembrane.resources.Resource;
import de.osmembrane.tools.HeadlessSafe;
import de.osmembrane.tools.I18N;
import de.osmembrane.tools.IconLoader.Size;

/**
 * Action to store a OSMembrane pipeline in file.
 * 
 * @author tobias_kuhn
 * 
 */
public class SavePipelineAction extends AbstractAction {

    private static final long serialVersionUID = 5036259208332239931L;

    /**
     * Creates a new {@link SavePipelineAction}
     */
    public SavePipelineAction() {
        putValue(
                Action.NAME,
                I18N.getInstance().getString(
                        "Controller.Actions.SavePipeline.Name"));
        putValue(
                Action.SHORT_DESCRIPTION,
                I18N.getInstance().getString(
                        "Controller.Actions.SavePipeline.Description"));
        putValue(Action.SMALL_ICON, Resource.PROGRAM_ICON.getImageIcon(
                "save_pipeline.png", Size.SMALL));
        putValue(Action.LARGE_ICON_KEY, Resource.PROGRAM_ICON.getImageIcon(
                "save_pipeline.png", Size.NORMAL));
        putValue(
                Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke(KeyEvent.VK_S,
                        HeadlessSafe.getMenuShortcutKeyMask()));
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (ModelProxy.getInstance().getPipeline().getFilename() == null) {
            ActionRegistry.getInstance().get(SaveAsPipelineAction.class)
                    .actionPerformed(null);
        } else {
            try {
                ModelProxy.getInstance().getPipeline().savePipeline();
            } catch (FileException e1) {
                Application.handleException(new ControlledException(this,
                        ExceptionSeverity.WARNING, e1, I18N.getInstance()
                                .getString(
                                        "Controller.Actions.Save.Failed."
                                                + e1.getType())));
            }
        }
    }
}
