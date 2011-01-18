package de.osmembrane.model.pipeline;

import java.awt.Color;

import de.osmembrane.model.ModelProxy;

public enum ConnectorType {
	CHANGE ("Change", -1, new Color(255,0,0)),
	ENTITY ("Entity", -1, new Color(0,255,0)),
	DATASET ("Dataset", 1, new Color(0,0,255));
	
	private String friendlyName;
	private int maxConnections;
	private Color color;
	
	private ConnectorType(String friendlyName, int maxConnections, Color color) {
		this.friendlyName = friendlyName;
		this.maxConnections = maxConnections;
		this.color = color;
	}
	
	public String getFriendlyName() {
		return friendlyName;
	}
	
	public int getMaxConnections() {
		return maxConnections;
	}
	
	public Color getColor() {
		return color;
	}
	
	public static ConnectorType parseString(String type) {
		for (ConnectorType connectorType : ConnectorType.values()) {
			if (type.toLowerCase().equals(connectorType.toString().toLowerCase())) {
				return connectorType;
			}
		}
		
		return null;
	}
}
