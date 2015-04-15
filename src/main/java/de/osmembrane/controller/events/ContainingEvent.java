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

package de.osmembrane.controller.events;

import java.awt.event.ActionEvent;

/**
 * Generic ActionEvent to contain a specific object.
 * 
 * @author tobias_kuhn
 * 
 */
public class ContainingEvent extends ActionEvent {

    private static final long serialVersionUID = -1862987145811665702L;

    /**
     * The contained object
     */
    private Object contained;

    /**
     * @see ActionEvent#ActionEvent
     * 
     * @param contained
     *            the value contained
     */
    public ContainingEvent(Object source, Object contained) {
        super(source, 0, null);
        this.contained = contained;
    }

    /**
     * @param contained
     *            the contained to set
     */
    public void setContained(Object contained) {
        this.contained = contained;
    }

    /**
     * @return the contained
     */
    public Object getContained() {
        return contained;
    }

    /**
     * @return the contained type
     */
    public Class<?> getContainedClass() {
        return contained.getClass();
    }

}
