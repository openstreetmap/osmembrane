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

/**
 * Value of an Enumeration for a {@link AbstractParameter}.
 * 
 * @author jakob_jarosch
 */
public abstract class AbstractEnumValue implements Serializable {

    private static final long serialVersionUID = 2011011820490001L;

    /**
     * Returns the description of the enum.
     * 
     * @return description of the enum
     */
    public abstract String getDescription();

    /**
     * Returns a human readable name for the enum.
     * 
     * @return human readable name
     */
    public abstract String getFriendlyName();

    /**
     * returns the value of the enum.
     * 
     * @return enum value
     */
    public abstract String getValue();
}
