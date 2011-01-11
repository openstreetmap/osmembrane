package de.osmembrane;

import de.osmembrane.view.ExceptionType;
import de.osmembrane.view.IView;
import de.osmembrane.view.ViewRegistry;

public class Application {
	public Main unnamed_Main_;

	public void initiate() {
		try {
		//throw new UnsupportedOperationException();
		/*
		 * TODO: IMPLEMENT ME!
		 */
		// FIXME
		} catch(Exception e) {
			ViewRegistry.showException(this.getClass(), ExceptionType.CRITICAL_ABNORMAL_BEHAVIOR, e);
		}
	}

	public void createModels() {
		try {
		// TODO Auto-generated method stub
		//throw new UnsupportedOperationException();
		// FIXME
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