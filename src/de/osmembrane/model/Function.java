package de.osmembrane.model;

import de.osmembrane.model.xml.XMLHasDescription;
import de.osmembrane.model.xml.XMLParameter;
import de.osmembrane.model.xml.XMLPipe;
import de.osmembrane.model.xml.XMLTask;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import de.osmembrane.model.Connector;
import de.osmembrane.model.xml.XMLFunction;

/**
 * This represents the implementation of a simple Function for the
 * XML-Functions.
 * 
 * @author jakob_jarosch
 */
public class Function extends AbstractFunction {

	private static final long serialVersionUID = 2010123022380001L;

	private XMLFunction xmlFunction;
	private FunctionGroup parent;
	private Point coordinate = new Point();
	private XMLTask activeTask;

	private List<Connector> inConnectors = new ArrayList<Connector>();
	private List<Connector> outConnectors = new ArrayList<Connector>();

	private final String comparator;

	/**
	 * Creates a new Function with given parent and XMLFunction.
	 * 
	 * @param parent
	 *            parent of the Function
	 * @param xmlFunction
	 *            XMLFunction belongs to this Function
	 */
	public Function(FunctionGroup parent, XMLFunction xmlFunction) {
		this.xmlFunction = xmlFunction;
		this.parent = parent;

		/* set the first task as default */
		activeTask = xmlFunction.getTask().get(0);

		/* create the connectors */
		createConnectors();

		/* create a simple comparator */
		comparator = this.xmlFunction.getId();
	}

	@Override
	public AbstractFunctionGroup getParent() {
		return parent;
	}

	@Override
	public String getId() {
		return xmlFunction.getId();
	}

	@Override
	public String getFriendlyName() {
		return xmlFunction.getFriendlyName();
	}

	@Override
	public XMLHasDescription getDescription() {
		return xmlFunction;
	}

	@Override
	public XMLTask[] getAvailableTasks() {
		XMLTask[] returnTasks = new XMLTask[xmlFunction.getTask().size()];
		return xmlFunction.getTask().toArray(returnTasks);
	}

	@Override
	public XMLTask getActiveTask() {
		return activeTask;
	}

	@Override
	public void setActiveTask(XMLTask newTask) {
		/* only allow a correct task to be set as active */
		for (XMLTask task : xmlFunction.getTask()) {
			/* should be the same instance of the task */
			if (task == newTask) {
				/*
				 * found the new active XMLTask, so copy the settings to the new
				 * Task.
				 */
				for (XMLParameter oldParam : activeTask.getParameter()) {
					for (XMLParameter newParam : task.getParameter()) {
						if (oldParam.getName().equals(newParam.getName())
								&& oldParam.getType()
										.equals(newParam.getType())) {
							/* both parameters match, set the value */
							newParam.setValue(oldParam.getValue());
						}
					}
				}

				activeTask = task;
				return;
			}
		}
	}

	@Override
	public Point getCoordinate() {
		return coordinate;
	}

	@Override
	public Connector[] getInConnectors() {
		Connector[] inConnectors = new Connector[this.inConnectors.size()]; 
		inConnectors = this.inConnectors.toArray(inConnectors);
		return inConnectors;
	}

	@Override
	public Connector[] getOutConnectors() {
		Connector[] outConnectors = new Connector[this.outConnectors.size()]; 
		outConnectors = this.outConnectors.toArray(outConnectors);
		return outConnectors;
	}

	@Override
	public boolean same(AbstractFunction function) {
		if (function instanceof Function) {
			Function oFG = (Function) function;
			return (oFG.getComparatorString()
					.equals(this.getComparatorString()));
		}

		return false;
	}

	@Override
	public void addConnectionTo(AbstractFunction function)
			throws ConnectorException {
		for (AbstractConnector connectorOut : getOutConnectors()) {
			for (AbstractConnector connectorIn : function.getInConnectors()) {
				if (connectorOut.getPipe().getType()
						.equals(connectorIn.getPipe().getType())) {
					/* found equal Connectors */
					if (!connectorOut.isFull() && !connectorIn.isFull()) {
						connectorIn.addConnection(connectorOut);
						connectorOut.addConnection(connectorIn);
						return;
					} else {
						throw new ConnectorException(Problem.FULL);
					}
				}
			}
		}
		
		throw new ConnectorException(Problem.NO_MATCH);
	}

	@Override
	public boolean removeConnectionTo(AbstractFunction function) {
		for (AbstractConnector connectorOut : getOutConnectors()) {
			for (AbstractConnector connectorIn : function.getInConnectors()) {
				if (connectorOut.getPipe().getType()
						.equals(connectorIn.getPipe().getType())) {
					/* found equal Connectors, remove connection */
					return (connectorIn.removeConnection(connectorOut) && connectorOut
							.removeConnection(connectorIn));
				}
			}
		}

		return false;
	}

	protected String getComparatorString() {
		return comparator;
	}

	/**
	 * Creates the connectors for the active XMLTask.
	 */
	private void createConnectors() {
		/* In-Connectors */
		for (XMLPipe pipe : activeTask.getInputXMLPipe()) {
			inConnectors.add(new Connector(pipe, this));
		}

		/* Out-Connectors */
		for (XMLPipe pipe : activeTask.getOutputXMLPipe()) {
			outConnectors.add(new Connector(pipe, this));
		}
	}
}