package de.osmembrane.view;

import java.awt.Component;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JDialog;

import de.osmembrane.view.interfaces.IView;

/**
 * An abstract class interface to be used for dialog view elements.
 * 
 * @author tobias_kuhn
 * 
 */
@SuppressWarnings("serial")
public abstract class AbstractDialog extends JDialog implements IView {

	/**
	 * This is the {@link KeyListener} for all buttons to be added, so they
	 * react on return. (A rather typical behavior not implemented by Swing
	 * natively)
	 */
	protected KeyListener returnButtonListener;

	/**
	 * common constructor for all dialog view elements
	 */
	public AbstractDialog() {
		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		setModal(true);
		setResizable(false);

		// ability activate buttons with return
		returnButtonListener = new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					Component focusedComponent = getFocusOwner();
					if (focusedComponent instanceof JButton) {
						((JButton) focusedComponent).doClick();
					}
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		};
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
		Point screenCenter = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getCenterPoint();
		Point edgeLeftTop = new Point(screenCenter.x - (getWidth() / 2),
				screenCenter.y - (getHeight() / 2));
		setLocation(edgeLeftTop.x, edgeLeftTop.y);
	}

	@Override
	public void bringToFront() {
		toFront();
	}

}