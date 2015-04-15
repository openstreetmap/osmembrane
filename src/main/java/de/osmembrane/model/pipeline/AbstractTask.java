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

import java.io.Serializable;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import de.osmembrane.model.xml.XMLPipe;
import de.osmembrane.model.xml.XMLTask;

/**
 * Represents a task inside of a {@link AbstractFunction}.
 * 
 * @author jakob_jarosch
 */
public abstract class AbstractTask extends Observable implements Observer,
        Serializable {

    private static final long serialVersionUID = 2011011821110001L;

    /**
     * Returns the parent function to which the task belongs to.
     * 
     * @return the parental function
     */
    public abstract AbstractFunction getParent();

    /**
     * Returns the description of the {@link AbstractTask}.
     * 
     * @return description of the task
     */
    public abstract String getDescription();

    /**
     * Returns the name of the {@link AbstractTask}.
     * 
     * @return name of the task
     */
    public abstract String getName();

    /**
     * Returns the short name of the {@link AbstractTask}.
     * 
     * @return short name of the task
     */
    public abstract String getShortName();

    /**
     * Returns a human readable name for the {@link AbstractTask}.
     * 
     * @return human readable name
     */
    public abstract String getFriendlyName();

    /**
     * Returns the uri to the help website for the {@link AbstractTask}.
     * 
     * @return uri to the help website
     */
    public abstract String getHelpURI();

    /**
     * Returns the parameters for the {@link AbstractTask}.
     * 
     * @return array of parameters
     */
    public abstract AbstractParameter[] getParameters();

    /**
     * Returns the bbox string.
     * 
     * @return the bbox string, and if task has no bbox parameter NULL
     */
    public abstract String getBBox();

    /**
     * Sets the bbox value to the given one.
     * 
     * @param bbox
     *            given bbox string
     * 
     * @throws ArrayStoreException
     *             if bbox has not not 4 comma separated parameters
     */
    public abstract boolean setBBox(String bbox);

    /**
     * Returns the input pipes for the {@link XMLTask} (required for
     * {@link AbstractFunction}).
     * 
     * @return input pipes of the {@link XMLTask}
     */
    protected abstract List<XMLPipe> getInputPipe();

    /**
     * Returns the output pipes for the {@link XMLTask} (required for
     * {@link AbstractFunction}).
     * 
     * @return output pipes of the {@link XMLTask}
     */
    protected abstract List<XMLPipe> getOutputPipe();

    /**
     * Copies the task.
     */
    public abstract AbstractTask copy(CopyType type,
            AbstractFunction newFunction);
}
