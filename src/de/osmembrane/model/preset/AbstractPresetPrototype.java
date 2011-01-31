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
	 * @param xmlFilename xml-file with presets
	 */
	public abstract void initiate(URL xmlFilename);

	/**
	 * Returns the way-items.
	 * 
	 * @return array of way-items
	 */
	public abstract PresetItem[] getWays();
	
	public abstract PresetItem[] getWayKeys();
	
	/**
	 * Returns only the way-items which matches to the given String-filter.
	 * 
	 * @param filter filter which should be applied
	 * @return an array of filtered way-items
	 */
	public abstract PresetItem[] getFilteredWays(String filter);
	
	public abstract PresetItem[] getFilteredWayKeys(String filter);

	/**
	 * Returns the node-items.
	 * 
	 * @return array of node-items
	 */
	public abstract PresetItem[] getNodes();
	
	public abstract PresetItem[] getNodeKeys();
	
	/**
	 * Returns only the node-items which matches to the given String-filter.
	 * 
	 * @param filter filter which should be applied
	 * @return an array of filtered node-items
	 */
	public abstract PresetItem[] getFilteredNodes(String filter);
	
	public abstract PresetItem[] getFilteredNodeKeys(String filter);
}
