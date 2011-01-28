package de.osmembrane.view.dialogs;

import java.awt.GraphicsEnvironment;
import java.awt.Point;

import de.osmembrane.view.AbstractDialog;
import de.osmembrane.view.IView;
import de.unistuttgart.iev.osm.bboxchooser.BBoxChooserDialog;

/**
 * 
 * Simple dialog to display the generated command line, export it, or copy it to
 * the clipboard.
 * 
 * @see Spezifikation.pdf, chapter 2.4
 * 
 * @author tobias_kuhn
 *
 */
public class BoundingBoxDialog implements IView {

	private static final long serialVersionUID = 5182327519016989905L;
	
	private BBoxChooserDialog dialog;
	
	/**
	 * Creates a new {@link BoundingBoxDialog}
	 */
	public BoundingBoxDialog() {
		dialog = new BBoxChooserDialog();
		dialog.setModal(true);
	}

	@Override
	public void showWindow() {
		dialog.setVisible(true);
	}

	@Override
	public void hideWindow() {
		dialog.setVisible(false);
	}

	@Override
	public void setWindowTitle(String viewTitle) {
		dialog.setTitle(viewTitle);
	}

	@Override
	public void centerWindow() {
		Point screenCenter = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
		Point edgeLeftTop = new Point(screenCenter.x - (dialog.getWidth() / 2),
									  screenCenter.y - (dialog.getHeight() / 2));
		dialog.setLocation(edgeLeftTop.x, edgeLeftTop.y);
	}

	@Override
	public void bringToFront() {
		dialog.toFront();
	}


}