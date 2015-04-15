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
 * Action to edit a parameter which is a file path and therefore open the file
 * path dialog. Receives a {@link ContainingEvent}.
 * 
 * @author tobias_kuhn
 * 
 */
public class EditFilePropertyAction extends AbstractAction {

    private static final long serialVersionUID = 1481319711002406388L;

    /**
     * Creates a new {@link EditFilePropertyAction}
     */
    public EditFilePropertyAction() {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ContainingEvent ce = (ContainingEvent) e;
        AbstractParameter ap = (AbstractParameter) ce.getContained();

        String wd = (String) ModelProxy.getInstance().getSettings()
                .getValue(SettingType.DEFAULT_WORKING_DIRECTORY);
        if (wd.length() == 0) {
            wd = null;
        }

        JFileChooser fileChooser = new JFileChooser(wd);

        String value = ap.getValue();
        if ((value == null) || (value.isEmpty())) {
            value = ".";
        }
        fileChooser.setSelectedFile(new File(value));

        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            String path = fileChooser.getSelectedFile().getAbsolutePath();
            if (wd != null) {
                path = new File(wd).toURI().relativize(new File(path).toURI())
                        .getPath();
            }
            ap.setValue(path);
        }
    }
}
