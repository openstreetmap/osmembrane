package de.osmembrane;

import javax.swing.SwingUtilities;

/**
 * the Main class for java to contain the static main() method
 * @author tobias_kuhn
 *
 */
public class Main {

	/**
	 * Initial routine of the program
	 * @param args command-line arguments
	 */
	public static void main(String[] args) {
		final Application application = new Application();
		
		// TODO: what does this?
		application.createModels();
		// TODO: what does this?
		application.initiate();
		
		// standard method to start Swing GUI
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				application.showMainFrame();				
			}
		});		
		
	}
}