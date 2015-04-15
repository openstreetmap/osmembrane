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

import javax.swing.AbstractAction;
import javax.swing.Action;

import de.osmembrane.model.ModelProxy;
import de.osmembrane.model.pipeline.AbstractFunction;
import de.osmembrane.model.settings.AbstractFunctionPreset;
import de.osmembrane.model.settings.AbstractSettings;
import de.osmembrane.resources.Resource;
import de.osmembrane.tools.I18N;
import de.osmembrane.tools.IconLoader.Size;
import de.osmembrane.view.ViewRegistry;
import de.osmembrane.view.dialogs.FunctionPresetDialog;
import de.osmembrane.view.frames.MainFrame;
import de.osmembrane.view.interfaces.IFunctionPresetDialog;
import de.osmembrane.view.interfaces.IMainFrame;
import de.osmembrane.view.panels.PipelineFunction;

/**
 * Action to load saved presets for a specific function.
 * 
 * @author tobias_kuhn
 * 
 */
public class LoadFunctionPresetAction extends AbstractAction {

    private static final long serialVersionUID = 6264271045174747984L;

    /**
     * Creates a new {@link LoadFunctionPresetAction}
     */
    public LoadFunctionPresetAction() {
        putValue(
                Action.NAME,
                I18N.getInstance().getString(
                        "Controller.Actions.LoadFunctionPreset.Name"));
        putValue(
                Action.SHORT_DESCRIPTION,
                I18N.getInstance().getString(
                        "Controller.Actions.LoadFunctionPreset.Description"));
        putValue(Action.SMALL_ICON, Resource.PROGRAM_ICON.getImageIcon(
                "load_pipeline.png", Size.SMALL));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        IMainFrame mainFrame = ViewRegistry.getInstance().getCasted(
                MainFrame.class, IMainFrame.class);
        Object select = mainFrame.getSelected();
        if ((select == null) || !(select instanceof PipelineFunction)) {
            return;
        }
        AbstractFunction function = ((PipelineFunction) select)
                .getModelFunction();

        AbstractSettings settings = ModelProxy.getInstance().getSettings();

        IFunctionPresetDialog fpd = ViewRegistry.getInstance().getCasted(
                FunctionPresetDialog.class, IFunctionPresetDialog.class);

        do {
            AbstractFunctionPreset afp[] = settings
                    .getAllFunctionPresets(function);

            fpd.open(afp);

            AbstractFunctionPreset preset = fpd.getSelectedPreset();
            if (fpd.loadSelected()) {
                preset.loadPreset(function);
            } else if (fpd.deleteSelected()) {
                settings.deleteFunctionPreset(preset);
            }
        } while (fpd.deleteSelected());
    }
}
