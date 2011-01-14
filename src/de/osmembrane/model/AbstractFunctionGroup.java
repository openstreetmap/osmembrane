package de.osmembrane.model;

import java.io.Serializable;
import java.util.List;

import de.osmembrane.model.xml.XMLHasDescription;
import de.osmembrane.model.xml.XMLHasDescription.Description;

/**
 * This represents a simple FunctionGroup for the XML-Functions.
 * 
 * @author jakob_jarosch
 */
public abstract class AbstractFunctionGroup extends XMLHasDescription implements Serializable {

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
	public abstract List<Description> getDescription();

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