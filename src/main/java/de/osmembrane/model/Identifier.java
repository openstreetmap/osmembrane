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

package de.osmembrane.model;

import java.io.Serializable;

/**
 * A simple identifier for pipeline items.
 * 
 * @author jakob_jarosch
 */
public class Identifier implements Serializable {

    private static final long serialVersionUID = 2011012315340001L;

    private String identifier;

    /**
     * Constructor for the Identifier.
     * 
     * @param identifier
     *            String which should be used as an Identifier.
     */
    public Identifier(String identifier) {
        this.identifier = identifier;
    }

    /**
     * Returns the {@link Identifier} as a String (like
     * {@link Identifier#toString()}).
     * 
     * @return {@link Identifier} as a String
     */
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public String toString() {
        return identifier.toString();
    }

    @Override
    public int hashCode() {
        return identifier.hashCode();
    }

    @Override
    public boolean equals(Object obj1) {
        /* Compares the two identifiers for equality. */
        if (obj1 instanceof Identifier) {
            Identifier ident = (Identifier) obj1;
            return identifier.equals(ident.getIdentifier());
        }
        return false;
    }
}
