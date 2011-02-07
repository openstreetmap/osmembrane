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


package de.osmembrane.view.interfaces;

import de.osmembrane.view.dialogs.CommandLineDialog;

/**
 * Interface for {@link CommandLineDialog}.
 * 
 * @author tobias_kuhn
 * 
 */
public interface ICommandLineDialog extends IView {

	/**
	 * Sets the command line to display
	 * 
	 * @param commandline
	 *            the command line to display
	 */
	void setCommandline(String commandline);

}
