package de.osmembrane.model;

import java.util.Observable;
import java.util.Observer;

import de.osmembrane.model.pipeline.AbstractPipeline;
import de.osmembrane.model.pipeline.Pipeline;

/**
 * The ModelProxy is the connection to the whole Model of OSMembrane.
 * A instance can got over {@see ModelProxy#getInstance()}.
 * 
 * @author jakob_jarosch
 */
public class ModelProxy extends Observable implements Observer {
	
	private AbstractSettings settings;
	private AbstractPipeline pipeline;
	private AbstractFunctionPrototype functionPrototype;
	
	/**
	 * Implements the Singleton pattern.
	 */
	private static ModelProxy instance = new ModelProxy();
	
	/**
	 * Initiates the ModelProxy.
	 */
	private ModelProxy() {
		settings = new Settings();
		pipeline = new Pipeline();
		functionPrototype = new FunctionPrototype();
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
	 * Returns the {@link AbstractPipeline}.
	 * 
	 * @return give back the active {@link AbstractPipeline}
	 */
	public AbstractPipeline accessPipeline() {
		return pipeline;
	}

	/**
	 * Returns the {@link AbstractSettings}.
	 * 
	 * @return give back the active {@link AbstractSettings}
	 */
	public AbstractSettings accessSettings() {
		return settings;
	}

	/**
	 * Returns the {@link AbstractFunctionPrototype}.
	 * 
	 * @return give back the active {@link AbstractFunctionPrototype}
	 */
	public AbstractFunctionPrototype accessFunctions() {
		return functionPrototype;
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o.hasChanged()) {
			setChanged();
		}
		
		notifyObservers(arg);
	}
}