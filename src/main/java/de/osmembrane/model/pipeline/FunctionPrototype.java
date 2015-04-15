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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.osmembrane.Application;
import de.osmembrane.exceptions.ControlledException;
import de.osmembrane.exceptions.ExceptionSeverity;
import de.osmembrane.model.Identifier;
import de.osmembrane.model.persistence.FileException;
import de.osmembrane.model.persistence.PersistenceFactory;
import de.osmembrane.model.persistence.XMLOsmosisStructurePersistence;
import de.osmembrane.model.xml.XMLEnumValue;
import de.osmembrane.model.xml.XMLFunction;
import de.osmembrane.model.xml.XMLFunctionGroup;
import de.osmembrane.model.xml.XMLOsmosisStructure;
import de.osmembrane.model.xml.XMLParameter;
import de.osmembrane.model.xml.XMLPipe;
import de.osmembrane.model.xml.XMLTask;

/**
 * Implementation of {@link AbstractFunctionPrototype}.
 * 
 * @author jakob_jarosch
 */
public class FunctionPrototype extends AbstractFunctionPrototype {

    private XMLOsmosisStructure xmlStruct = null;
    private List<FunctionGroup> functionGroups = new ArrayList<FunctionGroup>();

    private Map<Identifier, AbstractFunctionGroup> functionGroupMap = new HashMap<Identifier, AbstractFunctionGroup>();
    private Map<AbstractFunctionGroup, Identifier> functionGroupIdentifierMap = new HashMap<AbstractFunctionGroup, Identifier>();

    private Map<Identifier, XMLFunctionGroup> xmlFunctionGroupMap = new HashMap<Identifier, XMLFunctionGroup>();
    private Map<Identifier, XMLFunction> xmlFunctionMap = new HashMap<Identifier, XMLFunction>();
    private Map<Identifier, XMLTask> xmlTaskMap = new HashMap<Identifier, XMLTask>();
    private Map<Identifier, XMLPipe> xmlPipeMap = new HashMap<Identifier, XMLPipe>();
    private Map<Identifier, XMLParameter> xmlParameterMap = new HashMap<Identifier, XMLParameter>();
    private Map<Identifier, XMLEnumValue> xmlEnumValueMap = new HashMap<Identifier, XMLEnumValue>();

    private Map<Object, Identifier> identifiers = new HashMap<Object, Identifier>();

    @Override
    public void initiate(URL xmlFilename) {

        try {
            xmlStruct = (XMLOsmosisStructure) PersistenceFactory.getInstance()
                    .getPersistence(XMLOsmosisStructurePersistence.class)
                    .load(xmlFilename);

            createMaps();

            for (XMLFunctionGroup group : xmlStruct.getFunctionGroup()) {
                FunctionGroup fg = new FunctionGroup(group);
                functionGroups.add(fg);

                /* add to the maps */
                Identifier identifier = identifiers.get(group);
                functionGroupIdentifierMap.put(fg, identifier);
            }
        } catch (FileException e) {
            Application.handleException(new ControlledException(this,
                    ExceptionSeverity.CRITICAL_UNEXPECTED_BEHAVIOR, e,
                    "Could not load the OSMembrane xml file."));
        }
    }

    private void createMaps() {
        for (XMLFunctionGroup group : xmlStruct.getFunctionGroup()) {
            Identifier groupIdentifier = new Identifier(group.getId());
            xmlFunctionGroupMap.put(groupIdentifier, group);
            identifiers.put(group, groupIdentifier);

            for (XMLFunction function : group.getFunction()) {
                Identifier functionIdentifier = new Identifier(groupIdentifier
                        + "|" + function.getId());
                xmlFunctionMap.put(functionIdentifier, function);
                identifiers.put(function, functionIdentifier);

                for (XMLTask task : function.getTask()) {
                    Identifier taskIdentifier = new Identifier(
                            functionIdentifier + "|" + task.getName());
                    xmlTaskMap.put(taskIdentifier, task);
                    identifiers.put(task, taskIdentifier);

                    for (XMLPipe inPipe : task.getInputPipe()) {
                        Identifier pipeIdentifier = new Identifier(
                                taskIdentifier + "|in|" + inPipe.getType()
                                        + inPipe.getIndex());
                        xmlPipeMap.put(pipeIdentifier, inPipe);
                        identifiers.put(inPipe, pipeIdentifier);
                    }
                    for (XMLPipe outPipe : task.getOutputPipe()) {
                        Identifier pipeIdentifier = new Identifier(
                                taskIdentifier + "|out|" + outPipe.getType()
                                        + outPipe.getIndex());
                        xmlPipeMap.put(pipeIdentifier, outPipe);
                        identifiers.put(outPipe, pipeIdentifier);
                    }

                    for (XMLParameter param : task.getParameter()) {
                        Identifier paramIdentifier = new Identifier(
                                taskIdentifier + "|" + param.getName());
                        xmlParameterMap.put(paramIdentifier, param);
                        identifiers.put(param, paramIdentifier);

                        for (XMLEnumValue enumValue : param.getEnumValue()) {
                            Identifier enumValueIdentifier = new Identifier(
                                    paramIdentifier + "|enumValues|"
                                            + enumValue.getValue());
                            xmlEnumValueMap.put(enumValueIdentifier, enumValue);
                            identifiers.put(enumValue, enumValueIdentifier);
                        }
                    }
                }
            }
        }
    }

    @Override
    public AbstractFunctionGroup[] getFunctionGroups() {
        FunctionGroup[] groups = new FunctionGroup[functionGroups.size()];
        groups = functionGroups.toArray(groups);
        return groups;
    }

    @Override
    public AbstractFunction[] getFilteredFunctions(String matching) {
        matching = matching.toLowerCase().trim();

        /* remove all pre dashes, to optimize search results */
        while (matching.length() > 0 && matching.substring(0, 1).equals("-")) {
            matching = matching.substring(1);
        }

        List<AbstractFunction> matchingFunctions = new ArrayList<AbstractFunction>();

        for (AbstractFunctionGroup group : getFunctionGroups()) {
            for (AbstractFunction function : group.getFunctions()) {
                boolean matched = false;
                /* check if the function matches the String */
                if (function.getId().toLowerCase().contains(matching)
                        || function.getFriendlyName().toLowerCase()
                                .contains(matching)) {
                    matched = true;
                }

                /* check if a task matches the string */
                for (AbstractTask task : function.getAvailableTasks()) {
                    if (task.getName().toLowerCase().contains(matching)
                            || task.getFriendlyName().toLowerCase()
                                    .contains(matching)) {
                        matched = true;
                    }
                }

                /* something matched, add the function */
                if (matched) {
                    matchingFunctions.add(function);
                }
            }
        }

        AbstractFunction[] array = new AbstractFunction[matchingFunctions
                .size()];
        return matchingFunctions.toArray(array);
    }

    @Override
    public AbstractFunction getMatchingFunctionForTaskName(String taskName) {
        AbstractTask foundTask = null;

        for (AbstractFunctionGroup group : getFunctionGroups()) {
            for (AbstractFunction function : group.getFunctions()) {

                for (AbstractTask task : function.getAvailableTasks()) {
                    if (task.getName().toLowerCase()
                            .equals(taskName.toLowerCase())) {
                        foundTask = task;
                    }
                    if (task.getShortName() != null) {
                        if (task.getShortName().toLowerCase()
                                .equals(taskName.toLowerCase())) {
                            foundTask = task;
                        }
                    }
                }

                if (foundTask != null) {

                    AbstractFunction functionCopy = function
                            .copy(CopyType.WITHOUT_VALUES_AND_POSITION);
                    for (AbstractTask task : functionCopy.getAvailableTasks()) {
                        if (task.getName().equals(foundTask.getName())) {
                            functionCopy.setActiveTask(task);
                        }
                    }
                    return functionCopy;
                }
            }
        }

        return null;
    }

    @Override
    protected Identifier pushFGToMap(AbstractFunctionGroup fg,
            XMLFunctionGroup xmlFG) {
        Identifier ident = new Identifier(xmlFG.getId());
        functionGroupMap.put(ident, fg);
        identifiers.put(fg, ident);
        return ident;
    }

    @Override
    protected AbstractFunctionGroup getMatchingFunctionGroup(
            Identifier identifier) {
        return functionGroupMap.get(identifier);
    }

    @Override
    protected Identifier getMatchingFunctionGroupIdentifier(
            AbstractFunctionGroup identifier) {
        return identifiers.get(identifier);
    }

    @Override
    protected XMLFunction getMatchingXMLFunction(Identifier identifier) {
        return xmlFunctionMap.get(identifier);
    }

    @Override
    protected Identifier getMatchingXMLFunctionIdentifier(XMLFunction identifier) {
        return identifiers.get(identifier);
    }

    @Override
    protected XMLTask getMatchingXMLTask(Identifier identifier) {
        return xmlTaskMap.get(identifier);
    }

    @Override
    protected Identifier getMatchingXMLTaskIdentifier(XMLTask identifier) {
        return identifiers.get(identifier);
    }

    @Override
    protected XMLPipe getMatchingXMLPipe(Identifier identifier) {
        return xmlPipeMap.get(identifier);
    }

    @Override
    protected Identifier getMatchingXMLPipeIdentifier(XMLPipe identifier) {
        return identifiers.get(identifier);
    }

    @Override
    protected XMLParameter getMatchingXMLParameter(Identifier identifier) {
        return xmlParameterMap.get(identifier);
    }

    @Override
    protected Identifier getMatchingXMLParameterIdentifier(
            XMLParameter identifier) {
        return identifiers.get(identifier);
    }

    @Override
    protected XMLEnumValue getMatchingXMLEnumValue(Identifier identifier) {
        return xmlEnumValueMap.get(identifier);
    }

    @Override
    protected Identifier getMatchingXMLEnumValueIdentifier(
            XMLEnumValue identifier) {
        return identifiers.get(identifier);
    }
}
