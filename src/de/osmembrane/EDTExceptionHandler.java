package de.osmembrane;


/**
 * Class necessary to be instantiated when an EDT exception occurs to forward it
 * to the ErrorDialog.
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
		Application.handleException(t);
	}

}
