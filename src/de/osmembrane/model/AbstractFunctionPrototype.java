package de.osmembrane.model;

import java.io.Serializable;
import java.util.Observable;

import de.osmembrane.model.pipeline.AbstractFunction;
import de.osmembrane.model.pipeline.AbstractFunctionGroup;

/**
 * This is an Prototype for accessing the XMLFunctions.
 * 
 * @author jakob_jarosch
 */
public abstract class AbstractFunctionPrototype extends Observable {

	/**
	 * Initiates the Prototype with a given compatible XML-file.
	 * 
	 * @param xmlFilename filename of the given XML-file
	 */
	public abstract void initiate(String xmlFilename);

	/**
	 * Gives an array of all {@link AbstractFunctionGroup}s back.
	 * 
	 * @return array of all {@link AbstractFunctionGroup}s
	 */
	public abstract AbstractFunctionGroup[] getFunctionGroups();

	/**
	 * Gives a copy untouched of the given {@link AbstractFunctionGroup} back.
	 * 
	 * @param group group which should be copied
	 * @return a copy with default values of the requested group
	 */
	public abstract AbstractFunctionGroup getFunctionGroup(AbstractFunctionGroup group);

	/**
	 * Gives a copy untouched of the given {@link AbstractFunction} back.
	 * 
	 * @param function function which should be copied
	 * @return a copy with default values of the requested function
	 */
	public abstract AbstractFunction getFunction(AbstractFunction function);
	
	/**
	 * Duplicates a {@link Serializable} object.
	 * 
	 * @return duplicated {@link Serializable} object
	 */
	public abstract Object duplicate(Serializable object);
}