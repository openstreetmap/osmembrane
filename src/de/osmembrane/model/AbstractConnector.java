package de.osmembrane.model;

import java.io.Serializable;

public abstract class AbstractConnector implements Serializable {

	public abstract String getType();

	public abstract int getMaxConnections();

	public abstract AbstractFunction[] getConnections();

	public abstract void addConnection(AbstractFunction connection);

	public abstract void deleteConnection(AbstractFunction connection);
}