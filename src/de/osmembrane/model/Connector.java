package de.osmembrane.model;

import java.util.ArrayList;
import java.util.List;

import de.osmembrane.model.xml.XMLPipe;

/**
 * Implements {@link AbstractConnector}
 * 
 * @author jakob_jarosch
 */
public class Connector extends AbstractConnector {

	private static final long serialVersionUID = 2011010722340001L;

	private List<AbstractConnector> connectors = new ArrayList<AbstractConnector>();
	private int maxConnections;
	private XMLPipe pipe;
	private AbstractFunction parent;

	/**
	 * Constructor for a new Connector with given {@link XMLPipe} and
	 * {@link AbstractFunction} as parent.
	 * 
	 * @param pipe
	 * @param parent
	 */
	public Connector(XMLPipe pipe, AbstractFunction parent) {
		this.pipe = pipe;
		this.parent = parent;

		if (pipe.getType().equals("entity") || pipe.getType().equals("change")) {
			this.maxConnections = -1;
		} else {
			this.maxConnections = 1;
		}

	}

	@Override
	public AbstractFunction getParent() {
		return parent;
	}

	@Override
	public XMLPipe getPipe() {
		return pipe;
	}

	@Override
	public int getMaxConnections() {
		return maxConnections;
	}

	@Override
	public boolean isFull() {
		return (maxConnections > 0 & connectors.size() >= maxConnections);
	}

	@Override
	public Connector[] getConnections() {
		Connector[] connectors = new Connector[this.connectors.size()];
		connectors = this.connectors.toArray(connectors);
		return connectors;
	}

	@Override
	public boolean addConnection(AbstractConnector connector) {
		/*
		 * check if the connector is not full and both connector-types does
		 * equal.
		 */
		if (!isFull()
				&& getPipe().getType().equals(connector.getPipe().getType())) {
			connectors.add(connector);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean removeConnection(AbstractConnector connector) {
		return connectors.remove(connectors);
	}
}