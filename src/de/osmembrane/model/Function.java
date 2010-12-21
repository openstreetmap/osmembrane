package de.osmembrane.model;

import de.osmembrane.model.xml.XMLTask;
import java.util.Vector;
import de.osmembrane.model.ConnectorList;
import de.osmembrane.model.Connector;
import de.osmembrane.model.xml.XMLFunction;

public class Function extends de.osmembrane.model.java.util.Observable implements IFunction {
	private Point _coordinate;
	private XMLTask[] _availableTasks;
	private Task _activeTask;
	private ConnectorList _inConnectors;
	private ConnectorList _outConnectors;
	public Connector _unnamed_Connector_;
	public FunctionGroup _unnamed_FunctionGroup_;
	public Vector<ConnectorList> _unnamed_ConnectorList_ = new Vector<ConnectorList>();
	public Vector<Connector> _unnamed_Connector_2 = new Vector<Connector>();

	public XMLFunction getXMLFunction() {
		throw new UnsupportedOperationException();
	}

	public XMLTask[] getAvailableTasks() {
		throw new UnsupportedOperationException();
	}

	public XMLTask getActiveTask() {
		throw new UnsupportedOperationException();
	}

	public void setActiveTask(XMLTask aTask) {
		throw new UnsupportedOperationException();
	}

	public Point getCoordinate() {
		throw new UnsupportedOperationException();
	}

	public void setCoordinate(Point aCoordinate) {
		throw new UnsupportedOperationException();
	}

	public Connector[] getInConnectors() {
		throw new UnsupportedOperationException();
	}

	public Connector[] getOutConnectors() {
		throw new UnsupportedOperationException();
	}
}