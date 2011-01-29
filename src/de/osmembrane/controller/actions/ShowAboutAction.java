package de.osmembrane.controller.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import de.osmembrane.view.ViewRegistry;
import de.osmembrane.view.dialogs.AboutDialog;

/**
 * Action to show the about dialog
 * 
 * @author tobias_kuhn
 * 
 */
public class ShowAboutAction extends AbstractAction {

	private static final long serialVersionUID = 1015846096381941393L;

	/**
	 * Creates a new {@link ShowAboutAction}
	 */
	public ShowAboutAction() {
		putValue(Action.NAME, "About");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ViewRegistry.getInstance().get(AboutDialog.class).showWindow();
	}
}