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

package de.osmembrane.view.interfaces;

import de.osmembrane.model.settings.AbstractFunctionPreset;
import de.osmembrane.model.settings.FunctionPreset;
import de.osmembrane.view.dialogs.FunctionPresetDialog;

/**
 * The interface for {@link FunctionPresetDialog}.
 * 
 * @author tobias_kuhn
 * 
 */
public interface IFunctionPresetDialog extends IView {

    /**
     * Opens the dialog for a specific set of presets.
     * 
     * @param presets
     *            The presets to be able to choose from. May be empty.
     */
    void open(AbstractFunctionPreset[] presets);

    /**
     * @return the {@link FunctionPreset} that was selected, or null if none was
     *         selected
     */
    AbstractFunctionPreset getSelectedPreset();

    /**
     * @return whether the Load button was clicked
     */
    boolean loadSelected();

    /**
     * @return whether the Delete button was clicked
     */
    boolean deleteSelected();
}
