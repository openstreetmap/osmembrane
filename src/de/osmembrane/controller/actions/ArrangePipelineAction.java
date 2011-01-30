package de.osmembrane.controller.actions;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import de.osmembrane.model.ModelProxy;
import de.osmembrane.resources.Resource;
import de.osmembrane.tools.IconLoader.Size;

/**
 * Action to arrange the pipeline.
 * 
 * @author jakob_jarosch
 * 
 */
public class ArrangePipelineAction extends AbstractAction {

	private static final long serialVersionUID = -932349116204149527L;

	/**
	 * Creates a new {@link ArrangePipelineAction}
	 */
	public ArrangePipelineAction() {
		putValue(Action.NAME, "Arrange Pipeline");
		putValue(Action.SMALL_ICON, Resource.PROGRAM_ICON.getImageIcon("arrange_pipeline.png", Size.SMALL));
		putValue(Action.LARGE_ICON_KEY, Resource.PROGRAM_ICON.getImageIcon("arrange_pipeline.png", Size.NORMAL));
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_A,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ModelProxy.getInstance().accessPipeline().arrangePipeline();
	}
}