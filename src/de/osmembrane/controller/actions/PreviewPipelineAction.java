package de.osmembrane.controller.actions;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import com.sun.xml.internal.ws.util.StringUtils;

import de.osmembrane.Application;
import de.osmembrane.exceptions.ControlledException;
import de.osmembrane.exceptions.ExceptionSeverity;
import de.osmembrane.model.ModelProxy;
import de.osmembrane.model.persistence.FileType;
import de.osmembrane.model.settings.SettingType;
import de.osmembrane.resources.Resource;
import de.osmembrane.tools.I18N;
import de.osmembrane.tools.IconLoader.Size;
import de.unistuttgart.iev.osm.osmosiscontrol.JOSMExecutor;
import de.unistuttgart.iev.osm.osmosiscontrol.OsmosisExecutor;
import de.unistuttgart.iev.osm.osmosiscontrol.OsmosisResult;

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
		FileType type = FileType.EXECUTION_FILETYPE;
		String pipeline = ModelProxy.getInstance().getPipeline().generate(type);

		/* the path to osmosis */
		String osmosisPath = (String) ModelProxy.getInstance().getSettings()
				.getValue(SettingType.DEFAULT_OSMOSIS_PATH);

		/* the path to josm */
		String josmPath = (String) ModelProxy.getInstance().getSettings()
				.getValue(SettingType.DEFAULT_JOSM_PATH);

		/* the working directory */
		String workingDirectory = (String) ModelProxy.getInstance()
				.getSettings().getValue(SettingType.DEFAULT_WORKING_DIRECTORY);

		/* execute... */
		try {
			OsmosisExecutor exec = new OsmosisExecutor(osmosisPath);

			OsmosisResult result = exec
					.executeOsmosisSynchronously(Arrays.asList(pipeline),
							new File(workingDirectory), null);
			System.out.println("Osmosis done.");
			System.out.println("Osmosis exit value was: "
					+ result.getExitValue());
			System.out.println("Osmosis STDOUT/ERR output was:");
			System.out.println("------------------------------");
			System.out.println(result.getOutput());
			System.err.println("------------------------------");

			if (result.getExitValue() != 0) {
				System.out.println("Osmosis exit value != 0, aborting.");
				return;
			}
		} catch (IllegalArgumentException e1) {
			/* TODO internationlize the exception */
			Application.handleException(new ControlledException(this,
					ExceptionSeverity.WARNING,
					"Osmosis wurde unter dem angegebenen Pfad nicht gefunden"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		System.out.println("Executing JOSM...");

		try {
			JOSMExecutor jexec = new JOSMExecutor(josmPath);

			jexec.executeJOSM(Arrays.asList("test.osm"), new File(
					workingDirectory), null);
		} catch (IllegalArgumentException e1) {
			/* TODO internationlize the exception */
			Application.handleException(new ControlledException(this,
					ExceptionSeverity.WARNING,
					"JOSM wurde unter dem angegebenen Pfad nicht gefunden"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}
}