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
	private ActionRegistry instance = new ActionRegistry();

	/**
	 * internal storage of the actions, indexed by class
	 */
	public Map<Class<? extends Action>, Action> actions = new HashMap<Class<? extends Action>, Action>();

	/**
	 * 
	 * @return the one and only instance of ActionRegistry
	 */
	public ActionRegistry getInstance() {
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