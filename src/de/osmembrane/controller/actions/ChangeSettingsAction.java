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
import de.osmembrane.exceptions.ControlledException;
import de.osmembrane.exceptions.ExceptionSeverity;
import de.osmembrane.model.ModelProxy;
import de.osmembrane.model.settings.AbstractSettings;
import de.osmembrane.model.settings.SettingType;
import de.osmembrane.model.settings.UnparsableFormatException;
import de.osmembrane.resources.Resource;
import de.osmembrane.tools.I18N;
import de.osmembrane.tools.IconLoader.Size;
import de.osmembrane.view.ViewRegistry;
import de.osmembrane.view.dialogs.SettingsDialog;
import de.osmembrane.view.interfaces.ISettingsDialog;

/**
 * Action to open the change settings dialog.
 * 
 * @author tobias_kuhn
 * 
 */
public class ChangeSettingsAction extends AbstractAction {

    private static final long serialVersionUID = -1491395674816531738L;

    /**
     * Creates a new {@link ChangeSettingsAction}
     */
    public ChangeSettingsAction() {
        putValue(
                Action.NAME,
                I18N.getInstance().getString(
                        "Controller.Actions.ChangeSettings.Name"));
        putValue(
                Action.SHORT_DESCRIPTION,
                I18N.getInstance().getString(
                        "Controller.Actions.ChangeSettings.Description"));
        putValue(Action.SMALL_ICON,
                Resource.PROGRAM_ICON.getImageIcon("settings.png", Size.SMALL));
        putValue(Action.LARGE_ICON_KEY,
                Resource.PROGRAM_ICON.getImageIcon("settings.png", Size.NORMAL));
        putValue(Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke(KeyEvent.VK_F8, 0));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ISettingsDialog dialog = ViewRegistry.getInstance().getCasted(
                SettingsDialog.class, ISettingsDialog.class);

        // set the display components
        AbstractSettings settings = ModelProxy.getInstance().getSettings();

        dialog.setLocales(settings.getLanguages());

        for (SettingType setting : SettingType.values()) {
            dialog.setValue(setting, settings.getValue(setting));
        }

        dialog.showWindow();

        if (dialog.shallApplyChanges()) {
            // set the model values, if necessary
            for (SettingType setting : SettingType.values()) {
                Object newValue = dialog.getValue(setting);

                if ((newValue != null)
                        && (!newValue.equals(settings.getValue(setting)))) {

                    try {
                        settings.setValue(setting, newValue);
                    } catch (UnparsableFormatException e1) {
                        Application
                                .handleException(new ControlledException(
                                        this,
                                        ExceptionSeverity.WARNING,
                                        I18N.getInstance()
                                                .getString(
                                                        "Controller.ChangeSettings.UnparsableFormatException",
                                                        I18N.getInstance()
                                                                .getString(
                                                                        "Model.Settings.Type."
                                                                                + e1.getType()),
                                                        I18N.getInstance()
                                                                .getString(
                                                                        "Model.Settings.Format."
                                                                                + e1.getType()
                                                                                        .getDeclaringClass()
                                                                                        .getSimpleName()))));
                    }
                }
            }
        } /* if apply changes */
    }
}
