package de.osmembrane.model;

import java.io.Serializable;

public abstract class AbstractConnector implements Serializable {

	public abstract void getType();

	public abstract void getMaxConnections();

	public abstract void getConnections();

	public abstract void addConnection(AbstractFunction connection);

	public abstract void deleteConnection(AbstractFunction connection);
}