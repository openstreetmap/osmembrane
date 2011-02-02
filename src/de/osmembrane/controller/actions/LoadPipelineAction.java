package de.osmembrane.controller.actions;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import de.osmembrane.Application;
import de.osmembrane.controller.ActionRegistry;
import de.osmembrane.exceptions.ControlledException;
import de.osmembrane.exceptions.ExceptionSeverity;
import de.osmembrane.model.ModelProxy;
import de.osmembrane.model.persistence.FileException;
import de.osmembrane.model.persistence.FileType;
import de.osmembrane.resources.Resource;
import de.osmembrane.tools.I18N;
import de.osmembrane.tools.IconLoader.Size;

/**
 * Action to load a OSMembrane pipeline stored in a file.
 * 
 * @author tobias_kuhn
 * 
 */
public class LoadPipelineAction extends AbstractAction {

	private static final long serialVersionUID = -5491327014988511548L;

	/**
	 * Creates a new {@link LoadPipelineAction}
	 */
	public LoadPipelineAction() {
		putValue(
				Action.NAME,
				I18N.getInstance().getString(
						"Controller.Actions.LoadPipeline.Name"));
		putValue(
				Action.SHORT_DESCRIPTION,
				I18N.getInstance().getString(
						"Controller.Actions.LoadPipeline.Description"));
		putValue(Action.SMALL_ICON, Resource.PROGRAM_ICON.getImageIcon(
				"load_pipeline.png", Size.SMALL));
		putValue(Action.LARGE_ICON_KEY, Resource.PROGRAM_ICON.getImageIcon(
				"load_pipeline.png", Size.NORMAL));
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (!ModelProxy.getInstance().getPipeline().isSaved()) {
			int result = JOptionPane.showConfirmDialog(null, I18N.getInstance()
					.getString("Controller.Actions.NewPipeline.NotSaved"),
					"Controller.Actions.NewPipeline.NotSaved.Title",
					JOptionPane.YES_NO_OPTION);
			if (result == JOptionPane.NO_OPTION) {
				return;
			}
		}
		
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(FileType.OSMEMBRANE.getFileFilter());

		int result = fileChooser.showOpenDialog(null);

		if (result == JFileChooser.APPROVE_OPTION) {

			URL file;
			try {
				file = fileChooser.getSelectedFile().toURI().toURL();
			} catch (MalformedURLException e2) {
				file = null;
			}

			try {
				ModelProxy.getInstance().getPipeline().loadPipeline(file);

				ActionRegistry.getInstance().get(ViewAllAction.class)
						.actionPerformed(null);
			} catch (FileException e1) {
				Application.handleException(new ControlledException(this,
						ExceptionSeverity.WARNING, e1, I18N.getInstance()
								.getString(
										"Controller.Actions.Load.Failed."
												+ e1.getType())));
			}
		}
	}
}