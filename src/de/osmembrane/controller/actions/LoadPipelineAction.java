package de.osmembrane.controller.actions;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import de.osmembrane.tools.IconLoader;
import de.osmembrane.tools.IconLoader.Size;

/**
 * Action to load a OSMembrane pipeline stored in a file.
 * 
 * @author tobias_kuhn
 * 
 */
public class LoadPipelineAction extends AbstractAction {

	private static final long serialVersionUID = -5491327014988511548L;

	/**
	 * Creates a new {@link LoadPipelineAction}
	 */
	public LoadPipelineAction() {
		putValue(Action.NAME, "Load Pipeline");
		putValue(Action.SMALL_ICON, new IconLoader("load_pipeline.png",
				Size.SMALL).get());
		putValue(Action.LARGE_ICON_KEY, new IconLoader("load_pipeline.png",
				Size.NORMAL).get());
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO implement
		throw new UnsupportedOperationException();
	}
}