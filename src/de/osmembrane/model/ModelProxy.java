package de.osmembrane.model;

import java.util.Observable;
import java.util.Observer;

/**
 * The ModelProxy is the connection to the whole Model of OSMembrane.
 * A instance can got over {@see ModelProxy#getInstance()}.
 * 
 * @author jakob_jarosch
 */
public class ModelProxy extends Observable implements Observer {
	
	private ISettings settings;
	private IPipeline pipeline;
	private IFunctionPrototype functionPrototype;
	
	/**
	 * Implements the Singleton pattern.
	 */
	private static ModelProxy instance = new ModelProxy();
	
	/**
	 * Initiates the ModelProxy.
	 */
	private ModelProxy() {
		
	}
	
	/**
	 * Getter for the Singleton pattern.
	 * 
	 * @return the one and only instance of ModelProxy
	 */
	public static ModelProxy getInstance() {
		return instance;
	}
	
	/**
	 * Returns the {@link IPipeline}.
	 * 
	 * @return give back the active {@link IPipeline}
	 */
	public IPipeline accessPipeline() {
		return pipeline;
	}

	/**
	 * Returns the {@link ISettings}.
	 * 
	 * @return give back the active {@link ISettings}
	 */
	public ISettings accessSettings() {
		return settings;
	}

	/**
	 * Returns the {@link IFunctionPrototype}.
	 * 
	 * @return give back the active {@link IFunctionPrototype}
	 */
	public IFunctionPrototype accessFunctions() {
		return functionPrototype;
	}

	@Override
	public void update(Observable o, Object arg) {
		throw new UnsupportedOperationException();
	}
}