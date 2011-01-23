package de.osmembrane.model;

import java.io.Serializable;

public class Identifier implements Serializable {
	
	private static final long serialVersionUID = 2011012315340001L;
	
	private String identifier;
	
	public Identifier(String identifier) {
		this.identifier = identifier;
	}
	
	public String getIdentifier() {
		return identifier;
	}
}
