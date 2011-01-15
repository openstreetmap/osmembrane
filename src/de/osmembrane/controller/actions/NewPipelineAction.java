package de.osmembrane.controller.actions;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import de.osmembrane.controller.exceptions.ExceptionSeverity;
import de.osmembrane.view.ViewRegistry;

public class NewPipelineAction extends AbstractAction {

	public NewPipelineAction() {
		//throw new UnsupportedOperationException();
		// FIXME
		putValue(Action.NAME, "New Pipeline");
		//putValue(Action.LARGE_ICON_KEY, new ImageIcon(getClass().getResource("/res/Add.png")));
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_INSERT,
					Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		putValue(Action.SHORT_DESCRIPTION, "News an add pipeline item.");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ViewRegistry.showException(this.getClass(), ExceptionSeverity.UNEXPECTED_BEHAVIOR, new UnsupportedOperationException());
		throw new UnsupportedOperationException();
	}
}