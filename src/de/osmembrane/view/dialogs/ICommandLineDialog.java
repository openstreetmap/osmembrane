package de.osmembrane.view.dialogs;

import de.osmembrane.view.IView;

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
