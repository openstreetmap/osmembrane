package de.osmembrane.controller.actions;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import de.osmembrane.model.ModelProxy;
import de.osmembrane.tools.IconLoader;
import de.osmembrane.tools.IconLoader.Size;

/**
 * Action to redo the last undone edit in the pipeline. 
 * 
 * @author tobias_kuhn
 * 
 */
public class RedoAction extends AbstractAction {

	private static final long serialVersionUID = 5665756570061846480L;

	/**
	 * Creates a new {@link RedoAction}
	 */
	public RedoAction() {
		putValue(Action.NAME, "Redo");
		putValue(Action.SMALL_ICON, new IconLoader("redo.png",
				Size.SMALL).get());
		putValue(Action.LARGE_ICON_KEY, new IconLoader("redo.png",
				Size.NORMAL).get());
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Y,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("redo available: "
				+ ModelProxy.getInstance().accessPipeline().redoAvailable());
		
		ModelProxy.getInstance().accessPipeline().redo();
	}
}