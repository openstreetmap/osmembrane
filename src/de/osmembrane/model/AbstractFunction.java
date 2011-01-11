package de.osmembrane.model;

import de.osmembrane.model.xml.XMLHasDescription;
import de.osmembrane.model.xml.XMLTask;

import java.awt.Point;
import java.io.Serializable;
import java.util.Observable;
import java.util.Observer;

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
	 * Returns the Description of the {@link AbstractFunction} .
	 * 
	 * @return a {@link XMLHasDescription} object
	 */
	public abstract XMLHasDescription getDescription();

	/**
	 * Returns the available XMLTasks for the current Function.
	 * 
	 * @return available XMLTasks for the current Function
	 */
	public abstract XMLTask[] getAvailableTasks();

	/**
	 * Returns the currently active XMLTask for the actual Function.
	 * 
	 * If any changes are applied to the XMLTask call
	 * {@link AbstractFunction#setChanged()()} and
	 * {@link AbstractFunction#notifyObservers()}.
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
	public abstract AbstractConnector[] getInConnectors();

	/**
	 * Returns the Out-Connectors of the current Function.
	 * 
	 * @return Out-Connectors of the current Function
	 */
	public abstract AbstractConnector[] getOutConnectors();

	/**
	 * Creates a connection to the given {@link AbstractFunction}.
	 * 
	 * @param connection
	 *            to which the connection should be created.
	 * 
	 * @throws ConnectorException
	 *             when the given function is not compatible or all
	 *             {@link AbstractConnector}s are full.
	 */
	public abstract void addConnectionTo(AbstractFunction function)
			throws ConnectorException;

	/**
	 * Removes a connection between the current and the given
	 * {@link AbstractFunction}.
	 * 
	 * @param function
	 *            which connection should be removed
	 * @return true if there was a connection
	 */
	public abstract boolean removeConnectionTo(AbstractFunction function);

	/**
	 * Compares the Function with another Function.
	 * 
	 * @param function
	 *            Function which should be compared
	 * @return true if the Functions equals (just the IDs are being compared).
	 *         false if they do not equal.
	 */
	public abstract boolean same(AbstractFunction function);

	/**
	 * Notifies all registered {@link Observer}s with pre-called {@link Observable#setChanged())}.
	 */
	public void changedNotifyObservers() {
		this.setChanged();
		this.notifyObservers();
	}
	
	/* TODO implement auto-inform Pipeline-Observer */
}