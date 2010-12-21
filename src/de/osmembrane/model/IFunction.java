package de.osmembrane.model;

import de.osmembrane.model.xml.XMLFunction;
import de.osmembrane.model.xml.XMLTask;
import java.io.Serializable;

public interface IFunction extends Serializable {

	public XMLFunction getXMLFunction();

	public XMLTask[] getAvailableTasks();

	public XMLTask getActiveTask();

	public void setActiveTask(XMLTask task);

	public Point getCoordinate();

	public void setCoordinate(Point coordinate);

	public Connector[] getInConnectors();

	public Connector[] getOutConnectors();
}