package de.osmembrane.view.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import de.osmembrane.view.IView;
import de.osmembrane.view.ViewRegistry;
import de.osmembrane.view.frames.MainFrame;

public class StandardViewAction extends AbstractAction {
	
	public StandardViewAction() {
		// TODO Auto-generated constructor stub
		putValue(Action.NAME, "Reset View");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		IView mainFrame = ViewRegistry.getInstance().getMainFrame();
		MainFrame mf = (MainFrame) mainFrame;
		mf.getPipeline().resetView();
	}

}
