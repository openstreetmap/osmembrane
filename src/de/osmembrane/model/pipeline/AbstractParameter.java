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
import java.util.Observable;

/**
 * Parameter of an {@link AbstractTask}.
 * 
 * @author jakob_jarosch
 */
public abstract class AbstractParameter extends Observable implements
        Serializable {

    private static final long serialVersionUID = 2011011820530001L;

    /**
     * Returns the task which this parameter belongs to.
     * 
     * @return the parental task
     */
    public abstract AbstractTask getParent();

    /**
     * Returns the name of the task.
     * 
     * @return name of the task
     */
    public abstract String getName();

    /**
     * Returns a human readable name for the parameter.
     * 
     * @return human readable name
     */
    public abstract String getFriendlyName();

    /**
     * Returns the description for the parameter.
     * 
     * @return description for the parameter
     */
    public abstract String getDescription();

    /**
     * Returns the {@link ParameterType} of the {@link AbstractParameter}.
     * 
     * @return type of the parameter
     */
    public abstract ParameterType getType();

    /**
     * Returns possible enum values for the parameter if {@link ParameterType}
     * is {@link ParameterType#ENUM}.
     * 
     * @return array of {@link AbstractEnumValue}s
     */
    public abstract AbstractEnumValue[] getEnumValue();

    /**
     * Returns the list-type of the parameter, is only set, if getType() returns
     * {@link ParameterType#LIST}.
     * 
     * @return the type of the list
     */
    public abstract String getListType();

    /**
     * Returns the default value of the parameter.
     * 
     * @return default value
     */
    public abstract String getDefaultValue();

    /**
     * Returns if the value is equivalent to the default value.
     * 
     * @return true if the value is equivalent to the default value, otherwise
     *         false
     */
    public abstract boolean isDefaultValue();

    /**
     * Returns the value of the parameter.
     * 
     * @return parameter-value
     */
    public abstract String getValue();

    /**
     * Sets the value of the parameter.
     * 
     * @param value
     *            value to be set
     * @return true if value has been set, or false if not (wrong format,
     *         etc...)
     */
    public abstract boolean setValue(String value);

    /**
     * Returns the result of a validation of the given value.
     * 
     * @param value
     *            value to be checked
     * @return true if value is ok, otherwise false
     */
    public abstract boolean validate(String value);

    /**
     * Returns if the parameter has a valid value or not, if value is NULL the
     * defaultValue is checked.
     * 
     * @return true if parameter has a valid value, otherwise false
     */
    public abstract boolean isValid();

    /**
     * Returns if the parameter is a required one.
     * 
     * @return true if the parameter is required, otherwise false
     */
    public abstract boolean isRequired();

    /**
     * True if the parameter is the default parameter of the task. The name of a
     * default parameter may be omitted on the command line. No more than a
     * single parameter of a given task should be declared default, otherwise
     * the processing is undefined.
     */
    public abstract boolean isDefaultParameter();

    /**
     * Returns if the parameter has spaces.
     * 
     * @return true if the parameter has spaces, otherwise false
     */
    public abstract boolean hasSpaces();

    /**
     * Copies the parameter.
     */
    public abstract AbstractParameter copy(CopyType type, AbstractTask task);
}
