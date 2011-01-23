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
	
	@Override
	public String toString() {
		return identifier.toString();
	}
	
	@Override
	public int hashCode() {
		return identifier.hashCode();
	}
	
	@Override
	public boolean equals(Object obj1) {
		if (obj1 instanceof Identifier) {
			Identifier ident = (Identifier) obj1;
			return identifier.equals(ident.getIdentifier());
		}
		return false;
	}
}
