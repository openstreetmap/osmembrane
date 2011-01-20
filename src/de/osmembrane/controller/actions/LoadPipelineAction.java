package de.osmembrane.controller.actions;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import de.osmembrane.resources.Constants;
import de.osmembrane.tools.IconLoader;
import de.osmembrane.tools.IconLoader.Size;
import de.osmembrane.view.ViewRegistry;

public class LoadPipelineAction extends AbstractAction {

	public LoadPipelineAction() {
		putValue(Action.NAME, "Load Pipeline");
		putValue(Action.SMALL_ICON, new IconLoader("load_pipeline.png",
				Size.SMALL).get());
		putValue(Action.LARGE_ICON_KEY, new IconLoader("load_pipeline.png",
				Size.NORMAL).get());
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		// FIXME
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		throw new UnsupportedOperationException();
	}
}