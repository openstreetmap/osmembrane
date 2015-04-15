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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        return getFilteredNodes("");
    }

    @Override
    public PresetItem[] getNodeKeys() {
        return filterDoubleKeys(getNodes());
    }

    @Override
    public PresetItem[] getFilteredNodes(String filter) {
        List<PresetItem> items = new ArrayList<PresetItem>();
        for (PresetItem item : nodeList) {
            if (item.matches(filter)) {
                items.add(item);
            }
        }

        return items.toArray(new PresetItem[items.size()]);
    }

    @Override
    public PresetItem[] getFilteredNodeKeys(String filter) {
        return filterDoubleKeys(getFilteredNodes(filter));
    }

    @Override
    public PresetItem[] getWays() {
        return getFilteredWays("");
    }

    @Override
    public PresetItem[] getWayKeys() {
        return filterDoubleKeys(getWays());
    }

    @Override
    public PresetItem[] getFilteredWays(String filter) {
        List<PresetItem> items = new ArrayList<PresetItem>();
        for (PresetItem item : wayList) {
            if (item.matches(filter)) {
                items.add(item);
            }
        }

        return items.toArray(new PresetItem[items.size()]);
    }

    @Override
    public PresetItem[] getFilteredWayKeys(String filter) {
        return filterDoubleKeys(getFilteredWays(filter));
    }

    private void createLists(List<Object> objects) {
        for (Object obj : objects) {
            if (obj instanceof Item) {
                processItem((Item) obj);
            }
            if (obj instanceof Group) {
                createLists(((Group) obj).getGroupOrItemOrSeparator());
            }
        }
        Collections.sort(nodeList);
        Collections.sort(wayList);
    }

    private void processItem(Item item) {
        Key key = null;
        for (Object obj : item.getLabelOrSpaceOrLink()) {
            if (obj instanceof Key) {
                key = (Key) obj;
            }
        }
        if (key != null) {
            PresetItem presetItem = new PresetItem(item, key.getKey(),
                    key.getValue());

            /* Add the item to the lists which it refer to. */
            if (presetItem.isNode()) {
                nodeList.add(presetItem);
            }
            if (presetItem.isWay()) {
                wayList.add(presetItem);
            }
        }
    }

    private PresetItem[] filterDoubleKeys(PresetItem[] items) {
        Map<String, PresetItem> keyMap = new HashMap<String, PresetItem>();
        for (PresetItem item : items) {
            keyMap.put(item.getKey(), item);
        }

        return keyMap.values().toArray(new PresetItem[keyMap.values().size()]);
    }
}
