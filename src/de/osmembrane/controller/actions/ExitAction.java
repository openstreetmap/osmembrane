package de.osmembrane.controller.actions;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import de.osmembrane.resources.Constants;
import de.osmembrane.tools.IconLoader;
import de.osmembrane.tools.IconLoader.Size;

public class ExitAction extends AbstractAction {

	public ExitAction() {
		//throw new UnsupportedOperationException();
		// FIXME
		putValue(Action.NAME, "Exit");
		putValue(Action.SMALL_ICON, new IconLoader("exit.png",
				Size.SMALL).get());
		putValue(Action.LARGE_ICON_KEY, new IconLoader("exit.png",
				Size.NORMAL).get());
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Q,
					Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		putValue(Action.SHORT_DESCRIPTION, "Shut up");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//throw new UnsupportedOperationException();
		// FIXME
		System.exit(0);
	}
}