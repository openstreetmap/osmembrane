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
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import de.osmembrane.Application;
import de.osmembrane.exceptions.ControlledException;
import de.osmembrane.exceptions.ExceptionSeverity;
import de.osmembrane.model.ModelProxy;
import de.osmembrane.model.persistence.FileType;
import de.osmembrane.model.settings.SettingType;
import de.osmembrane.resources.Resource;
import de.osmembrane.tools.HeadlessSafe;
import de.osmembrane.tools.I18N;
import de.osmembrane.tools.IconLoader.Size;
import de.osmembrane.tools.PipelineExecutor;
import de.osmembrane.view.ViewRegistry;
import de.osmembrane.view.dialogs.ExecutionStateDialog;
import de.osmembrane.view.interfaces.IExecutionStateDialog;

/**
 * Action to directly execute the created pipeline on the local shell.
 * 
 * @author tobias_kuhn
 * 
 */
public class ExecutePipelineAction extends AbstractAction {

    private static final long serialVersionUID = -173334958831335922L;

    /**
     * Creates a new {@link ExecutePipelineAction}
     */
    public ExecutePipelineAction() {
        putValue(
                Action.NAME,
                I18N.getInstance().getString(
                        "Controller.Actions.ExecutePipeline.Name"));
        putValue(
                Action.SHORT_DESCRIPTION,
                I18N.getInstance().getString(
                        "Controller.Actions.ExecutePipeline.Description"));
        putValue(Action.SMALL_ICON, Resource.PROGRAM_ICON.getImageIcon(
                "execute_pipeline.png", Size.SMALL));
        putValue(Action.LARGE_ICON_KEY, Resource.PROGRAM_ICON.getImageIcon(
                "execute_pipeline.png", Size.NORMAL));
        putValue(
                Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke(KeyEvent.VK_X,
                        HeadlessSafe.getMenuShortcutKeyMask()));
    }

    @SuppressWarnings("unchecked")
    @Override
    public void actionPerformed(ActionEvent e) {

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

        FileType type = FileType.EXECUTION_FILETYPE;
        String pipeline = ModelProxy.getInstance().getPipeline().generate(type);

        /* the path to osmosis */
        final String osmosisPath = (String) ModelProxy.getInstance()
                .getSettings().getValue(SettingType.DEFAULT_OSMOSIS_PATH);

        /* the working directory */
        final String workingDirectory = (String) ModelProxy.getInstance()
                .getSettings().getValue(SettingType.DEFAULT_WORKING_DIRECTORY);

        final List<String> parameters = new ArrayList<String>();

        /* transform the params */
        String[] params = pipeline.split(" +");
        for (String param : params) {
            if (param.length() > 0) {
                parameters.add(param);
            }
        }

        IExecutionStateDialog dialog = ViewRegistry.getInstance().getCasted(
                ExecutionStateDialog.class, IExecutionStateDialog.class);

        /* clear the contents of the exectuion window */
        dialog.clear();

        Class<? extends Action> action;
        if (e.getSource() instanceof Action) {
            action = (Class<? extends Action>) e.getSource().getClass();
        } else {
            action = null;
        }

        try {
            PipelineExecutor executor = new PipelineExecutor(osmosisPath,
                    workingDirectory, parameters, dialog);
            executor.setCallbackAction(action);
            executor.start();
            dialog.showWindow();
        } catch (IllegalArgumentException e1) {
            Application
                    .handleException(new ControlledException(
                            this,
                            ExceptionSeverity.WARNING,
                            I18N.getInstance()
                                    .getString(
                                            "Controller.Actions.ExecutePipeline.OsmosisNotFound")));
        }

    }
}
