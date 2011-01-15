package de.osmembrane.controller.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import de.osmembrane.exceptions.ExceptionSeverity;
import de.osmembrane.model.pipeline.AbstractFunction;
import de.osmembrane.tools.I18N;
import de.osmembrane.view.ViewRegistry;

public class EditPropertyAction extends AbstractAction {

	public EditPropertyAction() {
		putValue(Action.NAME, "Edit Property");
		//throw new UnsupportedOperationException();
		// FIXME
	}

	@Override
	public void actionPerformed(ActionEvent e) {

	}
}