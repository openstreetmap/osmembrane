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

import de.osmembrane.model.ModelProxy;
import de.osmembrane.resources.Resource;
import de.osmembrane.tools.HeadlessSafe;
import de.osmembrane.tools.I18N;
import de.osmembrane.tools.IconLoader.Size;
import de.osmembrane.view.ViewRegistry;
import de.osmembrane.view.dialogs.CommandLineDialog;
import de.osmembrane.view.interfaces.ICommandLineDialog;

/**
 * Action to generate the pipeline command line and display the
 * {@link CommandLineDialog}.
 * 
 * @author tobias_kuhn
 * 
 */
public class GeneratePipelineAction extends AbstractAction {

    private static final long serialVersionUID = -932349116204149527L;

    /**
     * Creates a new {@link GeneratePipelineAction}
     */
    public GeneratePipelineAction() {
        putValue(
                Action.NAME,
                I18N.getInstance().getString(
                        "Controller.Actions.GeneratePipeline.Name"));
        putValue(
                Action.SHORT_DESCRIPTION,
                I18N.getInstance().getString(
                        "Controller.Actions.GeneratePipeline.Description"));
        putValue(Action.SMALL_ICON, Resource.PROGRAM_ICON.getImageIcon(
                "generate_pipeline.png", Size.SMALL));
        putValue(Action.LARGE_ICON_KEY, Resource.PROGRAM_ICON.getImageIcon(
                "generate_pipeline.png", Size.NORMAL));
        putValue(
                Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke(KeyEvent.VK_G,
                        HeadlessSafe.getMenuShortcutKeyMask()));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ICommandLineDialog commandLineDialog = ViewRegistry.getInstance()
                .getCasted(CommandLineDialog.class, ICommandLineDialog.class);

        /* Check if the pipeline is complete */
        if (!ModelProxy.getInstance().getPipeline().isComplete()) {
            if (!(JOptionPane.showConfirmDialog(
                    null,
                    I18N.getInstance().getString(
                            "Controller.Actions.PipelineNotComplete"),
                    I18N.getInstance().getString(
                            "Controller.Actions.PipelineNotComplete.Title"),
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION)) {
                return;
            }
        }

        commandLineDialog.setPipeline(ModelProxy.getInstance().getPipeline());
        commandLineDialog.showWindow();
    }
}
