package de.osmembrane.view.interfaces;

import de.osmembrane.controller.ICallable;

/**
 * Interface for {@link ExecutionStateDialog}.
 * 
 * @author jakob_jarosch
 */
public interface IExecutionStateDialog {
	
	/**
	 * Sets the status of the execution action.
	 * 
	 * @param status status to be set
	 */
	public void setStatus(String status);
	
	/**
	 * Sets the progress of the bar.
	 * 
	 * @param progress progress to be set
	 */
	public void setProgress(double progress);
	
	/**
	 * Adds a line to the output.
	 * 
	 * @param outputLine line which should be added
	 */
	public void addOutputLine(String outputLine);
	
	/**
	 * Clears the window.
	 */
	public void clear();
	
	/**
	 * Closes the window.
	 */
	public void dispose();
	
	/**
	 * Adds a callback object, which is called if the window is closed.<br/>
	 * Callback should be replied with the instance-object of the dialog.
	 * 
	 * @param callable which should be called if the windows was closed
	 */
	public void setCallbackObject(ICallable callable);
}
