package de.osmembrane.model;

import de.osmembrane.model.xml.XMLFunction;
import de.osmembrane.model.xml.XMLTask;

import java.awt.Point;
import java.io.Serializable;
import java.util.Observable;

public abstract class AbstractFunction extends Observable implements Serializable {

	public abstract XMLFunction getXMLFunction();

	public abstract XMLTask[] getAvailableTasks();

	public abstract XMLTask getActiveTask();

	public abstract void setActiveTask(XMLTask task);

	public abstract Point getCoordinate();

	public abstract void setCoordinate(Point coordinate);

	public abstract Connector[] getInConnectors();

	public abstract Connector[] getOutConnectors();
}