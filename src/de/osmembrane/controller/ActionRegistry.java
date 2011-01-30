package de.osmembrane.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Action;

import de.osmembrane.controller.actions.AddConnectionAction;
import de.osmembrane.controller.actions.AddFunctionAction;
import de.osmembrane.controller.actions.ArrangePipelineAction;
import de.osmembrane.controller.actions.ChangeSettingsAction;
import de.osmembrane.controller.actions.DeleteSelectionAction;
import de.osmembrane.controller.actions.DuplicateFunctionAction;
import de.osmembrane.controller.actions.EditBoundingBoxPropertyAction;
import de.osmembrane.controller.actions.EditFilePropertyAction;
import de.osmembrane.controller.actions.EditListPropertyAction;
import de.osmembrane.controller.actions.EditPropertyAction;
import de.osmembrane.controller.actions.ExecutePipelineAction;
import de.osmembrane.controller.actions.ExitAction;
import de.osmembrane.controller.actions.ExportPipelineAction;
import de.osmembrane.controller.actions.GeneratePipelineAction;
import de.osmembrane.controller.actions.ImportPipelineAction;
import de.osmembrane.controller.actions.LoadPipelineAction;
import de.osmembrane.controller.actions.MoveFunctionAction;
import de.osmembrane.controller.actions.NewPipelineAction;
import de.osmembrane.controller.actions.PreviewPipelineAction;
import de.osmembrane.controller.actions.RedoAction;
import de.osmembrane.controller.actions.SaveAsPipelineAction;
import de.osmembrane.controller.actions.SavePipelineAction;
import de.osmembrane.controller.actions.ShowAboutAction;
import de.osmembrane.controller.actions.ShowHelpAction;
import de.osmembrane.controller.actions.UndoAction;
import de.osmembrane.model.ModelProxy;

/**
 * The action registry implements the Broker pattern to organize the
 * {@link Action}s.
 * 
 * @author tobias_kuhn
 * 
 */
public class ActionRegistry implements Observer {
	/**
	 * implements the Singleton pattern
	 */
	private static ActionRegistry instance = new ActionRegistry();

	/**
	 * internal storage of the actions, indexed by class
	 */
	public Map<Class<? extends Action>, Action> actions = new HashMap<Class<? extends Action>, Action>();

	/**
	 * initializes the {@link ActionRegistry} with all the actions this
	 * controller component has
	 */
	private ActionRegistry() {
		// pipeline actions
		register(new NewPipelineAction());
		register(new SavePipelineAction());
		register(new SaveAsPipelineAction());
		register(new LoadPipelineAction());
		register(new ExportPipelineAction());
		register(new ImportPipelineAction());
		register(new GeneratePipelineAction());
		register(new ExecutePipelineAction());
		register(new PreviewPipelineAction());
		register(new ArrangePipelineAction());

		// function actions
		register(new AddFunctionAction());
		register(new MoveFunctionAction());
		register(new DuplicateFunctionAction());
		register(new DeleteSelectionAction());

		// connection actions
		register(new AddConnectionAction());

		// property actions
		register(new EditPropertyAction());
		register(new EditListPropertyAction());
		register(new EditFilePropertyAction());
		register(new EditBoundingBoxPropertyAction());

		// undo, redo actions
		register(new UndoAction());
		register(new RedoAction());

		// other actions
		register(new ShowHelpAction());
		register(new ShowAboutAction());
		register(new ChangeSettingsAction());
		register(new ExitAction());
		
		ModelProxy.getInstance().accessPipeline().addObserver(this);
	}

	/**
	 * 
	 * @return the one and only instance of {@link ActionRegistry}
	 */
	public static ActionRegistry getInstance() {
		return instance;
	}

	/**
	 * Adds an {@link Action} to the registry
	 * 
	 * @param action
	 *            Action to add
	 */
	public void register(Action action) {
		actions.put(action.getClass(), action);
	}

	/**
	 * Returns an {@link Action} from the registry
	 * 
	 * @param clazz
	 *            desired class to return
	 * @return the registered object for that class
	 */
	public Action get(Class<? extends Action> clazz) {
		return actions.get(clazz);
	}

	@Override
	public void update(Observable o, Object arg) {
		// update all actions and their enabled state
		ActionRegistry
				.getInstance()
				.get(UndoAction.class)
				.setEnabled(
						ModelProxy.getInstance().accessPipeline()
								.undoAvailable());
		ActionRegistry
				.getInstance()
				.get(RedoAction.class)
				.setEnabled(
						ModelProxy.getInstance().accessPipeline()
								.redoAvailable());

		ActionRegistry
				.getInstance()
				.get(SaveAsPipelineAction.class)
				.setEnabled(
						!ModelProxy.getInstance().accessPipeline().isSaved());

		ActionRegistry
				.getInstance()
				.get(SavePipelineAction.class)
				.setEnabled(
						!ModelProxy.getInstance().accessPipeline().isSaved());
	}
}