package de.osmembrane.controller.actions;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;

import de.osmembrane.resources.Constants;
import de.osmembrane.tools.IconLoader;
import de.osmembrane.tools.IconLoader.Size;
import de.osmembrane.view.IView;
import de.osmembrane.view.ViewRegistry;
import de.osmembrane.view.dialogs.CommandLineDialog;

public class GeneratePipelineAction extends AbstractAction {

	public GeneratePipelineAction() {
		putValue(Action.NAME, "Generate Pipeline");
		putValue(Action.SMALL_ICON, new IconLoader("generate_pipeline.png",
				Size.SMALL).get());
		putValue(Action.LARGE_ICON_KEY, new IconLoader("generate_pipeline.png",
				Size.NORMAL).get());
		// FIXME
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		IView commandLineDialog = ViewRegistry.getInstance().get(
				CommandLineDialog.class);
		CommandLineDialog cld = (CommandLineDialog) commandLineDialog;
		
		String NL = System.getProperty("line.separator");
		cld.setCommandline("osmosis \\" + NL +
				"--rx full/planet-071128.osm.bz2 \\" + NL +
				"--tee 16 \\" + NL +
				"--bp file=polygons/europe/germany/baden-wuerttemberg.poly \\" + NL +
				"--wx baden-wuerttemberg.osm.bz2 \\" + NL +
				"--bp file=polygons/europe/germany/bayern.poly \\" + NL +
				"--wx bayern.osm.bz2 \\" + NL +
				"--bp file=polygons/europe/germany/berlin.poly \\");
		cld.showWindow();
	}
}