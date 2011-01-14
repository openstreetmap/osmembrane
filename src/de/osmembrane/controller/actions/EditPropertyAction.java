package de.osmembrane.controller.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import de.osmembrane.model.pipeline.AbstractFunction;
import de.osmembrane.tools.I18N;
import de.osmembrane.view.ExceptionType;
import de.osmembrane.view.ViewRegistry;

public class EditPropertyAction extends AbstractAction {

	public EditPropertyAction() {
		putValue(Action.NAME, "Edit Property");
		//throw new UnsupportedOperationException();
		// FIXME
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// find the AbstractFunction to edit
		/*if (!(e.getSource() instanceof AbstractFunction)) {
			ViewRegistry.showException(this.getClass(), ExceptionType.ABNORMAL_BEHAVIOR, new Exception(I18N.getInstance().getString("Controller.EditProperty.InvalidSource")));
		}		
		AbstractFunction editing = (AbstractFunction) e.getSource();
		
		// check the parameter id to edit
		if (e.getID() >= editing.getActiveTask().getParameters().length) {
			ViewRegistry.showException(this.getClass(), ExceptionType.ABNORMAL_BEHAVIOR, new Exception(I18N.getInstance().getString("Controller.EditProperty.InvalidID")));
		}
		
		// perform
		//editing.getActiveTask().getParameters().get(e.getID()).setValue(e.getActionCommand());		
		editing.changedNotifyObservers();
		*/
		// BROKEN
	}
}