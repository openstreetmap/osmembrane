package de.osmembrane.controller;

/**
 * Like a very simple observer.
 * 
 * @author jakob_jarosch
 */
public interface ICallable {

	/**
	 * Interacts like a observer.
	 * 
	 * @param obj the object which called this method
	 */
	public void callback(Object obj);
}
