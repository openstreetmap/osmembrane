/*
 * This file is part of the OSMembrane project.
 * More informations under www.osmembrane.de
 * 
 * The project is licensed under the Creative Commons
 * Attribution-NonCommercial-ShareAlike 3.0 Unported License.
 * for more details about the license see
 * http://www.osmembrane.de/license/
 * 
 * Source: $HeadURL$ ($Revision$)
 * Last changed: $Date$
 */

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
import de.osmembrane.exceptions.ControlledException;
import de.osmembrane.exceptions.ExceptionSeverity;
import de.osmembrane.model.ModelProxy;
import de.osmembrane.model.persistence.FileException;
import de.osmembrane.model.persistence.FileType;
import de.osmembrane.resources.Resource;
import de.osmembrane.tools.I18N;
import de.osmembrane.tools.IconLoader.Size;

/**
 * Action to export the created pipeline to a file.
 * 
 * @author tobias_kuhn
 * 
 */
public class ExportPipelineAction extends AbstractAction {

	private static final long serialVersionUID = 8382050986007810817L;

	/**
	 * Creates a new {@link ExportPipelineAction}
	 */
	public ExportPipelineAction() {
		putValue(
				Action.NAME,
				I18N.getInstance().getString(
						"Controller.Actions.ExportPipeline.Name"));
		putValue(
				Action.SHORT_DESCRIPTION,
				I18N.getInstance().getString(
						"Controller.Actions.ExportPipeline.Description"));
		putValue(Action.SMALL_ICON, Resource.PROGRAM_ICON.getImageIcon(
				"export_pipeline.png", Size.SMALL));
		putValue(Action.LARGE_ICON_KEY, Resource.PROGRAM_ICON.getImageIcon(
				"export_pipeline.png", Size.NORMAL));
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_E,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(FileType.OSMEMBRANE.getFileFilter());
		fileChooser.addChoosableFileFilter(FileType.BASH.getFileFilter());
		fileChooser.addChoosableFileFilter(FileType.CMD.getFileFilter());
		fileChooser.addChoosableFileFilter(FileType.ALLTYPES.getFileFilter());

		int result = fileChooser.showSaveDialog(null);

		if (result == JFileChooser.APPROVE_OPTION) {

			URL file;
			try {
				file = fileChooser.getSelectedFile().toURI().toURL();
			} catch (MalformedURLException e2) {
				file = null;
			}

			try {
				FileType type = FileType.fileTypeFor(fileChooser
						.getSelectedFile());

				if (type == null) {
					JOptionPane
							.showMessageDialog(
									null,
									I18N.getInstance()
											.getString(
													"Controller.Actions.ExportPipeline.NoValidFileExtension"),
									I18N.getInstance()
											.getString(
													"Controller.Actions.ExportPipeline.NoValidFileExtension.Title"),
									JOptionPane.WARNING_MESSAGE);
				} else {
					ModelProxy.getInstance().getPipeline()
							.exportPipeline(file, type);
				}
			} catch (FileException e1) {
				String message = I18N.getInstance().getString(
						"Controller.Actions.Save.Failed." + e1.getType(),
						e1.getParentException().getMessage());

				Application.handleException(new ControlledException(this,
						ExceptionSeverity.WARNING, e1, message));
			}
		}
	}
}
