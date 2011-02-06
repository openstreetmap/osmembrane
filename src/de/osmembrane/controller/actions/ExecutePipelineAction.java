package de.osmembrane.controller.actions;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import de.osmembrane.model.ModelProxy;
import de.osmembrane.model.persistence.FileType;
import de.osmembrane.model.settings.SettingType;
import de.osmembrane.resources.Resource;
import de.osmembrane.tools.I18N;
import de.osmembrane.tools.IconLoader.Size;
import de.osmembrane.tools.PipelineExecutor;
import de.osmembrane.view.ViewRegistry;
import de.osmembrane.view.dialogs.ExecutionStateDialog;
import de.osmembrane.view.interfaces.IExecutionStateDialog;

/**
 * Action to directly execute the created pipeline on the local shell.
 * 
 * @author tobias_kuhn
 * 
 */
public class ExecutePipelineAction extends AbstractAction {

	private static final long serialVersionUID = -173334958831335922L;

	/**
	 * Creates a new {@link ExecutePipelineAction}
	 */
	public ExecutePipelineAction() {
		putValue(
				Action.NAME,
				I18N.getInstance().getString(
						"Controller.Actions.ExecutePipeline.Name"));
		putValue(
				Action.SHORT_DESCRIPTION,
				I18N.getInstance().getString(
						"Controller.Actions.ExecutePipeline.Description"));
		putValue(Action.SMALL_ICON, Resource.PROGRAM_ICON.getImageIcon(
				"execute_pipeline.png", Size.SMALL));
		putValue(Action.LARGE_ICON_KEY, Resource.PROGRAM_ICON.getImageIcon(
				"execute_pipeline.png", Size.NORMAL));
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_X,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		FileType type = FileType.EXECUTION_FILETYPE;
		String pipeline = ModelProxy.getInstance().getPipeline().generate(type);

		/* the path to osmosis */
		final String osmosisPath = (String) ModelProxy.getInstance()
				.getSettings().getValue(SettingType.DEFAULT_OSMOSIS_PATH);

		/* the working directory */
		final String workingDirectory = (String) ModelProxy.getInstance()
				.getSettings().getValue(SettingType.DEFAULT_WORKING_DIRECTORY);

		final List<String> parameters = new ArrayList<String>();

		/* transform the params */
		String[] params = pipeline.split(" +");
		for (String param : params) {
			if (param.length() > 0) {
				parameters.add(param);
			}
		}

		IExecutionStateDialog dialog = ViewRegistry.getInstance().getCasted(ExecutionStateDialog.class, IExecutionStateDialog.class);
		dialog.showWindow();
		
		PipelineExecutor executor = new PipelineExecutor(osmosisPath, workingDirectory, parameters, dialog);
		executor.start();
	}
}