package de.osmembrane.controller;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

public class UndoAction extends AbstractAction {

	public UndoAction() {
		putValue(Action.NAME, "Undo");
		//throw new UnsupportedOperationException();
		// FIXME
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		throw new UnsupportedOperationException();
	}
}