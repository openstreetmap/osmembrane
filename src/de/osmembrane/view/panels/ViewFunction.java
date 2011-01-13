package de.osmembrane.view.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import de.osmembrane.model.AbstractFunction;

public class ViewFunction extends JPanel {

	public ViewFunction(AbstractFunction modelFunction) {
		// TODO Auto-generated constructor stub
		setPreferredSize(new Dimension(256,256));
	}
	
	@Override
	protected void paintComponent(Graphics g) {		
		g.setColor(Color.YELLOW);
		g.fill3DRect(0, 0, getWidth(), getHeight(), true);
		g.setColor(Color.ORANGE);
		g.draw3DRect(0, 0, getWidth(), getHeight(), true);
	}
	
}
