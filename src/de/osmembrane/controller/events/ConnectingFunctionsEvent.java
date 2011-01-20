package de.osmembrane.controller.events;

import java.awt.event.ActionEvent;

import de.osmembrane.model.pipeline.AbstractFunction;

/**
 * Event for connecting two functions via their connectors (or deleting that
 * connection)
 * 
 * @author tobias_kuhn
 * 
 */
public class ConnectingFunctionsEvent extends ActionEvent {

	private static final long serialVersionUID = -9036269456329303714L;

	/**
	 * source of the connection
	 */
	private AbstractFunction connectionSource;

	/**
	 * destination of the connection
	 */
	private AbstractFunction connectionDestination;

	/**
	 * Creates a new connecting event. Source and destination do not necessarily
	 * represent the correct direction in the model.
	 * 
	 * @param source
	 *            object that has created the event
	 * @param connectionSource
	 *            source of the connection
	 * @param connectionDestination
	 *            destination of the connection
	 */
	public ConnectingFunctionsEvent(Object source,
			AbstractFunction connectionSource,
			AbstractFunction connectionDestination) {
		super(source, 0, null);
		this.connectionSource = connectionSource;
		this.connectionDestination = connectionDestination;
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the source of the connection
	 */
	public AbstractFunction getConnectionSource() {
		return this.connectionSource;
	}

	/**
	 * @return the destination of the connection
	 */
	public AbstractFunction getConnectionDestination() {
		return this.connectionDestination;
	}

}
