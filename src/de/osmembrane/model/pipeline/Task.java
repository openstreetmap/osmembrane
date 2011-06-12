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
import java.util.Observable;

import de.osmembrane.model.Identifier;
import de.osmembrane.model.ModelProxy;
import de.osmembrane.model.xml.XMLParameter;
import de.osmembrane.model.xml.XMLPipe;
import de.osmembrane.model.xml.XMLTask;
import de.osmembrane.resources.Constants;
import de.osmembrane.tools.I18N;

/**
 * Implementation of {@link AbstractTask}.
 * 
 * @author jakob_jarosch
 */
public class Task extends AbstractTask {

    private static final long serialVersionUID = 2011011821570001L;

    private AbstractFunction parentFunction;

    /**
     * The {@link XMLTask} which is represented by this instance.
     */
    transient private XMLTask xmlTask;
    private Identifier xmlTaskIdentifier;

    /**
     * Parameters which are bound to this Task.
     */
    private List<Parameter> parameters = new ArrayList<Parameter>();

    /**
     * Creates a new Task.
     * 
     * @param xmlTask
     *            {@link XMLTask} which will be represented by this instance.
     */
    public Task(AbstractFunction parentFunction, XMLTask xmlTask) {
        this.xmlTask = xmlTask;
        this.parentFunction = parentFunction;

        /* set the identifier */
        AbstractFunctionPrototype afp = ModelProxy.getInstance().getFunctions();
        this.xmlTaskIdentifier = afp.getMatchingXMLTaskIdentifier(this.xmlTask);

        for (XMLParameter xmlParam : xmlTask.getParameter()) {
            Parameter param = new Parameter(this, xmlParam);
            param.addObserver(this);
            parameters.add(param);
        }
    }

    @Override
    public AbstractFunction getParent() {
        return parentFunction;
    }

    @Override
    public String getDescription() {
        return I18N.getInstance().getDescription(xmlTask);
    }

    @Override
    public String getName() {
        return xmlTask.getName();
    }

    @Override
    public String getShortName() {
        return xmlTask.getShortName();
    }

    @Override
    public String getHelpURI() {
        return xmlTask.getHelpURI();
    }

    @Override
    public String getFriendlyName() {
        /* fallback if friendlyName is not available */
        if (xmlTask.getFriendlyName() == null) {
            return getName();
        }

        return getName() + " (" + xmlTask.getFriendlyName() + ")";
    }

    @Override
    public Parameter[] getParameters() {
        Parameter[] parameters = new Parameter[this.parameters.size()];
        return this.parameters.toArray(parameters);
    }

    @Override
    public String getBBox() {
        Parameter left = null, right = null, top = null, bottom = null;

        for (Parameter param : getParameters()) {
            if (param.getType() == ParameterType.BBOX) {
                if (param.getName().toLowerCase().equals("left")) {
                    left = param;
                } else if (param.getName().toLowerCase().equals("right")) {
                    right = param;
                } else if (param.getName().toLowerCase().equals("top")) {
                    top = param;
                } else if (param.getName().toLowerCase().equals("bottom")) {
                    bottom = param;
                }
            }
        }

        if (left != null && right != null && top != null && bottom != null) {
            return bottom.getValue() + Constants.BBOX_SEPERATOR
                    + left.getValue() + Constants.BBOX_SEPERATOR
                    + top.getValue() + Constants.BBOX_SEPERATOR
                    + right.getValue();
        } else {
            return null;
        }
    }

    @Override
    public boolean setBBox(String bbox) {
        String[] bboxArray = bbox.split(Constants.BBOX_SEPERATOR);
        if (bboxArray.length != 4) {
            throw new ArrayStoreException(
                    "bbox should have 4 comma separated parameters.");
        }

        String bottom = bboxArray[0];
        String left = bboxArray[1];
        String top = bboxArray[2];
        String right = bboxArray[3];

        for (Parameter param : getParameters()) {
            if (param.getType() == ParameterType.BBOX) {
                if (param.getName().toLowerCase().equals("left")) {
                    param.setValue(left);
                    left = null;
                } else if (param.getName().toLowerCase().equals("right")) {
                    param.setValue(right);
                    right = null;
                } else if (param.getName().toLowerCase().equals("top")) {
                    param.setValue(top);
                    top = null;
                } else if (param.getName().toLowerCase().equals("bottom")) {
                    param.setValue(bottom);
                    bottom = null;
                }
            }
        }

        return (left == null && right == null && top == null && bottom == null);
    }

    @Override
    protected List<XMLPipe> getInputPipe() {
        return xmlTask.getInputPipe();
    }

    @Override
    protected List<XMLPipe> getOutputPipe() {
        return xmlTask.getOutputPipe();
    }

    @Override
    public void update(Observable o, Object arg) {
        /* A parameter got a change (anything changed) */
        setChanged();
        notifyObservers();
    }

    @Override
    public Task copy(CopyType type, AbstractFunction newFunction) {
        Task newTask = new Task(parentFunction, this.xmlTask);

        /* copy the parameters */
        newTask.parameters.clear();
        for (Parameter param : this.parameters) {
            Parameter newParam = param.copy(type, newTask);
            newParam.addObserver(newTask);
            newTask.parameters.add(newParam);
        }

        return newTask;
    }

    private Object readResolve() throws ObjectStreamException {
        AbstractFunctionPrototype afp = ModelProxy.getInstance().getFunctions();
        this.xmlTask = afp.getMatchingXMLTask(this.xmlTaskIdentifier);

        /* create the observers */
        for (Parameter param : parameters) {
            param.addObserver(this);
        }

        return this;
    }
}
