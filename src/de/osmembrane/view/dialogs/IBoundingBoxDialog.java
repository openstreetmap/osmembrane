package de.osmembrane.view.dialogs;

import de.osmembrane.view.IView;
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
	 * @param bounds
	 */
	void setBoundingBox(Bounds bounds);
	
}
