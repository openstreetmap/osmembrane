package de.osmembrane.model.pipeline;

import de.osmembrane.model.pipeline.AbstractConnector.ConnectorPosition;
import de.osmembrane.model.pipeline.Connector;
import de.osmembrane.model.pipeline.ConnectorException.Type;
import de.osmembrane.model.pipeline.PipelineObserverObject.ChangeType;
import de.osmembrane.model.xml.XMLPipe;
import de.osmembrane.model.xml.XMLTask;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import javax.imageio.ImageIO;

import de.osmembrane.model.xml.XMLFunction;
import de.osmembrane.tools.I18N;

/**
 * This represents the implementation of a simple Function for the
 * XML-Functions.
 * 
 * @author jakob_jarosch
 */
public class Function extends AbstractFunction {

	private static final long serialVersionUID = 2010123022380001L;

	private AbstractFunctionGroup parent;
	private String parentIdentifier;

	transient private Pipeline pipeline;
	private String pipelineIdentifier;

	private XMLFunction xmlFunction;
	private String xmlFunctionIdentifier;

	private Point2D coordinate = new Point2D.Double();

	private List<Connector> inConnectors = new ArrayList<Connector>();
	private List<Connector> outConnectors = new ArrayList<Connector>();

	private List<Task> tasks = new ArrayList<Task>();
	private Task activeTask;

	/**
	 * State of the icon load sequence.
	 */
	private boolean triedLoadIcon = false;

	/**
	 * Icon of the function.
	 */
	private BufferedImage icon = null;

	/**
	 * Creates a new Function with given parent and XMLFunction.
	 * 
	 * @param parent
	 *            parent of the Function
	 * @param xmlFunction
	 *            XMLFunction belongs to this Function
	 */
	public Function(AbstractFunctionGroup parent, XMLFunction xmlFunction) {
		this.xmlFunction = xmlFunction;
		this.parent = parent;

		for (XMLTask xmlTask : xmlFunction.getTask()) {
			Task task = new Task(xmlTask);
			task.addObserver(this);
			tasks.add(task);
		}

		/* set the first task as default */
		activeTask = getAvailableTasks()[0];

		/* create the connectors */
		createConnectors();
	}

	@Override
	public AbstractFunctionGroup getParent() {
		return parent;
	}

	@Override
	protected void setPipeline(Pipeline pipeline) {
		this.pipeline = pipeline;
	}

	@Override
	public AbstractPipeline getPipeline() {
		return pipeline;
	}

	@Override
	public String getId() {
		return xmlFunction.getId();
	}

	@Override
	public String getFriendlyName() {
		/* fallback if friendlyName is not available */
		if (xmlFunction.getFriendlyName() == null) {
			return getId();
		}

		return xmlFunction.getFriendlyName();
	}

	@Override
	public String getDescription() {
		return I18N.getInstance().getDescription(xmlFunction);
	}

	@Override
	public BufferedImage getIcon() {
		if (triedLoadIcon == false) {
			try {
				if (xmlFunction.getIcon() != null) {
					icon = ImageIO.read(new File(xmlFunction.getIcon()));
				}
			} catch (IOException e) {
				icon = null;
			}
			triedLoadIcon = true;
		}

		return icon;
	}

	@Override
	public Task[] getAvailableTasks() {
		Task[] returnTasks = new Task[tasks.size()];
		return tasks.toArray(returnTasks);
	}

	@Override
	public AbstractTask getActiveTask() {
		return activeTask;
	}

	@Override
	public void setActiveTask(AbstractTask newTask) {
		/* only allow a correct task to be set as active */
		for (Task task : getAvailableTasks()) {
			/* should be the same instance of the task */
			if (task == newTask) {
				/*
				 * found the new active XMLTask, so copy the settings to the new
				 * Task.
				 */
				for (AbstractParameter oldParam : activeTask.getParameters()) {
					for (AbstractParameter newParam : task.getParameters()) {
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
	public Point2D getCoordinate() {
		return coordinate;
	}

	@Override
	public void setCoordinate(Point2D coordinate) {
		this.coordinate = coordinate;
		changedNotifyObservers(new PipelineObserverObject(
				ChangeType.CHANGE_FUNCTION, this));
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
			return (oFG.getIdentifier()
					.equals(this.getIdentifier()));
		}

		return false;
	}

	@Override
	public void addConnectionTo(AbstractFunction function)
			throws ConnectorException {
		boolean foundFullOne = false;

		for (AbstractConnector connectorOut : getOutConnectors()) {
			for (AbstractConnector connectorIn : function.getInConnectors()) {
				if (connectorOut.getType() == connectorIn.getType()) {
					/* found equal Connectors */
					if (!connectorOut.isFull() && !connectorIn.isFull()) {

						/*
						 * check if already a connection between these two
						 * function exists
						 */
						for (AbstractConnector con : connectorOut
								.getConnections()) {
							if (con == connectorIn) {
								throw new ConnectorException(
										Type.CONNECTION_ALREADY_EXISTS);
							}
						}

						/* first, add connections */
						connectorIn.addConnection(connectorOut);
						connectorOut.addConnection(connectorIn);

						/* now check loop freeness */
						if (getPipeline().hasLoop()) {
							/* remove 'cause that is not ok */
							removeConnectionTo(function);
							throw new ConnectorException(Type.LOOP_CREATED);
						}

						changedNotifyObservers(new PipelineObserverObject(
								ChangeType.ADD_CONNECTION, connectorOut,
								connectorIn));

						return;
					} else {
						foundFullOne = true;
					}
				}
			}
		}

		if (foundFullOne) {
			throw new ConnectorException(Type.FULL);
		} else {
			throw new ConnectorException(Type.NO_MATCH);
		}
	}

	@Override
	public boolean removeConnectionTo(AbstractFunction function) {
		for (AbstractConnector connectorOut : getOutConnectors()) {
			for (AbstractConnector connectorIn : function.getInConnectors()) {
				if (connectorOut.getType() == connectorIn.getType()) {
					/* found equal Connectors, remove connection */
					boolean inRemove = connectorIn
							.removeConnection(connectorOut);
					boolean outRemove = connectorOut
							.removeConnection(connectorIn);
					if (inRemove && outRemove) {
						changedNotifyObservers(new PipelineObserverObject(
								ChangeType.DELETE_CONNECTION, connectorOut,
								connectorIn));
						return true;
					}
				}
			}
		}

		return false;
	}

	@Override
	protected void unlinkConnectors() {
		for (AbstractConnector outConnector : getOutConnectors()) {
			outConnector.unlink(true);
		}
		for (AbstractConnector inConnector : getInConnectors()) {
			inConnector.unlink(false);
		}
	}

	public String getIdentifier() {
		return this.xmlFunction.getId();
	}
	
	/**
	 * Creates the connectors for the active XMLTask.
	 */
	private void createConnectors() {
		/* In-Connectors */
		for (XMLPipe pipe : activeTask.getInputPipe()) {
			inConnectors.add(new Connector(this, ConnectorPosition.IN, pipe));
		}

		/* Out-Connectors */
		for (XMLPipe pipe : activeTask.getOutputPipe()) {
			outConnectors.add(new Connector(this, ConnectorPosition.OUT, pipe));
		}
	}

	@Override
	protected void changedNotifyObservers(PipelineObserverObject poo) {
		this.setChanged();
		this.notifyObservers(poo);

		/* now we have to notify the observer of the pipeline */
		if (getPipeline() != null) {
			getPipeline().changedNotifyObservers(poo);
		}
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		/* get Updates from a Task (changed anything) */
		changedNotifyObservers(new PipelineObserverObject(
				ChangeType.CHANGE_FUNCTION, this));
	}
	
	@Override
	public Function copy(CopyType type) {
		return copy(type, null);
	}
	
	@Override
	public Function copy(CopyType type, AbstractFunctionGroup parent) {
		Function newFunction = new Function(this.parent, this.xmlFunction);
		
		newFunction.pipeline = this.pipeline;
		newFunction.coordinate = this.coordinate;
		
		/* copy the parent if it is a new one */
		if(parent != null) {
			newFunction.parent = parent;
		}
		
		/* copy the tasks */
		newFunction.tasks.clear();
		for(Task task : this.tasks) {
			Task newTask = task.copy(type);
			newFunction.tasks.add(newTask);
			
			if (task == activeTask && type.copyValues()) {
				newFunction.activeTask = newTask;
			}
		}
		
		/* coyp the connectors */
		newFunction.inConnectors.clear();
		for (Connector inCon : inConnectors) {
			newFunction.inConnectors.add(inCon.copy(type, newFunction));
		}
		newFunction.outConnectors.clear();
		for (Connector outCon : outConnectors) {
			newFunction.outConnectors.add(outCon.copy(type, newFunction));
		}
		
		return newFunction;
	}
}