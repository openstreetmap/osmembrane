package de.osmembrane.model.pipeline;

public enum ParameterType {
	INT ("Integer"),
	STRING ("String"),
	BOOLEAN ("Boolean"),
	ENUM ("Enumeration"),
	FILENAME ("Filename"),
	DIRECTORY ("Directory"),
	URI ("URI"),
	INSTANT ("Instant"),
	BBOX ("BoundingBox");
	
	private String friendlyName;
	
	
	private ParameterType(String friendlyName) {
		this.friendlyName = friendlyName;
	}
	
	public String getFriendlyName() {
		return friendlyName;
	}
	
	public static ParameterType parseString(String type) {
		for (ParameterType paramType : ParameterType.values()) {
			if (type.toLowerCase().equals(paramType.toString().toLowerCase())) {
				return paramType;
			}
		}
		
		return null;
	}
}
