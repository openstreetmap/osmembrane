package de.osmembrane.model.preset;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import sun.awt.SunHints.Value;

import de.openstreetmap.josm.tagging_preset_1.Group;
import de.openstreetmap.josm.tagging_preset_1.Item;
import de.openstreetmap.josm.tagging_preset_1.Key;
import de.openstreetmap.josm.tagging_preset_1.Root;
import de.osmembrane.Application;
import de.osmembrane.exceptions.ControlledException;
import de.osmembrane.exceptions.ExceptionSeverity;
import de.osmembrane.model.persistence.FileException;
import de.osmembrane.model.persistence.PersistenceFactory;
import de.osmembrane.model.persistence.TaggingPresetPresistence;

/**
 * Implementation of {@link AbstractPresetPrototype}.
 * 
 * @author jakob_jarosch
 */
public class PresetPrototype extends AbstractPresetPrototype {

	private Root preset;
	
	private List<PresetItem> nodeList = new ArrayList<PresetItem>();
	private List<PresetItem> wayList = new ArrayList<PresetItem>();

	@Override
	public void initiate(URL xmlFilename) {
		try {
			preset = (Root) PersistenceFactory.getInstance()
					.getPersistence(TaggingPresetPresistence.class)
					.load(xmlFilename);
			
			List<Object> obj = preset.getGroupOrItemOrSeparator();
			createLists(obj);
		} catch (FileException e) {
			Application.handleException(new ControlledException(this,
					ExceptionSeverity.CRITICAL_UNEXPECTED_BEHAVIOR, e,
					"Could not load the OSMembrane xml file."));
		}
	}
	
	@Override
	public PresetItem[] getNodes() {
		return nodeList.toArray(new PresetItem[nodeList.size()]);
	}

	@Override
	public PresetItem[] getFilteredNodes(String filter) {
		List<PresetItem> items = new ArrayList<PresetItem>();
		for(PresetItem item : nodeList) {
			if(item.matches(filter)) {
				items.add(item);
			}
		}
		
		return items.toArray(new PresetItem[items.size()]);
	}
	
	@Override
	public PresetItem[] getWays() {
		return wayList.toArray(new PresetItem[wayList.size()]);
	}
	
	@Override
	public PresetItem[] getFilteredWays(String filter) {
		List<PresetItem> items = new ArrayList<PresetItem>();
		for(PresetItem item : wayList) {
			if(item.matches(filter)) {
				items.add(item);
			}
		}
		
		return items.toArray(new PresetItem[items.size()]);
	}
	
	private void createLists(List<Object> objects) {
		for (Object obj : objects) {
			if(obj instanceof Item) {
				processItem((Item) obj);
			}
			if(obj instanceof Group) {
				createLists(((Group) obj).getGroupOrItemOrSeparator());
			}
		}
	}
	
	private void processItem(Item item) {
		Key key = null;
		for (Object obj : item.getLabelOrSpaceOrLink()) {
			if (obj instanceof Key) {
				key = (Key) obj;
			}
		}
		if (key != null) {
			PresetItem presetItem = new PresetItem(item, key.getKey(), key.getValue());
			
			/* Add the item to the lists which it refer to. */
			if(presetItem.isNode()) {
				nodeList.add(presetItem);
			}
			if(presetItem.isWay()) {
				wayList.add(presetItem);
			}
		}
	}
}
