package de.osmembrane.view.actions;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;

import de.osmembrane.resources.Constants;
import de.osmembrane.tools.IconLoader;
import de.osmembrane.tools.IconLoader.Size;
import de.osmembrane.view.IView;
import de.osmembrane.view.ViewRegistry;
import de.osmembrane.view.frames.MainFrame;

public class StandardViewAction extends AbstractAction {
	
	public StandardViewAction() {
		// TODO Auto-generated constructor stub
		putValue(Action.NAME, "Reset View");
		putValue(Action.SMALL_ICON, new IconLoader("zoom_reset.png",
				Size.SMALL).get());
		putValue(Action.LARGE_ICON_KEY, new IconLoader("zoom_reset.png",
				Size.NORMAL).get());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		IView mainFrame = ViewRegistry.getInstance().getMainFrame();
		MainFrame mf = (MainFrame) mainFrame;
		mf.getPipeline().resetView();
	}

}
