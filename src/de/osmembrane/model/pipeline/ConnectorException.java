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

package de.osmembrane.model.pipeline;

/**
 * Exception for reporting a failed connection between two
 * {@link AbstractFunction}s.
 * 
 * @author jakob_jarosch
 */
public class ConnectorException extends Exception {

    private static final long serialVersionUID = 2011010722360001L;

    /**
     * Type of the connection failure.
     */
    public enum Type {
        /**
         * There are no matching Connectors on both functions.
         */
        NO_MATCH,

        /**
         * One or both function have only full, matching connectors.
         */
        FULL,

        /**
         * The connection would create a loop, what should never be done.
         */
        LOOP_CREATED,

        /**
         * A connection between these functions does already exists.
         */
        CONNECTION_ALREADY_EXISTS
    }

    private Type type;

    /**
     * Creates a new {@link ConnectorException} with a given {@link Type}.
     * 
     * @param type
     */
    public ConnectorException(Type type) {
        this.type = type;
    }

    /**
     * Returns the {@link Type} of the {@link ConnectorException}.
     * 
     * @return {@link Type} of the {@link ConnectorException}
     */
    public Type getType() {
        return type;
    }
}
