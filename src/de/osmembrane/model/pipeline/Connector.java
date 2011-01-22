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
	private ConnectorPosition position;

	private XMLPipe xmlPipe;
	private AbstractFunction parent;

	/**
	 * Constructor for a new Connector with given {@link XMLPipe} and
	 * {@link AbstractFunction} as parent.
	 * 
	 * @param pipe
	 * @param parent
	 */
	public Connector(AbstractFunction parent, ConnectorPosition position,
			XMLPipe xmlPipe) {
		this.xmlPipe = xmlPipe;
		this.parent = parent;
		this.position = position;

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
		if (position == ConnectorPosition.IN) {
			return getType().getMaxInConnections();
		} else {
			return getType().getMaxOutConnections();
		}
	}

	@Override
	public boolean isFull() {
		return (connectors.size() >= getMaxConnections());
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
		return connectors.remove(connector);
	}

	@Override
	protected void unlink(boolean isOutConnector) {
		for (AbstractConnector connector : getConnections()) {
			AbstractFunction targetFunction = connector.getParent();
			if (isOutConnector) {
				this.getParent().removeConnectionTo(targetFunction);
			} else {
				targetFunction.removeConnectionTo(this.getParent());
			}
		}
	}

	@Override
	public Connector copy(CopyType type) {
		return copy(type, null);
	}

	@Override
	public Connector copy(CopyType type, AbstractFunction parent) {
		Connector newConnector = new Connector(this.parent, this.position, this.xmlPipe);
		
		if (parent != null) {
			newConnector.parent = parent;
		}
		
		if(type.copyConnections()) {
			newConnector.connectors.clear();
			for(AbstractConnector connector : this.connectors) {
				// TODO is here a copy to do or not?
				newConnector.connectors.add(connector);
			}
		}
		
		return newConnector;
	}

	@Override
	public String getIdentifier() {
		// TODO Auto-generated method stub
		return null;
	}
}