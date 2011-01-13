package de.osmembrane.controller;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import de.osmembrane.view.ViewRegistry;

public class LoadPipelineAction extends AbstractAction {

	public LoadPipelineAction() {
		putValue(Action.NAME, "Load Pipeline");
		//throw new UnsupportedOperationException();
		// FIXME
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ViewRegistry.showException(null, null, null);
		throw new UnsupportedOperationException();
	}
}