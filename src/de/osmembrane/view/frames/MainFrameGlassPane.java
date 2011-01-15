package de.osmembrane.view.frames;

import java.awt.Point;

import javax.swing.JComponent;

import de.osmembrane.view.panels.ViewFunction;

/**
 * Glass pane that improvises the drawing of the drag & drop
 * 
 * @author tobias_kuhn
 * 
 */
public class MainFrameGlassPane extends JComponent {

	private static final long serialVersionUID = -997512362081326789L;

	/**
	 * the function that is currently dragged to store
	 */
	private ViewFunction dragAndDrop;

	/**
	 * Initializer for new glass pane
	 */
	public MainFrameGlassPane() {
	}

	/**
	 * Draws a specific view function that is currently dragged & drop where
	 * the cursor is
	 * 
	 * @param viewFunction
	 *            the view function to be drawn
	 * @param point
	 *            top left position of the view function to be drawn
	 */
	protected void dragAndDrop(ViewFunction viewFunction, Point point) {
		if (dragAndDrop == null) {
			dragAndDrop = new ViewFunction(
					viewFunction.getModelFunctionPrototype(), false);
			dragAndDrop.setSize(dragAndDrop.getPreferredSize());
			dragAndDrop.forceHighlight(true);
			add(dragAndDrop);
			setVisible(true);
		}
		dragAndDrop.setLocation(point);
	}
	
	/**
	 * Ends the current drag and drop functionality and restores
	 * normal behavior
	 */
	protected void endDragAndDrop() {
		if (dragAndDrop != null) {
			remove(dragAndDrop);
			dragAndDrop = null;
		}
		setVisible(false);
	}

}