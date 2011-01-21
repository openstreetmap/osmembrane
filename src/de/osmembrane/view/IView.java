package de.osmembrane.view;

/**
 * View elements (i.e. windows) to be organized by the {@link ViewRegistry}
 * <b>N.B.</b>: Exceptions thrown in an IView constructor are not guaranteed
 * to result in an ErrorDialog. (the ErrorDialog is a view itself)
 * 
 * @author tobias_kuhn
 * 
 */
public interface IView {

	/**
	 * Shows the particular window
	 */
	public void showWindow();

	/**
	 * Hides the particular window
	 */
	public void hideWindow();

	/**
	 * Sets the title of the particular window
	 * 
	 * @param viewTitle
	 *            the new title
	 */
	public void setWindowTitle(String viewTitle);
	
	/**
	 * Centers this particular frame on the screen.
	 */
	public void centerWindow();
		
	/**
	 * Brings this window to the front.
	 */
	public void bringToFront();
	
}