package de.osmembrane.controller.actions;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import de.osmembrane.model.ModelProxy;
import de.osmembrane.model.persistence.FileType;
import de.osmembrane.resources.Resource;
import de.osmembrane.tools.I18N;
import de.osmembrane.tools.IconLoader.Size;
import de.osmembrane.view.ViewRegistry;
import de.osmembrane.view.dialogs.CommandLineDialog;
import de.osmembrane.view.interfaces.ICommandLineDialog;

/**
 * Action to generate the pipeline command line and display the
 * {@link CommandLineDialog}.
 * 
 * @author tobias_kuhn
 * 
 */
public class GeneratePipelineAction extends AbstractAction {

	private static final long serialVersionUID = -932349116204149527L;

	/**
	 * Creates a new {@link GeneratePipelineAction}
	 */
	public GeneratePipelineAction() {
		putValue(
				Action.NAME,
				I18N.getInstance().getString(
						"Controller.Actions.GeneratePipeline.Name"));
		putValue(
				Action.SHORT_DESCRIPTION,
				I18N.getInstance().getString(
						"Controller.Actions.GeneratePipeline.Description"));
		putValue(Action.SMALL_ICON, Resource.PROGRAM_ICON.getImageIcon(
				"generate_pipeline.png", Size.SMALL));
		putValue(Action.LARGE_ICON_KEY, Resource.PROGRAM_ICON.getImageIcon(
				"generate_pipeline.png", Size.NORMAL));
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_G,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ICommandLineDialog commandLineDialog = ViewRegistry.getInstance()
				.getCasted(CommandLineDialog.class, ICommandLineDialog.class);

		// TODO Let the user choose the correct file-type.
		commandLineDialog.setCommandline(ModelProxy.getInstance().getPipeline()
				.generate(FileType.BASH));
		commandLineDialog.showWindow();
	}
}