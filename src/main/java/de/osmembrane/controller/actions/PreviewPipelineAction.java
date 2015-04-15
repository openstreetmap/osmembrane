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
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import de.osmembrane.Application;
import de.osmembrane.controller.ActionRegistry;
import de.osmembrane.exceptions.ControlledException;
import de.osmembrane.exceptions.ExceptionSeverity;
import de.osmembrane.model.ModelProxy;
import de.osmembrane.model.pipeline.AbstractFunction;
import de.osmembrane.model.pipeline.AbstractParameter;
import de.osmembrane.model.settings.SettingType;
import de.osmembrane.resources.Constants;
import de.osmembrane.resources.Resource;
import de.osmembrane.tools.HeadlessSafe;
import de.osmembrane.tools.I18N;
import de.osmembrane.tools.IconLoader.Size;
import de.osmembrane.tools.PipelineExecutor;

/**
 * Action to preview the generated pipeline. Like {@link ExecutePipelineAction},
 * only faster and with direct display.
 * 
 * @author tobias_kuhn
 * 
 */
public class PreviewPipelineAction extends AbstractAction {

    private static final long serialVersionUID = 8099091858953447990L;

    /**
     * Creates a new {@link PreviewPipelineAction}
     */
    public PreviewPipelineAction() {
        putValue(
                Action.NAME,
                I18N.getInstance().getString(
                        "Controller.Actions.PreviewPipeline.Name"));
        putValue(
                Action.SHORT_DESCRIPTION,
                I18N.getInstance().getString(
                        "Controller.Actions.PreviewPipeline.Description"));
        putValue(Action.SMALL_ICON, Resource.PROGRAM_ICON.getImageIcon(
                "preview_pipeline.png", Size.SMALL));
        putValue(Action.LARGE_ICON_KEY, Resource.PROGRAM_ICON.getImageIcon(
                "preview_pipeline.png", Size.NORMAL));
        putValue(
                Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke(KeyEvent.VK_P,
                        HeadlessSafe.getMenuShortcutKeyMask()));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!(e.getSource() instanceof PipelineExecutor)) {
            ActionRegistry
                    .getInstance()
                    .get(ExecutePipelineAction.class)
                    .actionPerformed(
                            new ActionEvent(this, 0, "PreviewExection"));
        } else {

            /* Check if the pipeline is complete */
            if (!ModelProxy.getInstance().getPipeline().isComplete()) {
                if (!(JOptionPane
                        .showConfirmDialog(
                                null,
                                I18N.getInstance()
                                        .getString(
                                                "Controller.Actions.PipelineNotComplete"),
                                I18N.getInstance()
                                        .getString(
                                                "Controller.Actions.PipelineNotComplete.Title"),
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION)) {
                    return;
                }
            }

            /* the path to josm */
            final String josmPath = (String) ModelProxy.getInstance()
                    .getSettings().getValue(SettingType.DEFAULT_JOSM_PATH);

            /* proof if the josm binaries are available */
            File josmFile = new File(josmPath);
            if (!josmFile.isFile() || !josmFile.canExecute()
                    || !josmFile.canRead()) {
                Application
                        .handleException(new ControlledException(
                                this,
                                ExceptionSeverity.WARNING,
                                I18N.getInstance()
                                        .getString(
                                                "Controller.Actions.PreviewPipeline.JOSMNotFound")));
            }

            /* the working directory */
            final String workingDirectory = (String) ModelProxy.getInstance()
                    .getSettings()
                    .getValue(SettingType.DEFAULT_WORKING_DIRECTORY);

            final List<String> toBeLoadedFilesByJosm = new ArrayList<String>();
            for (AbstractFunction function : ModelProxy.getInstance()
                    .getPipeline().getFunctions()) {
                if (function.getActiveTask().getName().toLowerCase()
                        .equals("write-xml")) {
                    for (AbstractParameter param : function.getActiveTask()
                            .getParameters()) {
                        if (param.getName().toLowerCase().equals("file")) {
                            toBeLoadedFilesByJosm.add(param.getValue());
                        }
                    }

                }
            }

            List<String> cmdLine = new ArrayList<String>(
                    toBeLoadedFilesByJosm.size() + 5);
            String javaPath = System.getProperty("java.home") + "/bin/java";
            cmdLine.add(javaPath);
            cmdLine.add(String.format("-Xmx%sm", Constants.JOSM_HEAP_SIZE));
            cmdLine.add("-jar");
            cmdLine.add(josmPath);
            cmdLine.addAll(toBeLoadedFilesByJosm);

            ProcessBuilder processBuilder = new ProcessBuilder(cmdLine);

            try {
                processBuilder.directory(new File(workingDirectory))
                        .redirectErrorStream(false).start();
            } catch (IOException e1) {
                Application
                        .handleException(new ControlledException(
                                this,
                                ExceptionSeverity.WARNING,
                                e1,
                                I18N.getInstance()
                                        .getString(
                                                "Controller.Actions.PreviewPipelineAction.IOException")));
            }
        }
    }
}
