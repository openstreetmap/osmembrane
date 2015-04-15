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

import java.awt.Color;

import de.osmembrane.resources.Constants;

/**
 * Type of the {@link AbstractConnector}.
 * 
 * @author jakob_jarosch
 */
public enum ConnectorType {
    /**
     * Stream-type "change".
     */
    CHANGE("Change", 1, Integer.MAX_VALUE, Constants.CHANGE_STREAM_TYPE_COLOR),

    /**
     * Stream-type "entity".
     */
    ENTITY("Entity", 1, Integer.MAX_VALUE, Constants.ENTITY_STREAM_TYPE_COLOR),

    /**
     * Stream-type "dataset".
     */
    DATASET("Dataset", 1, 1, Constants.DATASET_STREAM_TYPE_COLOR);

    /**
     * Friendly name for connector type.
     */
    private String friendlyName;

    /**
     * Maximum connections for the connector type.
     */
    private int maxInConnections;
    private int maxOutConnections;

    /**
     * Color of the connector type.
     */
    private Color color;

    /**
     * Creates the ConnectorType.
     * 
     * @param friendlyName
     *            human readable name of the connector type
     * @param maxInConnections
     *            maximum in-connections count for the connector type
     * @param color
     *            for the connector type
     */
    private ConnectorType(String friendlyName, int maxInConnections,
            int maxOutConnections, Color color) {
        this.friendlyName = friendlyName;
        this.maxInConnections = maxInConnections;
        this.maxOutConnections = maxOutConnections;
        this.color = color;
    }

    /**
     * Returns the human readable name of the connector type.
     * 
     * @return human readable name
     */
    public String getFriendlyName() {
        return friendlyName;
    }

    /**
     * Returns the maximum count of in-connections for the connector type.
     * 
     * @return maximum connection count
     */
    protected int getMaxInConnections() {
        return maxInConnections;
    }

    /**
     * Returns the maximum count of out-connections for the connector type.
     * 
     * @return maximum connection count
     */
    protected int getMaxOutConnections() {
        return maxOutConnections;
    }

    /**
     * Returns the color of the connector type.
     * 
     * @return color of the connector type
     */
    public Color getColor() {
        return color;
    }

    /**
     * Parses a {@link String} to a {@link ConnectorType}.
     * 
     * @param type
     *            type as string
     * @return connector type or NULL if no type was found
     */
    public static ConnectorType parseString(String type) {
        for (ConnectorType connectorType : ConnectorType.values()) {
            if (type.toLowerCase().equals(
                    connectorType.toString().toLowerCase())) {
                return connectorType;
            }
        }

        return null;
    }
}
