/*
 * This file is part of the OSMembrane project.
 * More informations under www.osmembrane.de
 * 
 * The project is licensed under the GNU GENERAL PUBLIC LICENSE 3.0.
 * for more details about the license see http://www.osmembrane.de/license/
 * 
 * Source: $HeadURL$ ($Revision$)
 * Last changed: $Date$
 */

package de.osmembrane.model.pipeline;

import java.io.ObjectStreamException;
import java.util.ArrayList;
import java.util.List;

import de.osmembrane.model.Identifier;
import de.osmembrane.model.ModelProxy;
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

    private AbstractFunction parent;

    transient private XMLPipe xmlPipe;
    private Identifier xmlPipeIdentifier;

    /**
     * Constructor for a new Connector with given {@link XMLPipe} and
     * {@link AbstractFunction} as parent.
     */
    public Connector(AbstractFunction parent, ConnectorPosition position,
            XMLPipe xmlPipe) {
        this.parent = parent;
        this.position = position;
        this.type = ConnectorType.parseString(xmlPipe.getType());
        this.xmlPipe = xmlPipe;

        /* set the identifier */
        AbstractFunctionPrototype afp = ModelProxy.getInstance().getFunctions();
        this.xmlPipeIdentifier = afp.getMatchingXMLPipeIdentifier(this.xmlPipe);
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
    public int getConnectorIndex() {
        return xmlPipe.getIndex().intValue();
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
            connector.removeConnection(this);
            this.removeConnection(connector);
        }
    }

    private Object readResolve() throws ObjectStreamException {
        AbstractFunctionPrototype afp = ModelProxy.getInstance().getFunctions();
        this.xmlPipe = afp.getMatchingXMLPipe(this.xmlPipeIdentifier);

        return this;
    }
}
