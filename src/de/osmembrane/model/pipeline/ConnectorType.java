package de.osmembrane.model.pipeline;

import java.awt.Color;

/**
 * Type of the {@link AbstractConnector}.
 * 
 * @author jakob_jarosch
 */
public enum ConnectorType {
	/**
	 * Stream-type "change".
	 */
	CHANGE("Change", -1, new Color(255, 0, 0)),

	/**
	 * Stream-type "entity".
	 */
	ENTITY("Entity", -1, new Color(0, 255, 0)),

	/**
	 * Stream-type "dataset".
	 */
	DATASET("Dataset", 1, new Color(0, 0, 255));

	/**
	 * Friendly name for connector type.
	 */
	private String friendlyName;

	/**
	 * Maximum connections for the connector type.<br/>
	 * -1 represents infinity.
	 */
	private int maxConnections;

	/**
	 * Color of the connector type.
	 */
	private Color color;

	/**
	 * Creates the ConnectorType.
	 * 
	 * @param friendlyName
	 *            human readable name of the connector type
	 * @param maxConnections
	 *            maximum connections count for the connector type
	 * @param color
	 *            for the connector type
	 */
	private ConnectorType(String friendlyName, int maxConnections, Color color) {
		this.friendlyName = friendlyName;
		this.maxConnections = maxConnections;
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
	 * Returns the maximum count of connections for the connector type.
	 * 
	 * @return -1 for infinity
	 */
	public int getMaxConnections() {
		return maxConnections;
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
	 * @param type type as string
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
