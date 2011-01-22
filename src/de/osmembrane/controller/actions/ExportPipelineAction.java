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
 * Action to export the created pipeline to a file.
 * 
 * @author tobias_kuhn
 * 
 */
public class ExportPipelineAction extends AbstractAction {

	private static final long serialVersionUID = 8382050986007810817L;

	/**
	 * Creates a new {@link ExportPipelineAction}
	 */
	public ExportPipelineAction() {
		putValue(Action.NAME, "Export Pipeline");
		putValue(Action.SMALL_ICON, new IconLoader("export_pipeline.png",
				Size.SMALL).get());
		putValue(Action.LARGE_ICON_KEY, new IconLoader("export_pipeline.png",
				Size.NORMAL).get());
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_E,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO implement
		throw new UnsupportedOperationException();
	}
}