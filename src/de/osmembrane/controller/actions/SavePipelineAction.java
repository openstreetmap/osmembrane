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

public class SavePipelineAction extends AbstractAction {

	public SavePipelineAction() {
		putValue(Action.NAME, "Save Pipeline");
		putValue(Action.SMALL_ICON, new IconLoader("save_pipline.png",
				Size.SMALL).get());
		putValue(Action.LARGE_ICON_KEY, new IconLoader("save_pipline.png",
				Size.NORMAL).get());
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		// FIXME
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		throw new UnsupportedOperationException();
	}
}