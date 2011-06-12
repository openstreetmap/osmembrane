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

import de.osmembrane.Application;
import de.osmembrane.controller.events.ContainingLocationEvent;
import de.osmembrane.exceptions.ControlledException;
import de.osmembrane.exceptions.ExceptionSeverity;
import de.osmembrane.model.pipeline.AbstractFunction;
import de.osmembrane.tools.I18N;

/**
 * Action to move a function in the pipeline. Receives a
 * {@link ContainingLocationEvent}. Only invoked from the view, should never be
 * visible.
 * 
 * @author tobias_kuhn
 * 
 */
public class MoveFunctionAction extends AbstractAction {

    private static final long serialVersionUID = -8723478854428489285L;

    /**
     * Creates a new {@link MoveFunctionAction}
     */
    public MoveFunctionAction() {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ContainingLocationEvent cle = (ContainingLocationEvent) e;
        if (cle.getContained() instanceof AbstractFunction) {

            // set the position of the function
            AbstractFunction function = (AbstractFunction) cle.getContained();
            function.setCoordinate(cle.getLocation());
        } else {
            Application.handleException(new ControlledException(this,
                    ExceptionSeverity.UNEXPECTED_BEHAVIOR, I18N.getInstance()
                            .getString("Controller.Actions.InvalidEvent")));
        }
    }
}
