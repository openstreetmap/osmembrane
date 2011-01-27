package de.osmembrane.controller.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import de.osmembrane.resources.Resource;
import de.osmembrane.tools.IconLoader;
import de.osmembrane.tools.IconLoader.Size;

/**
 * Action to ... TODO what does this thing do?
 * 
 * @author tobias_kuhn
 * 
 */
public class ShowHelpAction extends AbstractAction {

	private static final long serialVersionUID = 1015846096381941393L;

	/**
	 * Creates a new {@link ShowHelpAction}
	 */
	public ShowHelpAction() {
		putValue(Action.NAME, "Show Help");
		putValue(Action.SMALL_ICON, Resource.PROGRAM_ICON.getImageIcon("help.png", Size.SMALL));
		putValue(Action.LARGE_ICON_KEY, Resource.PROGRAM_ICON.getImageIcon("help.png", Size.NORMAL));
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F1,
				0));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO implement
		throw new UnsupportedOperationException();
	}
}