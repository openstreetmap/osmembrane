package de.osmembrane.view.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import de.osmembrane.resources.Resource;
import de.osmembrane.tools.IconLoader;
import de.osmembrane.tools.IconLoader.Size;
import de.osmembrane.view.IView;
import de.osmembrane.view.ViewRegistry;
import de.osmembrane.view.frames.MainFrame;

/**
 * Action to nonchalantly zoom out.
 * 
 * @author tobias_kuhn
 * 
 */
public class ZoomOutAction extends AbstractAction {

	private static final long serialVersionUID = 6291772549719149526L;

	/**
	 * Creates a new {@link ZoomOutAction}
	 */
	public ZoomOutAction() {
		putValue(Action.NAME, "Zoom Out");
		putValue(Action.SMALL_ICON, Resource.PROGRAM_ICON.getImageIcon("zoom_out.png", Size.SMALL));
		putValue(Action.LARGE_ICON_KEY, Resource.PROGRAM_ICON.getImageIcon("zoom_out.png", Size.NORMAL));
		putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, 0));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		MainFrame mainFrame = ViewRegistry.getInstance().getMainFrameByPass();
		mainFrame.getPipeline().zoomOut();
	}

}
