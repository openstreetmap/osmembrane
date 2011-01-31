package de.osmembrane.controller.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import de.osmembrane.controller.events.ContainingLocationEvent;
import de.osmembrane.tools.I18N;

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
		putValue(
				Action.NAME,
				I18N.getInstance().getString(
						"Controller.Actions.SaveFunctionPreset.Name"));
		putValue(
				Action.SHORT_DESCRIPTION,
				I18N.getInstance().getString(
						"Controller.Actions.SaveFunctionPreset.Description"));
		// TODO some saving icon ?
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO do some fancy model stuff
	}
}