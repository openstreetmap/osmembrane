package de.osmembrane.view;

import javax.swing.JDialog;
import javax.swing.JFrame;

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

}