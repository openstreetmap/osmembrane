package de.osmembrane.model;

import de.osmembrane.model.xml.XMLFunction;
import de.osmembrane.model.xml.XMLTask;

public interface IFunction implements de.osmembrane.model.java.io.Serializable {

	public XMLFunction getXMLFunction();

	public XMLTask[] getAvailableTasks();

	public XMLTask getActiveTask();

	public void setActiveTask(XMLTask aTask);

	public Point getCoordinate();

	public void setCoordinate(Point aCoordinate);

	public Connector[] getInConnectors();

	public Connector[] getOutConnectors();
}