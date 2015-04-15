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
import java.io.Serializable;
import java.util.Observable;
import java.util.Observer;

import de.osmembrane.model.xml.XMLHasDescription;

/**
 * This represents a simple Function for the XML-Functions.
 * 
 * @author jakob_jarosch
 */
public abstract class AbstractFunction extends Observable implements Observer,
        Serializable {

    private static final long serialVersionUID = 2010123022230001L;

    /**
     * Returns the parent {@link AbstractFunctionGroup} of the current Function.
     * 
     * @return parent-group of the current Function
     */
    public abstract AbstractFunctionGroup getParent();

    /**
     * Sets the {@link AbstractPipeline} of this function
     * 
     * @param pipeline
     *            new {@link Pipeline}
     */
    protected abstract void setPipeline(Pipeline pipeline);

    /**
     * Returns the {@link AbstractPipeline} of this function.
     * 
     * @return {@link AbstractPipeline} of this function
     */
    public abstract AbstractPipeline getPipeline();

    /**
     * Returns the ID of the current Function.
     * 
     * @return ID of the current Function
     */
    public abstract String getId();

    /**
     * Returns a human readable name of the current Function.
     * 
     * @return human readable name of the current Function
     */
    public abstract String getFriendlyName();

    /**
     * Returns the Description of the {@link AbstractFunction} .
     * 
     * @return a {@link XMLHasDescription} object
     */
    public abstract String getDescription();

    /**
     * Returns the icon of the {@link AbstractFunction}.
     * 
     * @return icon of the {@link AbstractFunction}
     */
    public abstract BufferedImage getIcon();

    /**
     * Returns the available XMLTasks for the current Function.
     * 
     * @return available XMLTasks for the current Function
     */
    public abstract AbstractTask[] getAvailableTasks();

    /**
     * Returns the currently active XMLTask for the actual Function.
     * 
     * If any changes are applied to the XMLTask call
     * {@link AbstractFunction#changedNotifyObservers(PipelineObserverObject)}
     * 
     * @return active XMLTask for the Function
     */
    public abstract AbstractTask getActiveTask();

    /**
     * Changes the active XMLTask to another XMLTask. Only XMLTasks of the own
     * Function are allowed to set.
     * 
     * @param task
     *            XMLTask which should be set as active
     */
    public abstract void setActiveTask(AbstractTask task);

    /**
     * Returns if the function has all parameters in a valid format set, or not.
     * And also it is checked if all of the connectors has at least one
     * connection to another function.
     * 
     * @return true if function is valid, otherwise false
     */
    public abstract boolean isComplete();

    /**
     * Returns the Coordinates in the Pipeline of the current Function.<br/>
     * Updates to the returned object will not be published on the pipeline, use
     * {@link AbstractFunction#setCoordinate(Point2D)} instead.
     * 
     * @return Coordinates of the current Function
     */
    public abstract Point2D getCoordinate();

    /**
     * @see AbstractFunction#getCoordinate() but without using the rastered
     *      coordinates.
     */
    public abstract Point2D getUnrasteredCoordinate();

    /**
     * Sets the Coordinates in the Pipeline of the current Function.
     * 
     * @param coordinate
     *            new Coordinates of the current Function
     */
    public abstract void setCoordinate(Point2D coordinate);

    /**
     * Returns the In-Connectors of the current Function.
     * 
     * @return In-Connectors of the current Function
     */
    public abstract AbstractConnector[] getInConnectors();

    /**
     * Returns the Out-Connectors of the current Function.
     * 
     * @return Out-Connectors of the current Function
     */
    public abstract AbstractConnector[] getOutConnectors();

    /**
     * Creates a connection to the given {@link AbstractFunction}.
     * 
     * @param function
     *            the function to which the connection should be created.
     * 
     * @throws ConnectorException
     *             when the given function is not compatible or all
     *             {@link AbstractConnector}s are full.
     */
    public abstract void addConnectionTo(AbstractFunction function)
            throws ConnectorException;

    /**
     * Removes a connection between the current and the given
     * {@link AbstractFunction}.
     * 
     * @param function
     *            which connection should be removed
     * @return true if there was a connection
     */
    public abstract boolean removeConnectionTo(AbstractFunction function);

    /**
     * Removes all connections to other connectors.
     */
    protected abstract void unlinkConnectors();

    /**
     * Notifies all registered {@link Observer}s with pre-called
     * {@link Observable#setChanged()}.
     */
    protected abstract void changedNotifyObservers(PipelineObserverObject poo);

    /**
     * Copies the function.
     */
    public abstract AbstractFunction copy(CopyType type);
}
