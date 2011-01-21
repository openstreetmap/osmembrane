package de.osmembrane.controller.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import de.osmembrane.model.ModelProxy;
import de.osmembrane.model.pipeline.AbstractConnector;
import de.osmembrane.resources.Constants;
import de.osmembrane.tools.IconLoader;
import de.osmembrane.tools.IconLoader.Size;
import de.osmembrane.view.IView;
import de.osmembrane.view.ViewRegistry;
import de.osmembrane.view.frames.MainFrame;
import de.osmembrane.view.panels.PipelineFunction;
import de.osmembrane.view.panels.PipelineLink;

public class DeleteSelectionAction extends AbstractAction {

	public DeleteSelectionAction() {
		putValue(Action.NAME, "Delete Selection");
		putValue(Action.SMALL_ICON, new IconLoader("delete_function.png",
				Size.SMALL).get());
		putValue(Action.LARGE_ICON_KEY, new IconLoader("delete_function.png",
				Size.NORMAL).get());
		putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
		setEnabled(false);
		// FIXME
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		IView mainFrame = ViewRegistry.getInstance().getMainFrame();
		MainFrame mf = (MainFrame) mainFrame;
		Object selected = mf.getPipeline().getSelected();

		if (selected != null) {
			if (selected instanceof PipelineFunction) {
				PipelineFunction pf = (PipelineFunction) selected;
				ModelProxy.getInstance().accessPipeline()
						.deleteFunction(pf.getModelFunction());

			} else if (selected instanceof PipelineLink) {
				PipelineLink pl = (PipelineLink) selected;
				pl.getLinkSource()
						.getModelConnector()
						.getParent()
						.removeConnectionTo(
								pl.getLinkDestination().getModelConnector()
										.getParent());
			}
		}
	} /* actionPerformed */
}