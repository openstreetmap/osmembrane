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

import java.awt.geom.Point2D;

/**
 * Generic ActionEvent to contain a specific object and a location.
 * 
 * @author tobias_kuhn
 * 
 */
public class ContainingLocationEvent extends ContainingEvent {

    private static final long serialVersionUID = -5189868477891735798L;

    /**
     * the location
     */
    protected Point2D location;

    /**
     * @see ContainingEvent#ContainingEvent(Object, Object)
     * @param location
     *            the location
     */
    public ContainingLocationEvent(Object source, Object contained,
            Point2D location) {
        super(source, contained);
        this.location = location;
    }

    /**
     * @param location
     *            the location to set
     */
    public void setLocation(Point2D location) {
        this.location = location;
    }

    /**
     * @return the location
     */
    public Point2D getLocation() {
        return this.location;
    }

}
