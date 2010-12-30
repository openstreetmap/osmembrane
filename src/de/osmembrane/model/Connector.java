package de.osmembrane.model;

import java.util.ArrayList;
import java.util.List;

import de.osmembrane.model.Function;

public class Connector extends AbstractConnector {

	private List<Function> functions = new ArrayList<Function>();
	private int maxConnections;
	private String type;

	public Connector(int maxConnections, String type) {
		this.maxConnections = maxConnections;
		this.type = type;
	}
	
	public String getType() {
		return type;
	}

	public int getMaxConnections() {
		return maxConnections;
	}

	public AbstractFunction[] getConnections() {
		return (AbstractFunction[]) functions.toArray();
	}

	public void addConnection(AbstractFunction connection) {
		throw new UnsupportedOperationException();
	}

	public void deleteConnection(AbstractFunction connection) {
		throw new UnsupportedOperationException();
	}
}