package de.osmembrane;

import de.osmembrane.model.ModelProxy;
import de.osmembrane.resources.Constants;
import de.osmembrane.view.ExceptionType;
import de.osmembrane.view.IView;
import de.osmembrane.view.ViewRegistry;

public class Application {
	
	/**
	 * Connects the most basic stuff of the MVC architecture
	 */
	public void initiate() {
		try {
			ModelProxy.getInstance().addObserver(ViewRegistry.getInstance());
		} catch(Exception e) {
			ViewRegistry.showException(this.getClass(), ExceptionType.CRITICAL_ABNORMAL_BEHAVIOR, e);
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
			ViewRegistry.showException(this.getClass(), ExceptionType.CRITICAL_ABNORMAL_BEHAVIOR, e);
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
			ViewRegistry.showException(this.getClass(), ExceptionType.CRITICAL_ABNORMAL_BEHAVIOR, e);
		}
	}
}