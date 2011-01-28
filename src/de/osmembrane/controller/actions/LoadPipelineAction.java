package de.osmembrane.controller.actions;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.KeyStroke;

import de.osmembrane.Application;
import de.osmembrane.exceptions.ControlledException;
import de.osmembrane.exceptions.ExceptionSeverity;
import de.osmembrane.model.ModelProxy;
import de.osmembrane.model.persistence.FileException;
import de.osmembrane.model.persistence.FileType;
import de.osmembrane.resources.Constants;
import de.osmembrane.resources.Resource;
import de.osmembrane.tools.I18N;
import de.osmembrane.tools.IconLoader.Size;
import de.osmembrane.view.ViewRegistry;
import de.osmembrane.view.frames.MainFrame;

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
		putValue(Action.NAME, "Load Pipeline");
		putValue(Action.SMALL_ICON, Resource.PROGRAM_ICON.getImageIcon("load_pipeline.png", Size.SMALL));
		putValue(Action.LARGE_ICON_KEY, Resource.PROGRAM_ICON.getImageIcon("load_pipeline.png", Size.NORMAL));
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		/* get the MainFrame */
		MainFrame mainFrame = ViewRegistry.getInstance().getMainFrameByPass();

		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(FileType.OSMEMBRANE.getFileFilter());

		int result = fileChooser.showOpenDialog(mainFrame);

		if (result == JFileChooser.APPROVE_OPTION) {
			
			URL file;
			try {
				file = fileChooser.getSelectedFile().toURI().toURL();
			} catch (MalformedURLException e2) {
				file = null;
			}
			
			try {
				ModelProxy.getInstance().accessPipeline().loadPipeline(file);
			} catch (FileException e1) {
				e1.getParentException().printStackTrace();
				Application.handleException(new ControlledException(this,
						ExceptionSeverity.WARNING, e1, I18N.getInstance()
								.getString(
										"Controller.Actions.Load.Failed."
												+ e1.getType())));
			}
		}
	}
}