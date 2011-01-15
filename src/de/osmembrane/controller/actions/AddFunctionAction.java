package de.osmembrane.controller.actions;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.geom.Point2D;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;

import de.osmembrane.Application;
import de.osmembrane.controller.events.ContainingEvent;
import de.osmembrane.controller.events.ContainingLocationEvent;
import de.osmembrane.exceptions.ControlledException;
import de.osmembrane.exceptions.ExceptionSeverity;
import de.osmembrane.model.ModelProxy;
import de.osmembrane.model.pipeline.AbstractFunction;
import de.osmembrane.tools.I18N;
import de.osmembrane.view.ViewRegistry;
import de.osmembrane.view.panels.LibraryFunction;

public class AddFunctionAction extends AbstractAction {

	public AddFunctionAction() {
		putValue(Action.NAME, "Add Function");
		// throw new UnsupportedOperationException();
		// FIXME
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ContainingLocationEvent cle = (ContainingLocationEvent) e;
		if (cle.getContained() instanceof AbstractFunction) {

			// add the function
			AbstractFunction prototype = (AbstractFunction) cle.getContained();
			AbstractFunction newFunc = ModelProxy.getInstance()
					.accessFunctions().getFunction(prototype);

			newFunc.setCoordinate(cle.getLocation());
			ModelProxy.getInstance().accessPipeline().addFunction(newFunc);
		} else {
			Application.handleException(new ControlledException(this,
					ExceptionSeverity.UNEXPECTED_BEHAVIOR, I18N.getInstance()
							.getString("Controller.Actions.InvalidEvent")));
		}
	}
}