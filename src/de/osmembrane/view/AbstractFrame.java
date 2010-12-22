package de.osmembrane.view;

import javax.swing.JFrame;

public abstract class AbstractFrame extends JFrame implements IView {

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