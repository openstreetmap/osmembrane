package de.osmembrane.view;

import java.util.ArrayList;
import java.util.Map;

import de.osmembrane.view.IView;

/**
 * The view registry implements the View-Handler pattern to organize the views.
 * 
 * @author Whaite
 *
 */
public class ViewRegistry {
	/**
	 * implements the Singleton pattern
	 */
	private ViewRegistry instance = new ViewRegistry();
	
	/**
	 * internal storage of the views, indexed by class
	 */
	private Map<Class<? extends IView>, IView> views;

	/**
	 * 
	 * @return the one and only instance of ViewRegistry
	 */
	public ViewRegistry getInstance() {
		return instance;
	}

	/**
	 * Adds a view to the registry
	 * @param view IView to add
	 */
	public void register(IView view) {
		views.put(view.class, view);
	}

	/**
	 * Returns a view from the registry
	 * @param clazz desired class to return
	 * @return the registered object for that class
	 */
	public IView get(Class clazz) {
		return views.get(clazz);
	}
}