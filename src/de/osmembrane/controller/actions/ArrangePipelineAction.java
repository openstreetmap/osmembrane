package de.osmembrane.controller.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import de.osmembrane.model.ModelProxy;
import de.osmembrane.resources.Resource;
import de.osmembrane.tools.IconLoader.Size;
import de.osmembrane.view.dialogs.CommandLineDialog;

/**
 * Action to generate the pipeline command line and display the {@link CommandLineDialog}.
 * 
 * @author tobias_kuhn
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
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ModelProxy.getInstance().accessPipeline().arrangePipeline();
	}
}