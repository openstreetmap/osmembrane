package de.osmembrane.view;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import de.osmembrane.view.IView;

/**
 * The view registry implements the View-Handler pattern to organize the
 * {@link IView}s.
 * 
 * @author tobias_kuhn
 * 
 */
public class ViewRegistry implements Observer {
	/**
	 * implements the Singleton pattern
	 */
	private ViewRegistry instance = new ViewRegistry();

	/**
	 * internal storage of the views, indexed by class
	 */
	private Map<Class<? extends IView>, IView> views = new HashMap<Class<? extends IView>, IView>();

	/**
	 * 
	 * @return the one and only instance of ViewRegistry
	 */
	public ViewRegistry getInstance() {
		return instance;
	}

	/**
	 * Adds a view to the registry
	 * 
	 * @param view
	 *            IView to add
	 */
	public void register(IView view) {
		views.put(view.getClass(), view);
	}

	/**
	 * Returns a view from the registry
	 * 
	 * @param clazz
	 *            desired class to return
	 * @return the registered object for that class
	 */
	public IView get(Class<? extends IView> clazz) {
		return views.get(clazz);
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO: forward update() for ViewRegistry
		throw new UnsupportedOperationException();
		
	}
}