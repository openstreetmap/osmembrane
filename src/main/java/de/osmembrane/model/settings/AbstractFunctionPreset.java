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

package de.osmembrane.model.settings;

import java.io.Serializable;

import de.osmembrane.model.pipeline.AbstractFunction;

/**
 * Simple preset which saves a name and the function which should be saved.
 * 
 * @author jakob_jarosch
 */
public abstract class AbstractFunctionPreset implements Serializable {

    private static final long serialVersionUID = 2011020221230001L;

    /**
     * Returns the name of the preset.
     * 
     * @return name of the preset
     */
    public abstract String getName();

    /**
     * Loads the preset into the given function.
     * 
     * @param function
     *            the {@link AbstractFunction} in which the preset should be
     *            loaded
     */
    public abstract void loadPreset(AbstractFunction function);

    /**
     * Returns the inherited function.
     * 
     * @return inherited function
     */
    protected abstract AbstractFunction getInheritedFunction();
}
