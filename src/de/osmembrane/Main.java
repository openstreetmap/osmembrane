/*
 * This file is part of the OSMembrane project.
 * More informations under www.osmembrane.de
 * 
 * The project is licensed under the GNU GENERAL PUBLIC LICENSE 3.0.
 * for more details about the license see http://www.osmembrane.de/license/
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

        // configure UI defaults (default input maps and such)
        application.configureUIDefaults();

        // create home directory if not exists
        application.createHomeDirectory();

        // create the models
        application.createModels();

        // set the correct locale
        application.setLocale();

        // initiate the most basic stuff
        application.initiate();

        // check if a backup file is available
        application.checkForBackup();

        // // check if an update for OSMembrane is available
        // application.checkForUpdates();

        // setup backup thread
        application.initializeBackup();

        // standard method to start Swing GUI
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                application.createViews();
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
