package de.osmembrane.view.panels;

/**
 * An interface that can be implemented for the use of receiving zooming actions
 * invoked from the Controller.
 * 
 * @author tobias_kuhn
 * 
 */
public interface IZoomDevice {

	/**
	 * Zooms in
	 */
	public void zoomIn();

	/**
	 * Zooms out
	 */
	public void zoomOut();

	/**
	 * Resets the view to standard
	 */
	public void resetView();

	/**
	 * Shows the entire pipeline
	 */
	public void showEntireView();

}
