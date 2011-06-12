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

import de.osmembrane.model.pipeline.AbstractParameter;
import de.osmembrane.view.dialogs.ListDialog;

/**
 * Interface for {@link ListDialog}.
 * 
 * @author tobias_kuhn
 * 
 */
public interface IListDialog extends IView {

    /**
     * Opens the dialog for a specific abstract parameter
     * 
     * @param list
     *            the parameter of getType() == List
     */
    public void open(AbstractParameter list);

    /**
     * @return whether or not the changes made in the dialog should be applied
     *         to the model
     */
    public boolean shallApplyChanges();

    /**
     * @return the string to set the parameter to, if all changes should be
     *         applied
     */
    public String getEdits();

}
