package de.osmembrane.view.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import de.osmembrane.tools.IconLoader;
import de.osmembrane.tools.IconLoader.Size;
import de.osmembrane.view.IView;
import de.osmembrane.view.ViewRegistry;
import de.osmembrane.view.frames.MainFrame;

/**
 * Action to nonchalantly zoom in.
 * 
 * @author tobias_kuhn
 *
 */
public class ZoomInAction extends AbstractAction {

	private static final long serialVersionUID = 5671603148720489921L;

	/**
	 * Creates a new {@link ZoomInAction}
	 */
	public ZoomInAction() {
		putValue(Action.NAME, "Zoom In");
		putValue(Action.SMALL_ICON, new IconLoader("zoom_in.png", Size.SMALL).get());
		putValue(Action.LARGE_ICON_KEY, new IconLoader("zoom_in.png",
				Size.NORMAL).get());
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_PLUS,
				0));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		IView mainFrame = ViewRegistry.getInstance().getMainFrame();
		MainFrame mf = (MainFrame) mainFrame;
		mf.getPipeline().zoomIn();
	}

}
