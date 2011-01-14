package de.osmembrane.view.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import de.osmembrane.view.IView;
import de.osmembrane.view.ViewRegistry;
import de.osmembrane.view.frames.MainFrame;

public class ZoomOutAction extends AbstractAction {
	
	public ZoomOutAction() {
		// TODO Auto-generated constructor stub
		putValue(Action.NAME, "Zoom Out");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		IView mainFrame = ViewRegistry.getInstance().getMainFrame();
		MainFrame mf = (MainFrame) mainFrame;
		mf.getPipeline().zoomOut();
	}

}
