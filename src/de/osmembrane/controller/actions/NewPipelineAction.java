package de.osmembrane.controller.actions;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import de.osmembrane.exceptions.ExceptionSeverity;
import de.osmembrane.model.ModelProxy;
import de.osmembrane.resources.Constants;
import de.osmembrane.tools.I18N;
import de.osmembrane.tools.IconLoader;
import de.osmembrane.tools.IconLoader.Size;
import de.osmembrane.view.ViewRegistry;

public class NewPipelineAction extends AbstractAction {

	public NewPipelineAction() {
		// throw new UnsupportedOperationException();
		// FIXME
		putValue(Action.NAME, "New Pipeline");
		putValue(Action.SMALL_ICON, new IconLoader("new_pipeline.png",
				Size.SMALL).get());
		putValue(Action.LARGE_ICON_KEY, new IconLoader("new_pipeline.png",
				Size.NORMAL).get());
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(
				KeyEvent.VK_INSERT, Toolkit.getDefaultToolkit()
						.getMenuShortcutKeyMask()));
		putValue(Action.SHORT_DESCRIPTION, "News an add pipeline item.");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (!ModelProxy.getInstance().accessPipeline().isSaved()) {
			int result = JOptionPane.showConfirmDialog(null, I18N.getInstance()
					.getString("Controller.Actions.NewPipeline.NotSaved"),
					"Controller.Actions.NewPipeline.NotSaved.Title",
					JOptionPane.YES_NO_OPTION);
			if (result == JOptionPane.NO_OPTION) {
				return;
			}
		}
		ModelProxy.getInstance().accessPipeline().clear();
	}
}