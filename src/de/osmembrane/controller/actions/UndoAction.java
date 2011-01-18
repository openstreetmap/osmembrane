package de.osmembrane.controller.actions;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;

import de.osmembrane.resources.Constants;
import de.osmembrane.tools.IconLoader;
import de.osmembrane.tools.IconLoader.Size;

public class UndoAction extends AbstractAction {

	public UndoAction() {
		putValue(Action.NAME, "Undo");
		putValue(Action.SMALL_ICON, new IconLoader("undo.png",
				Size.SMALL).get());
		putValue(Action.LARGE_ICON_KEY, new IconLoader("undo.png",
				Size.NORMAL).get());
		// FIXME
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		throw new UnsupportedOperationException();
	}
}