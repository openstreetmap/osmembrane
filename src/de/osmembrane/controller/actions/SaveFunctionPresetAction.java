package de.osmembrane.controller.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;

import de.osmembrane.controller.events.ContainingEvent;
import de.osmembrane.controller.events.ContainingLocationEvent;
import de.osmembrane.model.ModelProxy;
import de.osmembrane.model.pipeline.AbstractFunction;
import de.osmembrane.resources.Resource;
import de.osmembrane.tools.I18N;
import de.osmembrane.tools.IconLoader.Size;
import de.osmembrane.view.ViewRegistry;
import de.osmembrane.view.frames.MainFrame;
import de.osmembrane.view.interfaces.IMainFrame;
import de.osmembrane.view.panels.PipelineFunction;

/**
 * Action to save properties as a preset for a specific function. 
 * 
 * @author tobias_kuhn
 * 
 */
public class SaveFunctionPresetAction extends AbstractAction {

	private static final long serialVersionUID = 6264271045174747984L;

	/**
	 * Creates a new {@link SaveFunctionPresetAction}
	 */
	public SaveFunctionPresetAction() {
		putValue(
				Action.NAME,
				I18N.getInstance().getString(
						"Controller.Actions.SaveFunctionPreset.Name"));
		putValue(
				Action.SHORT_DESCRIPTION,
				I18N.getInstance().getString(
						"Controller.Actions.SaveFunctionPreset.Description"));
		putValue(Action.SMALL_ICON, Resource.PROGRAM_ICON.getImageIcon(
				"save_pipeline.png", Size.SMALL));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		IMainFrame mainFrame = ViewRegistry.getInstance().getCasted(MainFrame.class, IMainFrame.class);
		Object select = mainFrame.getSelected();
		if ((select == null) || !(select instanceof PipelineFunction)) {
			return;
		}				
		AbstractFunction function = ((PipelineFunction) select).getModelFunction();
		
		/* Request a name for the preset */		
		// TODO internationalisieren
		String name = JOptionPane.showInputDialog(null, "Name dafür?", "Function Preset Name", JOptionPane.QUESTION_MESSAGE);
		if(name != null) {
			ModelProxy.getInstance().getSettings().saveFunctionPreset(name, function);
		}
	}
}