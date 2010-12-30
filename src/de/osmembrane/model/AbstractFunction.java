package de.osmembrane.model;

import de.osmembrane.model.xml.XMLHasDescription.Description;
import de.osmembrane.model.xml.XMLTask;

import java.awt.Point;
import java.io.Serializable;
import java.util.List;
import java.util.Observable;

/**
 * This represents a simple Function for the XML-Functions.
 * 
 * @author jakob_jarosch
 */
public abstract class AbstractFunction extends Observable implements
		Serializable {

	private static final long serialVersionUID = 2010123022230001L;

	/**
	 * Returns the parent of the current Function.
	 * 
	 * @return parent of the current Function
	 */
	public abstract AbstractFunctionGroup getParent();

	/**
	 * Returns the ID of the current Function.
	 * 
	 * @return ID of the current Function
	 */
	public abstract String getId();

	/**
	 * Returns a human readable name of the current Function.
	 * 
	 * @return human readable name of the current Function
	 */
	public abstract String getFriendlyName();

	/**
	 * Returns a List of Descriptions in available languages.
	 * 
	 * @return list of Descriptions in the available languages.
	 */
	public abstract List<Description> getDescription();

	/**
	 * Returns the available XMLTasks for the current Function.
	 * 
	 * @return available XMLTasks for the current Function
	 */
	public abstract XMLTask[] getAvailableTasks();

	/**
	 * Returns the currently active XMLTask for the actual Function.
	 * 
	 * @return active XMLTask for the Function
	 */
	public abstract XMLTask getActiveTask();

	/**
	 * Changes the active XMLTask to another XMLTask. Only XMLTasks of the own
	 * Function are allowed to set.
	 * 
	 * @param task
	 *            XMLTask which should be set as active
	 */
	public abstract void setActiveTask(XMLTask task);

	/**
	 * Returns the Coordinates in the Pipeline of the current Function.
	 * 
	 * @return Coordiantes of the current Function
	 */
	public abstract Point getCoordinate();

	/**
	 * Returns the In-Connectors of the current Function.
	 * 
	 * @return In-Connectors of the current Function
	 */
	public abstract Connector[] getInConnectors();

	/**
	 * Returns the Out-Connectors of the current Function.
	 * 
	 * @return Out-Connectors of the current Function
	 */
	public abstract Connector[] getOutConnectors();

	/**
	 * Compares the Function with another Function.
	 * 
	 * @param function
	 *            Function which should be compared
	 * @return true if the Functions equals (just the IDs are being compared).
	 *         false if they do not equal.
	 */
	public abstract boolean same(AbstractFunction function);
}