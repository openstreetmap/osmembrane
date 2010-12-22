package de.osmembrane.view;

import javax.swing.JDialog;

public abstract class AbstractDialog extends JDialog implements IView {

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