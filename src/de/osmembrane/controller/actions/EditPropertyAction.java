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

import de.osmembrane.controller.events.ContainingFunctionChangeParameterEvent;
import de.osmembrane.model.pipeline.AbstractFunction;

/**
 * Action to edit a specific parameter or task of a function. Receives a
 * {@link ContainingFunctionChangeParameterEvent}. Only invoked from the view,
 * should never be visible.
 * 
 * @author tobias_kuhn
 * 
 */
public class EditPropertyAction extends AbstractAction {

    private static final long serialVersionUID = 320672706619084111L;

    /**
     * Creates a new {@link EditPropertyAction}
     */
    public EditPropertyAction() {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ContainingFunctionChangeParameterEvent cfcpe = (ContainingFunctionChangeParameterEvent) e;
        AbstractFunction af = (AbstractFunction) cfcpe.getContained();

        // set new parameter
        if (cfcpe.wasNewParameterSet()) {
            cfcpe.getChangedParameter().setValue(cfcpe.getNewParameterValue());
        }

        // set new task
        if (cfcpe.wasNewTaskSet()) {
            af.setActiveTask(cfcpe.getNewTask());
        }
    }
}
