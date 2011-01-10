package de.osmembrane.controller;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

public class ExitAction extends AbstractAction {

	public ExitAction() {
		//throw new UnsupportedOperationException();
		// FIXME
		putValue(Action.NAME, "Exitdamnyou");
		//putValue(Action.LARGE_ICON_KEY, new ImageIcon(getClass().getResource("/res/Add.png")));
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_AT,
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