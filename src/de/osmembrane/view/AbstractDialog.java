package de.osmembrane.view;

import java.awt.GraphicsEnvironment;
import java.awt.Point;

import javax.swing.JDialog;

/**
 * An abstract class interface to be used for dialog view elements.
 * 
 * @author tobias_kuhn
 *
 */
@SuppressWarnings("serial")
public abstract class AbstractDialog extends JDialog implements IView {
	
	/**
	 * common constructor for all dialog view elements
	 */
	public AbstractDialog() {
		//setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setModal(true);
		// TODO
	}

	@Override
	public void showWindow() {
		setVisible(true);
	}

	@Override
	public void hideWindow() {
		setVisible(false);
	}

	@Override
	public void setWindowTitle(String title) {
		setTitle(title);
	}
	
	@Override
	public void centerWindow() {		
		Point screenCenter = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
		Point edgeLeftTop = new Point(screenCenter.x - (getWidth() / 2),
									  screenCenter.y - (getHeight() / 2));
		setLocation(edgeLeftTop.x, edgeLeftTop.y);
	}
	
	@Override
	public void bringToFront() {
		toFront();
	}

}