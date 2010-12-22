package de.osmembrane.controller;

import java.util.HashMap;
import java.util.Map;

import javax.swing.Action;

/**
 * The action registry implements the Broker pattern to organize the
 * {@link Action}s.
 * 
 * @author tobias_kuhn
 * 
 */
public class ActionRegistry {
	/**
	 * implements the Singleton pattern
	 */
	private static ActionRegistry instance = new ActionRegistry();

	/**
	 * internal storage of the actions, indexed by class
	 */
	public Map<Class<? extends Action>, Action> actions = new HashMap<Class<? extends Action>, Action>();

	/**
	 * initializes the action registry with all the actions this controller
	 * component has
	 */
	private ActionRegistry() {
		// pipeline actions
		register(new NewPipelineAction());
		register(new SavePipelineAction());
		register(new LoadPipelineAction());
		register(new ExportPipelineAction());
		register(new ImportPipelineAction());
		register(new GeneratePipelineAction());
		register(new ExecutePipelineAction());
		register(new PreviewPipelineAction());

		// function actions
		register(new AddFunctionAction());
		register(new MoveFunctionAction());
		register(new DuplicateFunctionAction());
		register(new DeleteFunctionAction());

		// connection actions
		register(new AddConnectionAction());
		register(new DeleteConnectionAction());

		// property actions
		register(new EditPropertyAction());
		register(new EditListPropertyAction());
		register(new EditFilePropertyAction());
		register(new EditBoundingBoxPropertyAction());

		// undo, redo actions
		register(new UndoAction());
		register(new RedoAction());

		// view actions
		register(new MoveViewAction());
		register(new ZoomViewAction());

		// other actions
		register(new ShowHelpAction());
		register(new ChangeSettingsAction());
		register(new ExitAction());
	}

	/**
	 * 
	 * @return the one and only instance of ActionRegistry
	 */
	public static ActionRegistry getInstance() {
		return instance;
	}

	/**
	 * Adds an action to the registry
	 * 
	 * @param action
	 *            Action to add
	 */
	public void register(Action action) {
		actions.put(action.getClass(), action);
	}

	/**
	 * Returns an action from the registry
	 * 
	 * @param clazz
	 *            desired class to return
	 * @return the registered object for that class
	 */
	public Action get(Class<? extends Action> clazz) {
		return actions.get(clazz);
	}
}