package de.osmembrane.controller.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import de.osmembrane.Application;
import de.osmembrane.controller.events.ContainingLocationEvent;
import de.osmembrane.exceptions.ControlledException;
import de.osmembrane.exceptions.ExceptionSeverity;
import de.osmembrane.model.ModelProxy;
import de.osmembrane.model.pipeline.AbstractFunction;
import de.osmembrane.tools.I18N;

public class MoveFunctionAction extends AbstractAction {

	public MoveFunctionAction() {
		putValue(Action.NAME, "Move Function");
		//throw new UnsupportedOperationException();
		// FIXME
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