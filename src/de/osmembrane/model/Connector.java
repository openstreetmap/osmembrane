package de.osmembrane.model;

import java.util.Vector;
import de.osmembrane.model.Function;

public class Connector implements IConnector {
	public Vector<Function> _unnamed_Function_ = new Vector<Function>();

	public void getType() {
		throw new UnsupportedOperationException();
	}

	public void getMaxConnections() {
		throw new UnsupportedOperationException();
	}

	public void getConnections() {
		throw new UnsupportedOperationException();
	}

	public void addConnection(IFunction aConnection) {
		throw new UnsupportedOperationException();
	}

	public void deleteConnection(IFunction aConnection) {
		throw new UnsupportedOperationException();
	}
}