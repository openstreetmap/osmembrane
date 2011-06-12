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
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import de.osmembrane.Application;
import de.osmembrane.exceptions.ControlledException;
import de.osmembrane.exceptions.ExceptionSeverity;
import de.osmembrane.model.ModelProxy;
import de.osmembrane.model.persistence.FileException;
import de.osmembrane.model.persistence.FileType;
import de.osmembrane.model.settings.SettingType;
import de.osmembrane.resources.Resource;
import de.osmembrane.tools.HeadlessSafe;
import de.osmembrane.tools.I18N;
import de.osmembrane.tools.IconLoader.Size;
import de.osmembrane.tools.Tools;

/**
 * Action to export the created pipeline to a file.
 * 
 * @author tobias_kuhn
 * 
 */
public class ExportPipelineAction extends AbstractAction {

    private static final long serialVersionUID = 8382050986007810817L;

    /**
     * Creates a new {@link ExportPipelineAction}
     */
    public ExportPipelineAction() {
        putValue(
                Action.NAME,
                I18N.getInstance().getString(
                        "Controller.Actions.ExportPipeline.Name"));
        putValue(
                Action.SHORT_DESCRIPTION,
                I18N.getInstance().getString(
                        "Controller.Actions.ExportPipeline.Description"));
        putValue(Action.SMALL_ICON, Resource.PROGRAM_ICON.getImageIcon(
                "export_pipeline.png", Size.SMALL));
        putValue(Action.LARGE_ICON_KEY, Resource.PROGRAM_ICON.getImageIcon(
                "export_pipeline.png", Size.NORMAL));
        putValue(
                Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke(KeyEvent.VK_E,
                        HeadlessSafe.getMenuShortcutKeyMask()));
    }

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

        File startDir = new File((String) ModelProxy.getInstance()
                .getSettings()
                .getValue((SettingType.DEFAULT_WORKING_DIRECTORY)));
        JFileChooser fileChooser = new JFileChooser(startDir);
        fileChooser.setFileFilter(FileType.OSMEMBRANE.getFileFilter());
        fileChooser.addChoosableFileFilter(FileType.BASH.getFileFilter());
        fileChooser.addChoosableFileFilter(FileType.CMD.getFileFilter());
        fileChooser.addChoosableFileFilter(FileType.ALLTYPES.getFileFilter());

        int result = fileChooser.showSaveDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {

            try {
                /* parse the file to an URL */
                URL file = fileChooser.getSelectedFile().toURI().toURL();
                FileType type = FileType.fileTypeFor(fileChooser
                        .getSelectedFile());

                if (type == null) {
                    /*
                     * could not find out which type the file has, add the
                     * system dependent extension.
                     */
                    String fileWithExplicitExtensionString = fileChooser
                            .getSelectedFile().getAbsolutePath();

                    /* check if it is windows */
                    if (System.getProperty("os.name").toLowerCase()
                            .contains("win")) {
                        fileWithExplicitExtensionString += FileType.CMD
                                .getExtension();
                        type = FileType.CMD;
                    } else {
                        /* should be a unix based os, use bash */
                        fileWithExplicitExtensionString += FileType.BASH
                                .getExtension();
                        type = FileType.BASH;
                    }

                    file = new File(fileWithExplicitExtensionString).toURI()
                            .toURL();
                }

                /* Check if the file does not already exists. */
                if (Tools.urlToFile(file).isFile()) {
                    int confirmResult = JOptionPane.showConfirmDialog(
                            null,
                            I18N.getInstance().getString(
                                    "Controller.Actions.File.Override"),
                            I18N.getInstance().getString(
                                    "Controller.Actions.File.Override.Title"),
                            JOptionPane.YES_NO_OPTION);
                    if (confirmResult == JOptionPane.NO_OPTION
                            || result == JOptionPane.CLOSED_OPTION) {
                        return;
                    }
                }

                ModelProxy.getInstance().getPipeline()
                        .exportPipeline(file, type);
            } catch (FileException e1) {
                String message = I18N.getInstance().getString(
                        "Controller.Actions.Save.Failed." + e1.getType(),
                        e1.getParentException().getMessage());

                Application.handleException(new ControlledException(this,
                        ExceptionSeverity.WARNING, e1, message));
            } catch (MalformedURLException e1) {
            }
        }
    }
}
