package de.osmembrane.controller.actions;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;

import de.osmembrane.Application;
import de.osmembrane.controller.events.ConnectingFunctionsEvent;
import de.osmembrane.controller.events.ContainingLocationEvent;
import de.osmembrane.exceptions.ControlledException;
import de.osmembrane.exceptions.ExceptionSeverity;
import de.osmembrane.model.ModelProxy;
import de.osmembrane.model.pipeline.AbstractFunction;
import de.osmembrane.model.pipeline.ConnectorException;
import de.osmembrane.resources.Constants;
import de.osmembrane.tools.I18N;
import de.osmembrane.tools.IconLoader;
import de.osmembrane.tools.IconLoader.Size;

public class AddConnectionAction extends AbstractAction {

	public AddConnectionAction() {
		putValue(Action.NAME, "Add Connection");
		putValue(Action.SMALL_ICON, new IconLoader("add_connection.png",
				Size.SMALL).get());
		putValue(Action.LARGE_ICON_KEY, new IconLoader("add_connection.png",
				Size.NORMAL).get());
		// FIXME
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ConnectingFunctionsEvent cfe = (ConnectingFunctionsEvent) e;

		try {
			cfe.getConnectionSource().addConnectionTo(
					cfe.getConnectionDestination());
		} catch (ConnectorException e1) {
			Application.handleException(new ControlledException(this,
							ExceptionSeverity.WARNING, I18N.getInstance()
									.getString("Controller.Actions.AddConnection."
											+ e1.getType())));

		}
	}
}