package de.osmembrane;

import de.osmembrane.controller.exceptions.ExceptionSeverity;
import de.osmembrane.model.ModelProxy;
import de.osmembrane.resources.Constants;
import de.osmembrane.view.IView;
import de.osmembrane.view.ViewRegistry;

/**
 * The one OO instance of the main program
 * 
 * @author tobias_kuhn
 *
 */
public class Application {
	
	/**
	 * Connects the most basic stuff of the MVC architecture,
	 */
	public void initiate() {
		try {
			ModelProxy.getInstance().addObserver(ViewRegistry.getInstance());
			System.setProperty("sun.awt.exception.handler",
					EDTExceptionHandler.class.getName());
		} catch(Exception e) {
			ViewRegistry.showException(this.getClass(), ExceptionSeverity.CRITICAL_UNEXPECTED_BEHAVIOR, e);
		}
	}

	/**
	 * Initializes the model.
	 */
	public void createModels() {
		try {
			ModelProxy.getInstance().accessFunctions().initiate(Constants.XML_RESOURCE_PATH);
			ModelProxy.getInstance().accessPipeline().truncate();
		} catch(Exception e) {
			ViewRegistry.showException(this.getClass(), ExceptionSeverity.CRITICAL_UNEXPECTED_BEHAVIOR, e);
		}
	}
	
	/**
	 * Shows the main window after application startup.
	 * Is guaranteed to be invoked by a different Runnable
	 */
	public void showMainFrame() {	
		try {
			IView mainFrame = ViewRegistry.getInstance().getMainFrame();
			mainFrame.showWindow();
		} catch(Exception e) {
			ViewRegistry.showException(this.getClass(), ExceptionSeverity.CRITICAL_UNEXPECTED_BEHAVIOR, e);
		}
	}
}