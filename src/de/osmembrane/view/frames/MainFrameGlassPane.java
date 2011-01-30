package de.osmembrane.view.frames;

import java.awt.Point;
import java.awt.dnd.DragSource;

import javax.swing.JComponent;

import de.osmembrane.view.panels.LibraryFunction;

/**
 * Glass pane that improvises the drawing of the drag & drop
 * 
 * @author tobias_kuhn
 * 
 */
public class MainFrameGlassPane extends JComponent {

	private static final long serialVersionUID = -997512362081326789L;

	/**
	 * the {@link LibraryFunction} that is currently dragged to store
	 */
	private LibraryFunction dragAndDrop;

	/**
	 * Initializer for new {@link MainFrameGlassPane}
	 */
	public MainFrameGlassPane() {
	}

	/**
	 * Draws a specific {@link LibraryFunction} that is currently dragged & drop
	 * where the cursor is
	 * 
	 * @param libraryFunction
	 *            the view function to be drawn
	 * @param point
	 *            top left position of the view function to be drawn
	 */
	public void dragAndDrop(LibraryFunction libraryFunction, Point point) {
		if (dragAndDrop == null) {
			// add the display function
			dragAndDrop = new LibraryFunction(null,
					libraryFunction.getModelFunctionPrototype(), false);

			dragAndDrop.setSize(dragAndDrop.getPreferredSize());
			dragAndDrop.forceHighlight(true);
			dragAndDrop.setCursor(DragSource.DefaultCopyDrop);

			add(dragAndDrop);
			setVisible(true);
		}
		dragAndDrop.setLocation(point);
	}

	/**
	 * Ends the current drag and drop functionality and restores normal behavior
	 */
	public void endDragAndDrop() {
		if (dragAndDrop != null) {
			remove(dragAndDrop);
			dragAndDrop = null;
		}
		setVisible(false);
	}

}