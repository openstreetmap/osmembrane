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
