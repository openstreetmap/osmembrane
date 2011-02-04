package de.osmembrane.view.interfaces;

import de.osmembrane.controller.ICallable;

/**
 * Interface for {@link ExecutionStateDialog}.
 * 
 * @author jakob_jarosch
 */
public interface IExecutionStateDialog {
	
	public void addLine(String outputLine);
	
	public void dispose();
	
	public void clear();
	
	public void setCallbackObject(ICallable callable);
}
