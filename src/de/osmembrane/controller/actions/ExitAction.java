package de.osmembrane.controller.actions;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import de.osmembrane.resources.Resource;
import de.osmembrane.tools.IconLoader;
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
		putValue(Action.NAME, "Exit");
		putValue(Action.SMALL_ICON, Resource.PROGRAM_ICON.getImageIcon("exit.png", Size.SMALL));
		putValue(Action.LARGE_ICON_KEY, Resource.PROGRAM_ICON.getImageIcon("exit.png", Size.NORMAL));
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Q,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		putValue(Action.SHORT_DESCRIPTION, "Shut up");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO implement
		System.exit(0);
	}
}