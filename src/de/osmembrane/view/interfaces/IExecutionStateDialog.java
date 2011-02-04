package de.osmembrane.view.interfaces;

/**
 * Interface for {@link ExecutionStateDialog}.
 * 
 * @author jakob_jarosch
 */
public interface IExecutionStateDialog {
	
	/**
	 * Sets the shown status of the execution action.
	 * 
	 * @param status status to be set
	 */
	public void setStatus(String status);
	
	/**
	 * Sets the progress of a status bar.
	 * 
	 * @param progress 0 <= progress <= 100 to be set
	 */
	public void setProgress(int progress);
	
	/**
	 * Adds a line to the output.
	 * 
	 * @param outputLine line which should be added
	 */
	public void addOutputLine(String outputLine);
	
	/**
	 * Clears the contents of the window.
	 */
	public void clear();
	
	/**
	 * Adds a callback object, which is called if the window is closed.<br/>
	 * Callback should be replied with the instance-object of the dialog.
	 * 
	 * @param callable which should be called if the windows was closed
	 * @deprecated to be replaced with other function
	 */
	public void setCallbackObject();
}
