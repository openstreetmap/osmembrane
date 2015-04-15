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

package de.osmembrane.model.preset;

import java.net.URL;

/**
 * A prototype for the Key.Value lists.
 * 
 * @author jakob_jarosch
 */
public abstract class AbstractPresetPrototype {

    /**
     * Loads an xml-file with the given presets definitions.
     * 
     * @param xmlFilename
     *            xml-file with presets
     */
    public abstract void initiate(URL xmlFilename);

    /**
     * Returns the way-items.
     * 
     * @return array of way-items
     */
    public abstract PresetItem[] getWays();

    /**
     * @see AbstractPresetPrototype#getWays() but only keys.
     */
    public abstract PresetItem[] getWayKeys();

    /**
     * Returns only the way-items which matches to the given String-filter.
     * 
     * @param filter
     *            filter which should be applied
     * @return an array of filtered way-items
     */
    public abstract PresetItem[] getFilteredWays(String filter);

    /**
     * @see AbstractPresetPrototype#getFilteredWays(String) but only keys.
     */
    public abstract PresetItem[] getFilteredWayKeys(String filter);

    /**
     * Returns the node-items.
     * 
     * @return array of node-items
     */
    public abstract PresetItem[] getNodes();

    /**
     * @see AbstractPresetPrototype#getNodes() but only keys.
     */
    public abstract PresetItem[] getNodeKeys();

    /**
     * Returns only the node-items which matches to the given String-filter.
     * 
     * @param filter
     *            filter which should be applied
     * @return an array of filtered node-items
     */
    public abstract PresetItem[] getFilteredNodes(String filter);

    /**
     * @see AbstractPresetPrototype#getFilteredNodes(String) but only keys.
     */
    public abstract PresetItem[] getFilteredNodeKeys(String filter);
}
