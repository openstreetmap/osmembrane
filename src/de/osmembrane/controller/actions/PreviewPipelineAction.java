package de.osmembrane.controller.actions;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import de.osmembrane.resources.Resource;
import de.osmembrane.tools.I18N;
import de.osmembrane.tools.IconLoader.Size;

/**
 * Action to preview the generated pipeline. Like {@link ExecutePipelineAction},
 * only faster and with direct display.
 * 
 * @author tobias_kuhn
 * 
 */
public class PreviewPipelineAction extends AbstractAction {

	private static final long serialVersionUID = 8099091858953447990L;

	/**
	 * Creates a new {@link PreviewPipelineAction}
	 */
	public PreviewPipelineAction() {
		putValue(
				Action.NAME,
				I18N.getInstance().getString(
						"Controller.Actions.PreviewPipeline.Name"));
		putValue(
				Action.SHORT_DESCRIPTION,
				I18N.getInstance().getString(
						"Controller.Actions.PreviewPipeline.Description"));
		putValue(Action.SMALL_ICON, Resource.PROGRAM_ICON.getImageIcon(
				"preview_pipeline.png", Size.SMALL));
		putValue(Action.LARGE_ICON_KEY, Resource.PROGRAM_ICON.getImageIcon(
				"preview_pipeline.png", Size.NORMAL));
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_P,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO implement
		throw new UnsupportedOperationException();
	}
}