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
 * Action to undo the last done edit in the pipeline.
 * 
 * @author tobias_kuhn
 * 
 */
public class UndoAction extends AbstractAction {

	private static final long serialVersionUID = 4603258297595882886L;

	/**
	 * Creates a new {@link UndoAction}
	 */
	public UndoAction() {
		putValue(Action.NAME, "Undo");
		putValue(Action.SMALL_ICON,
				new IconLoader("undo.png", Size.SMALL).get());
		putValue(Action.LARGE_ICON_KEY,
				new IconLoader("undo.png", Size.NORMAL).get());
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Z,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		setEnabled(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("undo available: "
				+ ModelProxy.getInstance().accessPipeline().undoAvailable());
		ModelProxy.getInstance().accessPipeline().undo();
	}
}