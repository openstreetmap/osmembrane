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
 * Action to return to the initial view.
 * 
 * @author tobias_kuhn
 *
 */
public class StandardViewAction extends AbstractAction {
	
	private static final long serialVersionUID = 3090041670794938671L;

	/**
	 * Creates a new {@link StandardViewAction}
	 */
	public StandardViewAction() {
		putValue(Action.NAME, "Reset View");
		putValue(Action.SMALL_ICON, new IconLoader("zoom_reset.png",
				Size.SMALL).get());
		putValue(Action.LARGE_ICON_KEY, new IconLoader("zoom_reset.png",
				Size.NORMAL).get());
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_HOME,
				0));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		IView mainFrame = ViewRegistry.getInstance().getMainFrame();
		MainFrame mf = (MainFrame) mainFrame;
		mf.getPipeline().resetView();
	}

}
