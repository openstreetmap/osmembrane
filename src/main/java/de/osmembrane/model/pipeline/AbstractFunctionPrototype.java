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

import java.net.URL;
import java.util.Observable;

import de.osmembrane.model.Identifier;
import de.osmembrane.model.xml.XMLEnumValue;
import de.osmembrane.model.xml.XMLFunction;
import de.osmembrane.model.xml.XMLFunctionGroup;
import de.osmembrane.model.xml.XMLParameter;
import de.osmembrane.model.xml.XMLPipe;
import de.osmembrane.model.xml.XMLTask;

/**
 * This is an Prototype for accessing the XMLFunctions.
 * 
 * @author jakob_jarosch
 */
public abstract class AbstractFunctionPrototype extends Observable {

    /**
     * Initiates the Prototype with a given compatible XML-file.
     * 
     * @param xmlFilename
     *            filename of the given XML-file
     */
    public abstract void initiate(URL xmlFilename);

    /**
     * Returns all {@link AbstractFunctionGroup}s.
     * 
     * @return the {@link AbstractFunctionGroup}s
     */
    public abstract AbstractFunctionGroup[] getFunctionGroups();

    /**
     * Returns matching functions for a given String.
     * 
     * @param matching
     *            string which should match on every function
     * @return all matching functions
     */
    public abstract AbstractFunction[] getFilteredFunctions(String matching);

    /**
     * Returns the matching function for a given task-name. The corresponding
     * task is set as active
     * 
     * @param taskName
     *            name of task which should be found
     * @return the function for the task
     */
    public abstract AbstractFunction getMatchingFunctionForTaskName(
            String taskName);

    /**
     * Adds an Identifier for an {@link AbstractFunctionGroup} to the Map.
     * 
     * @param fg
     *            {@link AbstractFunctionGroup} which should be mapped
     * @param xmlFG
     *            referring {@link XMLFunctionGroup} for the
     *            {@link AbstractFunctionGroup}
     * @return the {@link Identifier} for the given
     *         {@link AbstractFunctionGroup}
     */
    protected abstract Identifier pushFGToMap(AbstractFunctionGroup fg,
            XMLFunctionGroup xmlFG);

    /**
     * Returns a matching {@link AbstractFunctionGroup} for an
     * {@link Identifier}.
     * 
     * @param identifier
     *            {@link Identifier} which should be found.
     * @return the matching {@link AbstractFunctionGroup} or NULL
     */
    protected abstract AbstractFunctionGroup getMatchingFunctionGroup(
            Identifier identifier);

    /**
     * Returns a matching {@link Identifier} for an
     * {@link AbstractFunctionGroup}.
     * 
     * @param identifier
     *            {@link AbstractFunctionGroup} which should be found.
     * @return the matching {@link Identifier} or NULL
     */
    protected abstract Identifier getMatchingFunctionGroupIdentifier(
            AbstractFunctionGroup identifier);

    /**
     * Returns a matching {@link XMLFunction} for an {@link Identifier}.
     * 
     * @param identifier
     *            {@link Identifier} which should be found.
     * @return the matching {@link XMLFunction} or NULL
     */
    protected abstract XMLFunction getMatchingXMLFunction(Identifier identifier);

    /**
     * Returns a matching {@link Identifier} for an {@link XMLFunction}.
     * 
     * @param identifier
     *            {@link XMLFunction} which should be found.
     * @return the matching {@link Identifier} or NULL
     */
    protected abstract Identifier getMatchingXMLFunctionIdentifier(
            XMLFunction identifier);

    /**
     * Returns a matching {@link XMLTask} for an {@link Identifier}.
     * 
     * @param identifier
     *            {@link Identifier} which should be found.
     * @return the matching {@link XMLTask} or NULL
     */
    protected abstract XMLTask getMatchingXMLTask(Identifier identifier);

    /**
     * Returns a matching {@link Identifier} for an {@link XMLTask}.
     * 
     * @param identifier
     *            {@link XMLTask} which should be found.
     * @return the matching {@link Identifier} or NULL
     */
    protected abstract Identifier getMatchingXMLTaskIdentifier(
            XMLTask identifier);

    /**
     * Returns a matching {@link XMLPipe} for an {@link Identifier}.
     * 
     * @param identifier
     *            {@link Identifier} which should be found.
     * @return the matching {@link XMLPipe} or NULL
     */
    protected abstract XMLPipe getMatchingXMLPipe(Identifier identifier);

    /**
     * Returns a matching {@link Identifier} for an {@link XMLPipe}.
     * 
     * @param identifier
     *            {@link XMLPipe} which should be found.
     * @return the matching {@link Identifier} or NULL
     */
    protected abstract Identifier getMatchingXMLPipeIdentifier(
            XMLPipe identifier);

    /**
     * Returns a matching {@link XMLParameter} for an {@link Identifier}.
     * 
     * @param identifier
     *            {@link Identifier} which should be found.
     * @return the matching {@link XMLParameter} or NULL
     */
    protected abstract XMLParameter getMatchingXMLParameter(
            Identifier identifier);

    /**
     * Returns a matching {@link Identifier} for an {@link XMLParameter}.
     * 
     * @param identifier
     *            {@link XMLParameter} which should be found.
     * @return the matching {@link Identifier} or NULL
     */
    protected abstract Identifier getMatchingXMLParameterIdentifier(
            XMLParameter identifier);

    /**
     * Returns a matching {@link XMLEnumValue} for an {@link Identifier}.
     * 
     * @param identifier
     *            {@link Identifier} which should be found.
     * @return the matching {@link XMLEnumValue} or NULL
     */
    protected abstract XMLEnumValue getMatchingXMLEnumValue(
            Identifier identifier);

    /**
     * Returns a matching {@link Identifier} for an {@link XMLEnumValue}.
     * 
     * @param identifier
     *            {@link XMLEnumValue} which should be found.
     * @return the matching {@link Identifier} or NULL
     */
    protected abstract Identifier getMatchingXMLEnumValueIdentifier(
            XMLEnumValue identifier);

}
