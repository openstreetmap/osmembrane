package de.osmembrane.view;

/**
 * View elements (i.e. windows) to be organized by the {@link ViewRegistry}
 * 
 * @author tobias_kuhn
 * 
 */
public interface IView {

	/**
	 * Shows the particular window
	 */
	public void show();

	/**
	 * Hides the particular window
	 */
	public void hide();
}