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

/**
 * Action to store a OSMembrane pipeline in file.
 * 
 * @author tobias_kuhn
 * 
 */
public class SaveAsPipelineAction extends AbstractAction {

    private static final long serialVersionUID = 5036259208332239931L;

    /**
     * Creates a new {@link SaveAsPipelineAction}
     */
    public SaveAsPipelineAction() {
        putValue(
                Action.NAME,
                I18N.getInstance().getString(
                        "Controller.Actions.SaveAsPipeline.Name"));
        putValue(
                Action.SHORT_DESCRIPTION,
                I18N.getInstance().getString(
                        "Controller.Actions.SaveAsPipeline.Description"));
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

        File startDir = new File((String) ModelProxy.getInstance()
                .getSettings()
                .getValue((SettingType.DEFAULT_WORKING_DIRECTORY)));
        JFileChooser fileChooser = new JFileChooser(startDir);
        fileChooser.setFileFilter(FileType.OSMEMBRANE.getFileFilter());

        int result = fileChooser.showSaveDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {

            /* check if the .osmembrane extension is missing */
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            if (!filePath.toLowerCase().endsWith(
                    FileType.OSMEMBRANE.getExtension().toLowerCase())) {
                filePath = filePath + FileType.OSMEMBRANE.getExtension();
            }

            /* Check if the file does not already exists. */
            if (new File(filePath).isFile()) {
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

            /* parse the file to an URL */
            URL file;
            try {
                file = new File(filePath).toURI().toURL();
            } catch (MalformedURLException e1) {
                file = null;
            }

            try {
                ModelProxy.getInstance().getPipeline().savePipeline(file);
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
