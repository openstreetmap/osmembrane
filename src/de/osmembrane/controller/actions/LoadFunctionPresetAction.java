package de.osmembrane.controller.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import de.osmembrane.controller.events.ContainingLocationEvent;

/**
 * Action to load saved presets for a specific function. Gets a
 * {@link ContainingLocationEvent} with the function to be edited.
 * 
 * @author tobias_kuhn
 * 
 */
public class LoadFunctionPresetAction extends AbstractAction {

	private static final long serialVersionUID = 6264271045174747984L;

	/**
	 * Creates a new {@link LoadFunctionPresetAction}
	 */
	public LoadFunctionPresetAction() {
		// TODO some loading icon ?
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO do some fancy model stuff
	}
}