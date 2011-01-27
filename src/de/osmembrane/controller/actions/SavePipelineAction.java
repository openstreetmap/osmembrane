package de.osmembrane.controller.actions;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.KeyStroke;

import de.osmembrane.Application;
import de.osmembrane.exceptions.ControlledException;
import de.osmembrane.exceptions.ExceptionSeverity;
import de.osmembrane.model.ModelProxy;
import de.osmembrane.model.persistence.FileException;
import de.osmembrane.resources.Constants;
import de.osmembrane.resources.Resource;
import de.osmembrane.tools.I18N;
import de.osmembrane.tools.IconLoader;
import de.osmembrane.tools.IconLoader.Size;
import de.osmembrane.view.IView;
import de.osmembrane.view.ViewRegistry;
import de.osmembrane.view.frames.MainFrame;

/**
 * Action to store a OSMembrane pipeline in file.
 * 
 * @author tobias_kuhn
 * 
 */
public class SavePipelineAction extends AbstractAction {

	private static final long serialVersionUID = 5036259208332239931L;

	/**
	 * Creates a new {@link SavePipelineAction}
	 */
	public SavePipelineAction() {
		putValue(Action.NAME, "Save Pipeline");
		putValue(Action.SMALL_ICON, Resource.PROGRAM_ICON.getImageIcon("save_pipeline.png", Size.SMALL));
		putValue(Action.LARGE_ICON_KEY, Resource.PROGRAM_ICON.getImageIcon("save_pipeline.png", Size.NORMAL));
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		/* get the MainFrame */
		IView mainFrame = ViewRegistry.getInstance().getMainFrame();
		MainFrame mf = (MainFrame) mainFrame;

		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
			@Override
			public String getDescription() {
				return (I18N.getInstance().getString(
						"Controller.Actions.FileTypeName")
						+ " (*" + Constants.OSMEMBRANE_EXTENSION)
						+ ")";
			}

			@Override
			public boolean accept(File arg0) {
				return arg0.getName().toLowerCase()
						.endsWith(Constants.OSMEMBRANE_EXTENSION);
			}
		});

		int result = fileChooser.showSaveDialog(mf);

		if (result == JFileChooser.APPROVE_OPTION) {
			String file = fileChooser.getSelectedFile().getAbsolutePath();

			if (!file.toLowerCase().endsWith(Constants.OSMEMBRANE_EXTENSION)) {
				file = file + Constants.OSMEMBRANE_EXTENSION;
			}

			try {
				ModelProxy.getInstance().accessPipeline().savePipeline(file);
			} catch (FileException e1) {
				Application.handleException(new ControlledException(this,
						ExceptionSeverity.WARNING, e1, I18N.getInstance()
								.getString(
										"Controller.Actions.Save.Failed."
												+ e1.getType())));
			}
		}
	}
}