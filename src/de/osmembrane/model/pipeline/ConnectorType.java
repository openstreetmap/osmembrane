package de.osmembrane.model.pipeline;

public enum ConnectorType {
	CHANGE ("Change", -1),
	ENTITY ("Entity", -1),
	DATASET ("Dataset", 1);
	
	private String friendlyName;
	private int maxConnections;
	
	
	private ConnectorType(String friendlyName, int maxConnections) {
		this.friendlyName = friendlyName;
		this.maxConnections = maxConnections;
	}
	
	public String getFriendlyName() {
		return friendlyName;
	}
	
	public int getMaxConnections() {
		return maxConnections;
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
