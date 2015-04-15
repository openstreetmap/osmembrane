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

import de.osmembrane.model.Identifier;
import de.osmembrane.model.ModelProxy;
import de.osmembrane.model.xml.XMLEnumValue;
import de.osmembrane.model.xml.XMLParameter;
import de.osmembrane.tools.I18N;

/**
 * Implementation of {@link AbstractParameter}.
 * 
 * @author jakob_jarosch
 */
public class Parameter extends AbstractParameter {

    private static final long serialVersionUID = 2011011821310001L;

    /**
     * The XML counterpart of the parameter.
     */
    transient private XMLParameter xmlParam;
    private Identifier xmlParamIdentifier;

    /**
     * The enum values for the parameter (if {@link Parameter#type} is
     * {@link ParameterType#ENUM}).
     */
    private List<EnumValue> enumValues = new ArrayList<EnumValue>();

    /**
     * Parent task.
     */
    private AbstractTask parentTask;

    /**
     * Type of the parameter.
     */
    private ParameterType type;

    /**
     * Value of the Parameter.
     */
    private String value;

    /**
     * Constructor for a new {@link Parameter}.
     * 
     * @param xmlParam
     *            XML counterpart which should be represented by the
     *            {@link Parameter}.
     */
    public Parameter(AbstractTask parentTask, XMLParameter xmlParam) {
        this.type = ParameterType.parseString(xmlParam.getType());
        this.value = null;
        this.xmlParam = xmlParam;
        this.parentTask = parentTask;

        /* set the identifiers */
        AbstractFunctionPrototype afp = ModelProxy.getInstance().getFunctions();
        this.xmlParamIdentifier = afp
                .getMatchingXMLParameterIdentifier(this.xmlParam);

        /* create enum values */
        for (XMLEnumValue xmlEnum : xmlParam.getEnumValue()) {
            enumValues.add(new EnumValue(xmlEnum));
        }
    }

    @Override
    public AbstractTask getParent() {
        return parentTask;
    }

    @Override
    public String getName() {
        return xmlParam.getName();
    }

    @Override
    public String getFriendlyName() {
        /* fallback when friendlyName is not available */
        if (xmlParam.getFriendlyName() == null) {
            return getName();
        }

        return xmlParam.getFriendlyName();
    }

    @Override
    public String getDescription() {
        return I18N.getInstance().getDescription(xmlParam);
    }

    @Override
    public ParameterType getType() {
        return type;
    }

    @Override
    public AbstractEnumValue[] getEnumValue() {
        EnumValue[] values = new EnumValue[enumValues.size()];
        return enumValues.toArray(values);
    }

    @Override
    public String getListType() {
        return xmlParam.getListType();
    }

    @Override
    public String getDefaultValue() {
        return xmlParam.getDefaultValue();
    }

    @Override
    public boolean isDefaultValue() {
        return (getValue() == null);
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean setValue(String value) {
        if (this.getType().isStringEmpty(value)) {
            this.value = null;
        } else {
            this.value = value;
        }

        setChanged();
        notifyObservers();

        return true;
    }

    @Override
    public boolean validate(String value) {
        switch (this.getType()) {
        case ENUM:
            for (AbstractEnumValue enumValue : getEnumValue()) {
                if (enumValue.getValue().equals(value)) {
                    return true;
                }
            }
            break;

        default:
            return this.getType().validate(value);
        }

        return false;
    }

    @Override
    public boolean isValid() {
        if (getValue() != null) {
            return validate(getValue());
        } else {
            return validate(getDefaultValue());
        }
    }

    @Override
    public boolean isRequired() {
        return xmlParam.isRequired();
    }

    @Override
    public boolean isDefaultParameter() {
        return xmlParam.isDefaultParameter();
    }

    @Override
    public boolean hasSpaces() {
        return xmlParam.isHasSpaces();
    }

    @Override
    public Parameter copy(CopyType type, AbstractTask task) {
        Parameter newParam = new Parameter(task, this.xmlParam);

        /* copy the param-value */
        if (type.copyValues()) {
            newParam.value = this.value;
        }

        return newParam;
    }

    private Object readResolve() throws ObjectStreamException {
        AbstractFunctionPrototype afp = ModelProxy.getInstance().getFunctions();
        this.xmlParam = afp.getMatchingXMLParameter(this.xmlParamIdentifier);

        return this;
    }
}
