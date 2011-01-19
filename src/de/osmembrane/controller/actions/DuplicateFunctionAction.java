package de.osmembrane.controller.actions;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import de.osmembrane.model.ModelProxy;
import de.osmembrane.model.pipeline.AbstractFunction;
import de.osmembrane.resources.Constants;
import de.osmembrane.tools.IconLoader;
import de.osmembrane.tools.IconLoader.Size;
import de.osmembrane.view.IView;
import de.osmembrane.view.ViewRegistry;
import de.osmembrane.view.frames.MainFrame;
import de.osmembrane.view.panels.PipelineFunction;

public class DuplicateFunctionAction extends AbstractAction {

	public DuplicateFunctionAction() {
		putValue(Action.NAME, "Duplicate Function");
		putValue(Action.SMALL_ICON,
				new IconLoader("duplicate.png", Size.SMALL).get());
		putValue(Action.LARGE_ICON_KEY, new IconLoader("duplicate.png",
				Size.NORMAL).get());
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_D,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
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

				AbstractFunction duplicate = (AbstractFunction) ModelProxy
						.getInstance().accessFunctions()
						.duplicate(pf.getModelFunction());
				Point2D duplLoc = duplicate.getCoordinate();
				duplicate.setCoordinate(new Point2D.Double(duplLoc.getX() + 0.3
						* pf.getPreferredSize().width, duplLoc.getY() + 1.1
						* pf.getPreferredSize().height));

				ModelProxy.getInstance().accessPipeline()
						.addFunction(duplicate);
			}
		}
	}
}