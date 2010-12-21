package de.osmembrane.model;

import java.io.Serializable;

public interface IConnector extends Serializable {

	public void getType();

	public void getMaxConnections();

	public void getConnections();

	public void addConnection(IFunction connection);

	public void deleteConnection(IFunction connection);
}