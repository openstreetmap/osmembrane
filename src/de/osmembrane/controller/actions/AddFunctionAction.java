package de.osmembrane.controller.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import de.osmembrane.controller.events.ContainingEvent;
import de.osmembrane.model.ModelProxy;
import de.osmembrane.model.pipeline.AbstractFunction;

public class AddFunctionAction extends AbstractAction {

	public AddFunctionAction() {
		putValue(Action.NAME, "Add Function");
		// throw new UnsupportedOperationException();
		// FIXME
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ContainingEvent ce = (ContainingEvent) e;
		if (ce.getContainedClass() == AbstractFunction.class) {

			// add the function
			AbstractFunction prototype = (AbstractFunction) ce.getContained();
			AbstractFunction newFunc = ModelProxy.getInstance()
					.accessFunctions().getFunction(prototype);
			ModelProxy.getInstance().accessPipeline().addFunction(newFunc);
		}
	}
}