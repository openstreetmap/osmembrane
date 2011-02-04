package de.osmembrane.view.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.SystemColor;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

/**
 * A class that adds the most basic idea of the recurring pattern of a bar
 * displaying current status to Swing.
 * 
 * @author tobias_kuhn
 * 
 */
public class JStatusBar extends JPanel {

	private static final long serialVersionUID = -6417005916705677582L;
	
	/**
	 * The current text displayed
	 */
	private JLabel text;
	
	/**
	 * The current progress displayed	
	 */
	private JProgressBar progress;

	/**
	 * Creates a new {@link JStatusBar}.
	 */
	public JStatusBar() {
		setLayout(new FlowLayout());
		setBorder(BorderFactory.createEtchedBorder());
		
		progress = new JProgressBar(0, 100);
		add(progress);
		add(new JSeparator(SwingConstants.VERTICAL));
		
		text = new JLabel();
		add(text);		
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text.setText(text);
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return this.text.getText();
	}

	/**
	 * @param progress the progress 0.0 <= progress <= 1.0 to set
	 */
	public void setProgress(double progress) {
		this.progress.setValue((int) (100.0 * progress));
	}

	/**
	 * @return the progress
	 */
	public double getProgress() {
		return this.progress.getValue() / 100.0;
	}

}