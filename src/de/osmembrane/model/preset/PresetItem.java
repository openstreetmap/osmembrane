package de.osmembrane.model.preset;

import java.io.File;

import javax.swing.ImageIcon;

import de.openstreetmap.josm.tagging_preset_1.Item;
import de.openstreetmap.josm.tagging_preset_1.Key;
import de.osmembrane.resources.Resource;
import de.osmembrane.tools.IconLoader.Size;

/**
 * An item of the node/way key.value lists.
 * 
 * @author jakob_jarosch
 */
public class PresetItem implements Comparable<PresetItem> {

	private Item item;
	
	private String key;
	private String value;

	private ImageIcon icon = null;
	private boolean triedLoadIcon = false;

	/**
	 * Constructor for a PresetItem.
	 * 
	 * @param item
	 *            {@link Item} which should be represented
	 * @param string2 
	 * @param string 
	 */
	protected PresetItem(Item item, String key, String value) {
		this.item = item;
		this.key = key;
		this.value = value;
	}

	/**
	 * Returns the name of the item.
	 * 
	 * @return item-name
	 */
	public String getName() {
		return item.getName();
	}

	/**
	 * Returns the relative path to the icon of the item.
	 * 
	 * @returns the ImageIcon for the item. return NULL if icon failed to load,
	 *          or no icon does exists.
	 */
	public ImageIcon getIcon() {
		if (icon == null && !triedLoadIcon && item.getIcon() != null
				&& item.getIcon().length() > 0) {
			triedLoadIcon = true;
			icon = Resource.PRESET_ICON.getImageIcon(new File(item.getIcon()).getName(), Size.NORMAL);
		}

		return icon;
	}

	/**
	 * Returns the key of the item.
	 * 
	 * @return key of the item
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Returns the value of the item.
	 * 
	 * @return value of the item
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * Returns the key.value of the item.
	 * 
	 * @return key.value of the item
	 */
	public String getKeyValue() {
		return key + "." + value;
	}

	/**
	 * Returns if the item is a node or not.
	 * 
	 * @return true if the item is a node.
	 */
	public boolean isNode() {
		return isType("node");
	}

	/**
	 * Returns if the item is a way or not.
	 * 
	 * @return true if the item is a way
	 */
	public boolean isWay() {
		return isType("way");
	}

	/**
	 * Returns if the item matches to an String-based filter.
	 * 
	 * @return true if the filter matches
	 */
	protected boolean matches(String filter) {
		filter = filter.toLowerCase();
		String name = (getName() != null ? getName().toLowerCase() : "");
		String key = (getKey() != null ? getKey().toLowerCase() : "");
		String value = (getValue() != null ? getValue().toLowerCase() : "");

		if (name.contains(filter) || key.contains(filter)
				|| value.contains(filter)
				|| (key + "." + value).contains(filter)) {
			return true;
		}
		return false;
	}

	/**
	 * Checks if the item matches to an string-type.
	 * 
	 * @param type
	 *            which should be matched
	 * @return true if the type matches
	 */
	private boolean isType(String type) {
		return item.getType().matches(
				"^(" + type + "|" + type + ",.+|.+," + type + ",.+|.+," + type
						+ ")$");
	}

	/**
	 * Returns the {@link Key} of the item.
	 * 
	 * @return {@link Key} of th item.
	 */
	private Key getObjKey() {
		for (Object obj : item.getLabelOrSpaceOrLink()) {
			if (obj instanceof Key) {
				return (Key) obj;
			}
		}
		return null;
	}

	@Override
	public int compareTo(PresetItem o) {
		return (getKey() + "." + getValue()).compareTo(o.getKey() + "." + o.getValue());
	}
}
