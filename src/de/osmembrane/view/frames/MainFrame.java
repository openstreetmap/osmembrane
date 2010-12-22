package de.osmembrane.view.frames;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import de.osmembrane.view.AbstractFrame;

/**
 * The Main window that is the center of OSMembrane and the first thing
 * you'll see after the splash screen.
 * 
 * @author tobias_kuhn
 *
 */
public class MainFrame extends AbstractFrame {

	public MainFrame() {
		// TODO Auto-generated constructor stub
		addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowClosed(WindowEvent e) {
				System.exit(0);
				
			}

			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
	
	
	
}