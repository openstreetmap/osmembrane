package de.osmembrane.controller.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import de.osmembrane.controller.events.ContainingEvent;
import de.osmembrane.controller.events.ContainingLocationEvent;
import de.osmembrane.model.ModelProxy;
import de.osmembrane.model.pipeline.AbstractFunction;
import de.osmembrane.model.settings.AbstractFunctionPreset;
import de.osmembrane.resources.Resource;
import de.osmembrane.tools.I18N;
import de.osmembrane.tools.IconLoader.Size;
import de.osmembrane.view.ViewRegistry;
import de.osmembrane.view.frames.MainFrame;
import de.osmembrane.view.interfaces.IMainFrame;
import de.osmembrane.view.panels.PipelineFunction;

/**
 * Action to load saved presets for a specific function. 
 * 
 * @author tobias_kuhn
 * 
 */
public class LoadFunctionPresetAction extends AbstractAction {

	private static final long serialVersionUID = 6264271045174747984L;

	/**
	 * Creates a new {@link LoadFunctionPresetAction}
	 */
	public LoadFunctionPresetAction() {
		putValue(
				Action.NAME,
				I18N.getInstance().getString(
						"Controller.Actions.LoadFunctionPreset.Name"));
		putValue(
				Action.SHORT_DESCRIPTION,
				I18N.getInstance().getString(
						"Controller.Actions.LoadFunctionPreset.Description"));
		putValue(Action.SMALL_ICON, Resource.PROGRAM_ICON.getImageIcon(
				"load_pipeline.png", Size.SMALL));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		IMainFrame mainFrame = ViewRegistry.getInstance().getCasted(MainFrame.class, IMainFrame.class);
		Object select = mainFrame.getSelected();
		if ((select == null) || !(select instanceof PipelineFunction)) {
			return;
		}				
		AbstractFunction function = ((PipelineFunction) select).getModelFunction();
		
		AbstractFunctionPreset afp[] = ModelProxy.getInstance().getSettings().getAllFunctionPresets(function);
		
		// open the windows with afp
		
		// return the action
		
		/* got delete:
		 * 
		 * need: AbstractFunctionPreset
		 * call: ModelProxy.getInstance().getSettings().deleteFunctionPreset(AbstractFunctionPreset);
		 */
		
		/* got load
		 * 
		 * need: AbstractFunctionPreset, AbstractFunction
		 * call: AbstractFunctionPreset.loadPreset(AbstractFunction);
		 */
		
		/* got cancel:
		 * 
		 * need: -
		 * call: break;
		 */
	}
}