package de.osmembrane.model.pipeline;

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

	private FunctionGroup parent;
	
	private XMLFunction xmlFunction;
	
	private Point2D coordinate = new Point2D.Double();
	
	private List<Connector> inConnectors = new ArrayList<Connector>();
	private List<Connector> outConnectors = new ArrayList<Connector>();
	
	private List<Task> tasks = new ArrayList<Task>();
	private Task activeTask;

	private final String comparator;
	
	private Pipeline pipeline;

	/**
	 * State of the icon load sequence.
	 */
	private boolean triedLoadIcon;

	/**
	 * Icon
	 */
	private BufferedImage icon;

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
		
		for (XMLTask xmlTask : xmlFunction.getTask()) {
			Task task = new Task(xmlTask);
			task.addObserver(this);
			tasks.add(task);
		}

		/* set the first task as default */
		activeTask = getAvailableTasks()[0];

		/* create the connectors */
		createConnectors();

		/* create a simple comparator */
		comparator = this.xmlFunction.getId();
	}

	@Override
	public FunctionGroup getParent() {
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
		return xmlFunction.getFriendlyName();
	}

	@Override
	public String getDescription() {
		return I18N.getInstance().getDescription(xmlFunction);
	}

	@Override
	public BufferedImage getIcon() {
		if (triedLoadIcon = false) {
			try {
				icon = ImageIO.read(new File(xmlFunction.getIcon()));
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
		changedNotifyObservers(new PipelineObserverObject(ChangeType.CHANGE, this));
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
				if (connectorOut.getType() == connectorIn.getType()) {
					/* found equal Connectors */
					if (!connectorOut.isFull() && !connectorIn.isFull()) {
						
						/* first, add connections */
						connectorIn.addConnection(connectorOut);
						connectorOut.addConnection(connectorIn);
						
						/* now check loop freeness */
						if (getPipeline().hasLoop()) {
							/* remove 'cause that is not ok */
							removeConnectionTo(function);
							throw new ConnectorException(Type.LOOP_CREATED);
						}
						
						changedNotifyObservers(new PipelineObserverObject(ChangeType.CHANGE, this));
						
						return;
					} else {
						throw new ConnectorException(Type.FULL);
					}
				}
			}
		}
		
		throw new ConnectorException(Type.NO_MATCH);
	}

	@Override
	public boolean removeConnectionTo(AbstractFunction function) {
		for (AbstractConnector connectorOut : getOutConnectors()) {
			for (AbstractConnector connectorIn : function.getInConnectors()) {
				if (connectorOut.getType() == connectorIn.getType()) {
					/* found equal Connectors, remove connection */
					if(connectorIn.removeConnection(connectorOut) && connectorOut
							.removeConnection(connectorIn)) {
						changedNotifyObservers(new PipelineObserverObject(ChangeType.CHANGE, this));
						return true;
					}
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
			inConnectors.add(new Connector(this, pipe));
		}

		/* Out-Connectors */
		for (XMLPipe pipe : activeTask.getOutputXMLPipe()) {
			outConnectors.add(new Connector(this, pipe));
		}
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		/* get Updates from a Task (changed anything) */
		changedNotifyObservers(new PipelineObserverObject(ChangeType.CHANGE, this));
	}
}