package de.osmembrane.model.pipeline;

import java.util.ArrayList;
import java.util.List;

import de.osmembrane.model.xml.XMLPipe;
import de.osmembrane.tools.I18N;

/**
 * Implements {@link AbstractConnector}
 * 
 * @author jakob_jarosch
 */
public class Connector extends AbstractConnector {

	private static final long serialVersionUID = 2011010722340001L;

	private List<AbstractConnector> connectors = new ArrayList<AbstractConnector>();
	private ConnectorType type;

	private XMLPipe xmlPipe;
	private AbstractFunction parent;

	/**
	 * Constructor for a new Connector with given {@link XMLPipe} and
	 * {@link AbstractFunction} as parent.
	 * 
	 * @param pipe
	 * @param parent
	 */
	public Connector(AbstractFunction parent, XMLPipe pipe) {
		this.xmlPipe = pipe;
		this.parent = parent;

		this.type = ConnectorType.parseString(xmlPipe.getType());
	}

	@Override
	public AbstractFunction getParent() {
		return parent;
	}

	@Override
	public String getDescription() {
		return I18N.getInstance().getDescription(xmlPipe);
	}

	@Override
	public ConnectorType getType() {
		return type;
	}

	@Override
	public int getMaxConnections() {
		return this.type.getMaxConnections();
	}

	@Override
	public boolean isFull() {
		return (getMaxConnections() > 0 && connectors.size() >= getMaxConnections());
	}

	@Override
	public Connector[] getConnections() {
		Connector[] connectors = new Connector[this.connectors.size()];
		connectors = this.connectors.toArray(connectors);
		return connectors;
	}

	@Override
	protected boolean addConnection(AbstractConnector connector) {
		/*
		 * check if the connector is not full and both connector-types does
		 * equal.
		 */
		if (!isFull() && getType() == connector.getType()) {
			connectors.add(connector);
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected boolean removeConnection(AbstractConnector connector) {
		return connectors.remove(connectors);
	}
}