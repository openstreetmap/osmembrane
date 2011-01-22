package de.osmembrane.controller.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import de.osmembrane.controller.events.ContainingEvent;
import de.osmembrane.tools.IconLoader;
import de.osmembrane.tools.IconLoader.Size;
import de.osmembrane.view.dialogs.BoundingBoxDialog;

/**
 * Action to edit a parameter which is a bounding box and therefore open the
 * {@link BoundingBoxDialog}. Receives a {@link ContainingEvent}.
 * 
 * @author tobias_kuhn
 * 
 */
public class EditBoundingBoxPropertyAction extends AbstractAction {

	private static final long serialVersionUID = -8977717015720840558L;

	/**
	 * Creates a new {@link EditBoundingBoxPropertyAction}
	 */
	public EditBoundingBoxPropertyAction() {
		putValue(Action.NAME, "Edit BBox Property");
		putValue(Action.SMALL_ICON, new IconLoader("bbox_property_edit.png",
				Size.SMALL).get());
		putValue(Action.LARGE_ICON_KEY, new IconLoader(
				"bbox_property_edit.png", Size.NORMAL).get());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO implement
		throw new UnsupportedOperationException();
	}
}