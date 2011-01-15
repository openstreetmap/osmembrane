package de.osmembrane;

import de.osmembrane.controller.exceptions.ExceptionSeverity;
import de.osmembrane.view.ViewRegistry;

/**
 * Class necessary to be instantiated when an EDT exception occurs to forward it
 * to the ViewRegistry.
 * 
 * @see java.awt.EventDispatchThread#handleException
 * 
 * @author tobias_kuhn
 * 
 */
public class EDTExceptionHandler {

	/**
	 * Handles exceptions thrown by the EDT of Swing
	 * 
	 * @param t
	 *            the {@link Throwable} to catch
	 */
	public void handle(Throwable t) {
		Exception e = new Exception(t);
		ViewRegistry.showException(this.getClass(),
				ExceptionSeverity.UNEXPECTED_BEHAVIOR, e);
	}

}
