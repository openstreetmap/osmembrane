package de.osmembrane.model;

public interface IConnector implements de.osmembrane.model.java.io.Serializable {

	public void getType();

	public void getMaxConnections();

	public void getConnections();

	public void addConnection(IFunction aConnection);

	public void deleteConnection(IFunction aConnection);
}