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
import de.osmembrane.model.ModelProxy;
import de.osmembrane.model.pipeline.AbstractFunction;
import de.osmembrane.model.pipeline.CopyType;
import de.osmembrane.tools.I18N;

/**
 * Action to add a function to the pipeline. Receives a
 * {@link ContainingLocationEvent}. Only invoked from the view, should never be
 * visible.
 * 
 * @author tobias_kuhn
 * 
 */
public class AddFunctionAction extends AbstractAction {

    private static final long serialVersionUID = 6264271045174747984L;

    /**
     * Creates a new {@link AddFunctionAction}
     */
    public AddFunctionAction() {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ContainingLocationEvent cle = (ContainingLocationEvent) e;
        if (cle.getContained() instanceof AbstractFunction) {

            // get the copy from the prototypes
            AbstractFunction prototype = (AbstractFunction) cle.getContained();
            AbstractFunction newFunc = prototype
                    .copy(CopyType.WITHOUT_VALUES_AND_POSITION);

            // add the function at the location
            newFunc.setCoordinate(cle.getLocation());
            ModelProxy.getInstance().getPipeline().addFunction(newFunc);
        } else {
            Application.handleException(new ControlledException(this,
                    ExceptionSeverity.UNEXPECTED_BEHAVIOR, I18N.getInstance()
                            .getString("Controller.Actions.InvalidEvent")));
        }
    }
}
