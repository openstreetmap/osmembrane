package de.osmembrane.controller.actions;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import de.osmembrane.controller.ActionRegistry;
import de.osmembrane.model.ModelProxy;
import de.osmembrane.resources.Resource;
import de.osmembrane.tools.I18N;
import de.osmembrane.tools.IconLoader.Size;

/**
 * Action to exit the program with all additional stuff that might be necessary
 * like asking for save, etc.
 * 
 * @author tobias_kuhn
 * 
 */
public class ExitAction extends AbstractAction {

	private static final long serialVersionUID = 8759673259846990468L;

	/**
	 * Creates a new {@link ExitAction}
	 */
	public ExitAction() {
		putValue(Action.NAME,
				I18N.getInstance().getString("Controller.Actions.Exit.Name"));
		putValue(
				Action.SHORT_DESCRIPTION,
				I18N.getInstance().getString(
						"Controller.Actions.Exit.Description"));
		putValue(Action.SMALL_ICON,
				Resource.PROGRAM_ICON.getImageIcon("exit.png", Size.SMALL));
		putValue(Action.LARGE_ICON_KEY,
				Resource.PROGRAM_ICON.getImageIcon("exit.png", Size.NORMAL));
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Q,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		/* Ask if the Pipeline should be saved, if that is not so. */
		if (!ModelProxy.getInstance().getPipeline().isSaved()) {
			int result = JOptionPane.showConfirmDialog(
					null,
					I18N.getInstance().getString(
							"Controller.Actions.NewPipeline.NotSaved"),
					I18N.getInstance().getString(
							"Controller.Actions.NewPipeline.NotSaved.Title"),
					JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			if (result == JOptionPane.NO_OPTION) {
				return;
			}
		}

		/*
		 * Remove the backup, 'cause otherwise startup will ask to load the
		 * backup everytime.
		 */
		ModelProxy.getInstance().getPipeline().clearBackup();

		System.exit(0);
	}
}