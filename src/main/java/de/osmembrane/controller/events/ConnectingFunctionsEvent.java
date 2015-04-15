/*
 * This file is part of the OSMembrane project.
 * More informations under www.osmembrane.de
 * 
 * The project is licensed under the GNU GENERAL PUBLIC LICENSE 3.0.
 * for more details about the license see http://www.osmembrane.de/license/
 * 
 * Source: $HeadURL$ ($Revision$)
 * Last changed: $Date$
 */

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
