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
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;

import de.osmembrane.controller.events.ContainingEvent;
import de.osmembrane.model.ModelProxy;
import de.osmembrane.model.pipeline.AbstractParameter;
import de.osmembrane.model.settings.SettingType;

/**
 * Action to edit a parameter which is a directory path and therefore open the
 * directory path dialog. Receives a {@link ContainingEvent}.
 * 
 * @author tobias_kuhn
 * 
 */
public class EditDirectoryPropertyAction extends AbstractAction {

    private static final long serialVersionUID = -1358550319488735885L;

    /**
     * Creates a new {@link EditDirectoryPropertyAction}
     */
    public EditDirectoryPropertyAction() {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ContainingEvent ce = (ContainingEvent) e;
        AbstractParameter ap = (AbstractParameter) ce.getContained();

        File startDir = new File((String) ModelProxy.getInstance()
                .getSettings()
                .getValue((SettingType.DEFAULT_WORKING_DIRECTORY)));

        JFileChooser fileChooser = new JFileChooser(startDir);
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);

        String value = ap.getValue();
        if ((value == null) || (value.isEmpty())) {
            value = ".";
        }
        fileChooser.setCurrentDirectory(new File(value));

        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            ap.setValue(fileChooser.getCurrentDirectory().getAbsolutePath());
        }
    }
}
