/*
 * This file is part of the OSMembrane project.
 * More informations under www.osmembrane.de
 * 
 * The project is licensed under the Creative Commons
 * Attribution-NonCommercial-ShareAlike 3.0 Unported License.
 * for more details about the license see
 * http://www.osmembrane.de/license/
 * 
 * Source: $HeadURL$ ($Revision$)
 * Last changed: $Date$
 */


package de.osmembrane;

import javax.swing.SwingUtilities;

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

		// initiate the most basic stuff
		application.initiate();

		// check if a backup file is available
		application.checkForBackup();

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
