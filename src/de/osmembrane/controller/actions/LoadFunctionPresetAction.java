package de.osmembrane.controller.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import de.osmembrane.controller.events.ContainingLocationEvent;
import de.osmembrane.resources.Resource;
import de.osmembrane.tools.I18N;
import de.osmembrane.tools.IconLoader.Size;

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
		putValue(
				Action.NAME,
				I18N.getInstance().getString(
						"Controller.Actions.LoadFunctionPreset.Name"));
		putValue(
				Action.SHORT_DESCRIPTION,
				I18N.getInstance().getString(
						"Controller.Actions.LoadFunctionPreset.Description"));
		putValue(Action.SMALL_ICON, Resource.PROGRAM_ICON.getImageIcon(
				"load_pipeline.png", Size.SMALL));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO do some fancy model stuff
		throw new UnsupportedOperationException();
	}
}