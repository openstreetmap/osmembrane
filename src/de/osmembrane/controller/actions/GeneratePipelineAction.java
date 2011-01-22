package de.osmembrane.controller.actions;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import de.osmembrane.tools.IconLoader;
import de.osmembrane.tools.IconLoader.Size;
import de.osmembrane.view.IView;
import de.osmembrane.view.ViewRegistry;
import de.osmembrane.view.dialogs.CommandLineDialog;

/**
 * Action to generate the pipeline command line and display the {@link CommandLineDialog}.
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
		putValue(Action.NAME, "Generate Pipeline");
		putValue(Action.SMALL_ICON, new IconLoader("generate_pipeline.png",
				Size.SMALL).get());
		putValue(Action.LARGE_ICON_KEY, new IconLoader("generate_pipeline.png",
				Size.NORMAL).get());
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_G,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		IView commandLineDialog = ViewRegistry.getInstance().get(
				CommandLineDialog.class);
		CommandLineDialog cld = (CommandLineDialog) commandLineDialog;
		
		// TODO implement
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