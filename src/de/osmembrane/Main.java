package de.osmembrane;

import javax.swing.SwingUtilities;

import de.osmembrane.model.ModelProxy;

/**
 * the Main class for java to contain the static main() method
 * 
 * @author tobias_kuhn
 * 
 */
public class Main {

	private static Application application;

	/**
	 * Initial routine of the program
	 * 
	 * @param args
	 *            command-line arguments
	 */
	public static void main(String[] args) {
		application = new Application();

		// create the models
		application.createModels();
		
		// set the correct locale
		application.setLocale();
		
		// check if a backup file is available
		application.checkForBackup();
		
		// initiate the most basic stuff
		application.initiate();

		// standard method to start Swing GUI
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				application.showMainFrame();
			}
		});

	}
	
	/**
	 * @return the currently running application
	 */
	public static Application getApplication() {
		return application;
	}
}