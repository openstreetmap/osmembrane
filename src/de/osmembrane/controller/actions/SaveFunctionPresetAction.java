package de.osmembrane.controller.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import de.osmembrane.controller.events.ContainingLocationEvent;

/**
 * Action to save properties as a preset for a specific function. Gets a
 * {@link ContainingLocationEvent} with the function to be saved.
 * 
 * @author tobias_kuhn
 * 
 */
public class SaveFunctionPresetAction extends AbstractAction {

	private static final long serialVersionUID = 6264271045174747984L;

	/**
	 * Creates a new {@link SaveFunctionPresetAction}
	 */
	public SaveFunctionPresetAction() {
		// TODO some saving icon ?
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO do some fancy model stuff
	}
}