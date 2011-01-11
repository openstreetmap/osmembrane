package de.osmembrane.view;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import de.osmembrane.view.IView;
import de.osmembrane.view.dialogs.CommandLineDialog;
import de.osmembrane.view.dialogs.ErrorDialog;
import de.osmembrane.view.dialogs.ListSelectionDialog;
import de.osmembrane.view.dialogs.MapSelectionDialog;
import de.osmembrane.view.dialogs.SettingsDialog;
import de.osmembrane.view.frames.HelpFrame;
import de.osmembrane.view.frames.MainFrame;

/**
 * The view registry implements the View-Handler pattern to organize the
 * {@link IView}s.
 * 
 * @author tobias_kuhn
 * 
 */
public class ViewRegistry implements Observer {

	/**
	 * implements the error-handling dialog Note: This is important to be static
	 * and external to the ViewRegistry instance, because anyway else occurring
	 * exceptions during anything static would cause an
	 * ExceptionInInitializerError because the error handling would require the
	 * ViewRegistry instance to be available.
	 */
	private static ErrorDialog errorDialog;

	/**
	 * implements the Singleton pattern
	 */
	private static ViewRegistry instance = new ViewRegistry();

	/**
	 * internal storage of the views, indexed by class
	 */
	private Map<Class<? extends IView>, IView> views = new HashMap<Class<? extends IView>, IView>();

	/**
	 * initializes the view registry
	 */
	private ViewRegistry() {
	}

	/**
	 * 
	 * @return the one and only instance of ViewRegistry
	 */
	public static ViewRegistry getInstance() {
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
	 * Returns a view from the registry. If not already registered, creates it
	 * and registers it.
	 * 
	 * @param clazz
	 *            desired class to return
	 * @return the registered object for that class
	 */
	public IView get(Class<? extends IView> clazz) {
		IView result = views.get(clazz);

		// if it does not exist, create it
		if (result == null) {
			try {
				result = clazz.newInstance();
			} catch (Exception e) {
				showException(this.getClass(), ExceptionType.ABNORMAL_BEHAVIOR,
						e);
			}
			views.put(clazz, result);
		}

		return result;
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO: forward update() for ViewRegistry
		throw new UnsupportedOperationException();

	}

	/**
	 * Note: The view registry can be exchanged along with all view components,
	 * so it has to identify its main window for the purpose of showing this
	 * window at startup.
	 * 
	 * @return the main frame of this view component
	 */
	public IView getMainFrame() {
		return get(MainFrame.class);
	}

	/**
	 * A parameter being null results automatically in a fatal error blaming the
	 * initial caller.
	 * 
	 * @param triggerClass
	 *            class in which the Exception occurred
	 * @param type
	 *            indicates what happened and how fatal it is
	 * @param exception
	 *            original Exception
	 */
	public static void showException(Class<?> triggerClass, ExceptionType type,
			Exception exception) {
		if (errorDialog == null) {
			errorDialog = new ErrorDialog();
		}
		errorDialog.showException(triggerClass, type, exception);
	}
}