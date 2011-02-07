/*
 * This file is part of the OSMembrane project.
 * More informations under www.osmembrane.de
 * 
 * The project is licensed under the Creative Commons
 * Attribution-NonCommercial-ShareAlike 3.0 Unported License.
 * for more details about the license see
 * http://www.osmembrane.de/license/
 * 
 * Source: $HeadURL$ ($Revision$)
 * Last changed: $Date$
 */

package de.osmembrane.view.interfaces;

import de.osmembrane.view.dialogs.BoundingBoxDialog;
import de.unistuttgart.iev.osm.bboxchooser.Bounds;

/**
 * The interface for calling the {@link BoundingBoxDialog}.
 * 
 * @author tobias_kuhn
 * 
 */
public interface IBoundingBoxDialog extends IView {

	/**
	 * @return the selected bounding box, or null, if none
	 */
	Bounds getBoundingBox();

	/**
	 * Sets the selected bounding box to bounds
	 * 
	 * @param bounds
	 */
	void setBoundingBox(Bounds bounds);

}
