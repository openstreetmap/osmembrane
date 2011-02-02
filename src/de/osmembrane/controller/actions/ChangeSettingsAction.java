package de.osmembrane.controller.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import de.osmembrane.resources.Resource;
import de.osmembrane.tools.I18N;
import de.osmembrane.tools.IconLoader.Size;
import de.osmembrane.view.ViewRegistry;
import de.osmembrane.view.dialogs.SettingsDialog;
import de.osmembrane.view.interfaces.ISettingsDialog;

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
		putValue(
				Action.NAME,
				I18N.getInstance().getString(
						"Controller.Actions.ChangeSettings.Name"));
		putValue(
				Action.SHORT_DESCRIPTION,
				I18N.getInstance().getString(
						"Controller.Actions.ChangeSettings.Description"));
		putValue(Action.SMALL_ICON,
				Resource.PROGRAM_ICON.getImageIcon("settings.png", Size.SMALL));
		putValue(Action.LARGE_ICON_KEY,
				Resource.PROGRAM_ICON.getImageIcon("settings.png", Size.NORMAL));
		putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_F8, 0));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ISettingsDialog settingsDialog = ViewRegistry.getInstance().getCasted(
				SettingsDialog.class, ISettingsDialog.class);
		
		settingsDialog.showWindow();
		
		if (settingsDialog.shallApplyChanges()) {
			
		}
	}
}