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
 * Action to store a OSMembrane pipeline in file.
 * 
 * @author tobias_kuhn
 * 
 */
public class SavePipelineAction extends AbstractAction {

	private static final long serialVersionUID = 5036259208332239931L;

	/**
	 * Creates a new {@link SavePipelineAction}
	 */
	public SavePipelineAction() {
		putValue(Action.NAME, "Save Pipeline");
		putValue(Action.SMALL_ICON, new IconLoader("save_pipline.png",
				Size.SMALL).get());
		putValue(Action.LARGE_ICON_KEY, new IconLoader("save_pipline.png",
				Size.NORMAL).get());
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO implement
		throw new UnsupportedOperationException();
	}
}