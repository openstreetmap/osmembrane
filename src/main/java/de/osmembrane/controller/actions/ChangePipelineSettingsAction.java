/*
 * This file is part of the OSMembrane project.
 * More informations under www.osmembrane.de
 * 
 * The project is licensed under the GNU GENERAL PUBLIC LICENSE 3.0.
 * for more details about the license see http://www.osmembrane.de/license/
 * 
 * Source: $HeadURL: https://osmembrane.de/svn/sources/src/header.txt $ ($Revision: 703 $)
 * Last changed: $Date: 2011-02-07 10:56:49 +0100 (Mo, 07 Feb 2011) $
 */

package de.osmembrane.controller.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import de.osmembrane.model.ModelProxy;
import de.osmembrane.model.pipeline.AbstractPipelineSettings;
import de.osmembrane.resources.Resource;
import de.osmembrane.tools.I18N;
import de.osmembrane.tools.IconLoader.Size;
import de.osmembrane.view.ViewRegistry;
import de.osmembrane.view.dialogs.PipelineSettingsDialog;
import de.osmembrane.view.interfaces.IPipelineSettingsDialog;

/**
 * Action to open the change pipeline settings dialog.
 * 
 * @author tobias_kuhn
 * 
 */
public class ChangePipelineSettingsAction extends AbstractAction {

    private static final long serialVersionUID = -1491395674816531738L;

    /**
     * Creates a new {@link ChangePipelineSettingsAction}
     */
    public ChangePipelineSettingsAction() {
        putValue(
                Action.NAME,
                I18N.getInstance().getString(
                        "Controller.Actions.ChangePipelineSettings.Name"));
        putValue(
                Action.SHORT_DESCRIPTION,
                I18N.getInstance()
                        .getString(
                                "Controller.Actions.ChangePipelineSettings.Description"));
        putValue(Action.SMALL_ICON, Resource.PROGRAM_ICON.getImageIcon(
                "property_edit.png", Size.SMALL));
        putValue(Action.LARGE_ICON_KEY, Resource.PROGRAM_ICON.getImageIcon(
                "property_edit.png", Size.NORMAL));
        putValue(Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        IPipelineSettingsDialog dialog = ViewRegistry.getInstance().getCasted(
                PipelineSettingsDialog.class, IPipelineSettingsDialog.class);

        // set the display components
        AbstractPipelineSettings settings = ModelProxy.getInstance()
                .getPipeline().getSettings();

        dialog.setVerbose(settings.getVerbose());
        dialog.setDebug(settings.getDebug());
        dialog.setComment(settings.getComment());
        dialog.setName(settings.getName());

        dialog.showWindow();

        if (dialog.shallApplyChanges()) {
            // set the model values, if necessary

            settings.setVerbose(dialog.getVerbose());
            settings.setDebug(dialog.getDebug());
            settings.setComment(dialog.getComment());
            settings.setName(dialog.getName());

        } /* if apply changes */
    }
}
