package de.osmembrane.controller.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import de.osmembrane.resources.Resource;
import de.osmembrane.tools.IconLoader.Size;
import de.osmembrane.view.ViewRegistry;
import de.osmembrane.view.frames.IMainFrame;
import de.osmembrane.view.frames.MainFrame;

/**
 * Action to display the *entire* pipeline.
 * 
 * @author tobias_kuhn
 *
 */
public class ViewAllAction extends AbstractAction {
	
	private static final long serialVersionUID = 3116936141334903589L;

	/**
	 * Creates a new {@link ViewAllAction}
	 */
	public ViewAllAction() {
		putValue(Action.NAME, "View All");
		putValue(Action.SMALL_ICON, Resource.PROGRAM_ICON.getImageIcon("zoom_fit.png", Size.SMALL));
		putValue(Action.LARGE_ICON_KEY, Resource.PROGRAM_ICON.getImageIcon("zoom_fit.png", Size.NORMAL));
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_END,
				0));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		IMainFrame mainFrame = ViewRegistry.getInstance().getCasted(MainFrame.class, IMainFrame.class);
		mainFrame.getZoomDevice().showEntireView();
	}

}
