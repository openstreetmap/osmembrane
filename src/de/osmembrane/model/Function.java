package de.osmembrane.model;

import de.osmembrane.model.xml.XMLTask;
import java.util.ArrayList;
import de.osmembrane.model.Connector;
import de.osmembrane.model.xml.XMLFunction;
import java.util.Observable;

public class Function extends Observable implements IFunction {
	private Point coordinate;
	private XMLTask[] availableTasks;
	private Task activeTask;
	private List<Connector> inConnectors;
	private List<Connector> outConnectors;
	public Connector unnamed_Connector_;
	public FunctionGroup unnamed_FunctionGroup_;
	public ArrayList<Connector> unnamed_Connector_2 = new ArrayList<Connector>();

	public XMLFunction getXMLFunction() {
		throw new UnsupportedOperationException();
	}

	public XMLTask[] getAvailableTasks() {
		throw new UnsupportedOperationException();
	}

	public XMLTask getActiveTask() {
		throw new UnsupportedOperationException();
	}

	public void setActiveTask(XMLTask task) {
		throw new UnsupportedOperationException();
	}

	public Point getCoordinate() {
		throw new UnsupportedOperationException();
	}

	public void setCoordinate(Point coordinate) {
		throw new UnsupportedOperationException();
	}

	public Connector[] getInConnectors() {
		throw new UnsupportedOperationException();
	}

	public Connector[] getOutConnectors() {
		throw new UnsupportedOperationException();
	}
}