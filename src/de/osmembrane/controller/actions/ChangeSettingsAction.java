package de.osmembrane.controller.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import de.osmembrane.resources.Resource;
import de.osmembrane.tools.IconLoader.Size;

/**
 * Action to open the change settings dialog.
 * 
 * @author tobias_kuhn
 * 
 */
public class ChangeSettingsAction extends AbstractAction {

	private static final long serialVersionUID = -1491395674816531738L;

	/**
	 * Creates a new {@link ChangeSettingsAction}
	 */
	public ChangeSettingsAction() {
		putValue(Action.NAME, "Change Settings");
		putValue(Action.SMALL_ICON, Resource.PROGRAM_ICON.getImageIcon("settings.png", Size.SMALL));
		putValue(Action.LARGE_ICON_KEY, Resource.PROGRAM_ICON.getImageIcon("settings.png", Size.NORMAL));
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F8,
				0));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		throw new UnsupportedOperationException();
		// TODO implement
	}
}