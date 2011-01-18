package de.osmembrane.model.pipeline;

import java.awt.Color;
import java.io.Serializable;

/**
 * This represents a simple FunctionGroup for the XML-Functions.
 * 
 * @author jakob_jarosch
 */
public abstract class AbstractFunctionGroup implements Serializable {

	private static final long serialVersionUID = 2010123022140001L;

	/**
	 * Returns the ID of the current FunctionGroup.
	 * 
	 * @return ID of the current FunctionGroup
	 */
	public abstract String getId();

	/**
	 * Returns a human readable name of the current FunctionGroup.
	 * 
	 * @return human readable name of the current FunctionGroup
	 */
	public abstract String getFriendlyName();

	/**
	 * Returns a List of Descriptions in available languages.
	 * 
	 * @return list of Descriptions in the available languages.
	 */
	public abstract String getDescription();
	
	/**
	 * Returns the color of the FunctionGroup.
	 * 
	 * @return color of the FunctionGroup
	 */
	public abstract Color getColor();

	/**
	 * Returns the Functions inherited by this FunctionGroup.
	 * 
	 * @return Functions inherited by the FunctionGroup
	 */
	public abstract AbstractFunction[] getFunctions();

	/**
	 * Compares the FunctionGroup with another FunctionGroup.
	 * 
	 * @param group
	 *            FunctionGroup which should be compared
	 * @return true if the Functions equals (just the IDs are being compared).
	 *         false if they do not equal.
	 */
	public abstract boolean same(AbstractFunctionGroup group);
}