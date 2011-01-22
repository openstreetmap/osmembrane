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
 * Action to import a pipeline from a file that contains a command line.
 * 
 * @author tobias_kuhn
 * 
 */
public class ImportPipelineAction extends AbstractAction {

	private static final long serialVersionUID = -6853818186320458126L;

	/**
	 * Creates a new {@link ImportPipelineAction}
	 */
	public ImportPipelineAction() {
		putValue(Action.NAME, "Import Pipeline");
		putValue(Action.SMALL_ICON, new IconLoader("import_pipeline.png",
				Size.SMALL).get());
		putValue(Action.LARGE_ICON_KEY, new IconLoader("import_pipeline.png",
				Size.NORMAL).get());
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_I,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO implement
		throw new UnsupportedOperationException();
	}
}