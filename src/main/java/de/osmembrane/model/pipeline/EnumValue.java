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

import de.osmembrane.model.Identifier;
import de.osmembrane.model.ModelProxy;
import de.osmembrane.model.xml.XMLEnumValue;
import de.osmembrane.tools.I18N;

/**
 * Implementation of {@link AbstractEnumValue}.
 * 
 * @author jakob_jarosch
 */
public class EnumValue extends AbstractEnumValue {

    private static final long serialVersionUID = 2011011821200001L;

    /**
     * xml value of enum value.
     */
    transient private XMLEnumValue xmlEnum;
    private Identifier xmlEnumIdentifier;

    /**
     * Creates a new EnumValue with a given {@link XMLEnumValue}.
     * 
     * @param xmlEnum
     *            which will be represented by {@link EnumValue}
     */
    public EnumValue(XMLEnumValue xmlEnum) {
        this.xmlEnum = xmlEnum;

        /* set the identifiers */
        AbstractFunctionPrototype afp = ModelProxy.getInstance().getFunctions();
        this.xmlEnumIdentifier = afp
                .getMatchingXMLEnumValueIdentifier(this.xmlEnum);
    }

    @Override
    public String getDescription() {
        return I18N.getInstance().getDescription(xmlEnum);
    }

    @Override
    public String getFriendlyName() {
        return xmlEnum.getFriendlyName();
    }

    @Override
    public String getValue() {
        return xmlEnum.getValue();
    }

    private Object readResolve() throws ObjectStreamException {
        AbstractFunctionPrototype afp = ModelProxy.getInstance().getFunctions();
        this.xmlEnum = afp.getMatchingXMLEnumValue(this.xmlEnumIdentifier);

        return this;
    }
}
