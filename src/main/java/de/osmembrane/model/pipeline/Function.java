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

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectStreamException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import javax.imageio.ImageIO;

import de.osmembrane.model.Identifier;
import de.osmembrane.model.ModelProxy;
import de.osmembrane.model.pipeline.AbstractConnector.ConnectorPosition;
import de.osmembrane.model.pipeline.ConnectorException.Type;
import de.osmembrane.model.pipeline.PipelineObserverObject.ChangeType;
import de.osmembrane.model.settings.SettingType;
import de.osmembrane.model.xml.XMLFunction;
import de.osmembrane.model.xml.XMLPipe;
import de.osmembrane.model.xml.XMLTask;
import de.osmembrane.tools.I18N;

/**
 * This represents the implementation of a simple Function for the
 * XML-Functions.
 * 
 * @author jakob_jarosch
 */
public class Function extends AbstractFunction {

    private static final long serialVersionUID = 2010123022380001L;

    transient private Pipeline pipeline;

    transient private AbstractFunctionGroup parent;
    private Identifier parentIdentifier;

    transient private XMLFunction xmlFunction;
    private Identifier xmlFunctionIdentifier;

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
        this.parent = parent;
        this.xmlFunction = xmlFunction;

        /* set the identifiers */
        AbstractFunctionPrototype afp = ModelProxy.getInstance().getFunctions();
        this.parentIdentifier = this.parent.getIdentifier();
        this.xmlFunctionIdentifier = afp
                .getMatchingXMLFunctionIdentifier(this.xmlFunction);

        /* add task observers */
        for (XMLTask xmlTask : xmlFunction.getTask()) {
            Task task = new Task(this, xmlTask);
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
                            /*
                             * Now check if the oldParam is the default value,
                             * if that is so, don't copy.
                             */
                            if (oldParam.getValue() != null
                                    && !oldParam.isDefaultValue()) {
                                /* oldParam has a real non-default value. */
                                newParam.setValue(oldParam.getValue());
                            }

                        }
                    }

                }
                activeTask = task;

                changedNotifyObservers(new PipelineObserverObject(
                        ChangeType.CHANGE_FUNCTION, this));

                return;
            }
        }
    }

    @Override
    public boolean isComplete() {
        /* check the params */
        for (AbstractParameter param : getActiveTask().getParameters()) {
            if (!param.isValid()) {
                return false;
            }
        }

        /* check the in connectors */
        for (AbstractConnector connector : getInConnectors()) {
            if (connector.getConnections().length == 0) {
                return false;
            }
        }

        /* check the out connectors */
        for (AbstractConnector connector : getOutConnectors()) {
            if (connector.getConnections().length == 0) {
                return false;
            }
        }

        /* everything seems to be ok, return true */
        return true;
    }

    @Override
    public Point2D getCoordinate() {
        Integer rasterInt = (Integer) ModelProxy.getInstance().getSettings()
                .getValue(SettingType.PIPELINE_RASTER_SIZE);
        double raster = (double) rasterInt.intValue();
        if (raster < 1.0) {
            return coordinate;
        } else {
            /*
             * filter the coordinates, use the modulo function, round to the
             * next raster-value.
             */
            double x = coordinate.getX();
            double y = coordinate.getY();
            double xSub = x % (double) raster;
            double ySub = y % (double) raster;
            double xAdd = raster - xSub;
            double yAdd = raster - ySub;

            x = x + ((xSub < xAdd) ? -xSub : xAdd);
            y = y + ((ySub < yAdd) ? -ySub : yAdd);

            return new Point2D.Double(x, y);
        }

    }

    @Override
    public Point2D getUnrasteredCoordinate() {
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
    public void addConnectionTo(AbstractFunction function)
            throws ConnectorException {
        boolean foundFullOne = false;

        for (AbstractConnector connectorOut : getOutConnectors()) {
            for (AbstractConnector connectorIn : function.getInConnectors()) {
                if (connectorOut.getType() == connectorIn.getType()) {
                    /* found equal Connectors */

                    /*
                     * check if already a connection between these two function
                     * exists
                     */
                    for (AbstractConnector con : connectorOut.getConnections()) {
                        if (con == connectorIn) {
                            throw new ConnectorException(
                                    Type.CONNECTION_ALREADY_EXISTS);
                        }
                    }

                    if (!connectorOut.isFull() && !connectorIn.isFull()) {
                        /* first, add connections */
                        connectorIn.addConnection(connectorOut);
                        connectorOut.addConnection(connectorIn);

                        /* now check loop freeness */
                        if (getPipeline().hasLoop()) {
                            /* remove 'cause that is not ok */
                            connectorIn.removeConnection(connectorOut);
                            connectorOut.removeConnection(connectorIn);

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
    }

    @Override
    public void update(Observable arg0, Object arg1) {
        /* get Updates from a Task (changed anything) */
        changedNotifyObservers(new PipelineObserverObject(
                ChangeType.CHANGE_FUNCTION, this));
    }

    @Override
    public Function copy(CopyType type) {
        Function newFunction = new Function(this.parent, this.xmlFunction);

        newFunction.pipeline = this.pipeline;
        newFunction.parent = parent;

        if (type.copyPosition()) {
            newFunction.coordinate = this.coordinate;
        }

        /* copy the tasks */
        newFunction.tasks.clear();
        for (Task task : this.tasks) {
            Task newTask = task.copy(type, newFunction);
            newTask.addObserver(newFunction);
            newFunction.tasks.add(newTask);

            if (task == activeTask) {
                newFunction.activeTask = newTask;
            }
        }

        if (!type.copyValues()) {
            newFunction.activeTask = newFunction.getAvailableTasks()[0];
        }

        return newFunction;
    }

    private Object readResolve() throws ObjectStreamException {
        AbstractFunctionPrototype afp = ModelProxy.getInstance().getFunctions();
        this.parent = afp.getMatchingFunctionGroup(this.parentIdentifier);
        this.xmlFunction = afp
                .getMatchingXMLFunction(this.xmlFunctionIdentifier);

        /* create the observers */
        for (Task task : tasks) {
            task.addObserver(this);
        }

        return this;
    }
}
