package de.osmembrane.controller.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import de.osmembrane.Application;
import de.osmembrane.controller.events.ContainingEvent;
import de.osmembrane.exceptions.ControlledException;
import de.osmembrane.exceptions.ExceptionSeverity;
import de.osmembrane.model.pipeline.AbstractParameter;
import de.osmembrane.model.pipeline.ParameterType;
import de.osmembrane.resources.Constants;
import de.osmembrane.resources.Resource;
import de.osmembrane.tools.I18N;
import de.osmembrane.tools.IconLoader.Size;
import de.osmembrane.view.ViewRegistry;
import de.osmembrane.view.dialogs.BoundingBoxDialog;
import de.osmembrane.view.dialogs.IBoundingBoxDialog;
import de.unistuttgart.iev.osm.bboxchooser.Bounds;

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
		putValue(Action.SMALL_ICON, Resource.PROGRAM_ICON.getImageIcon(
				"bbox_property_edit.png", Size.SMALL));
		putValue(Action.LARGE_ICON_KEY, Resource.PROGRAM_ICON.getImageIcon(
				"bbox_property_edit.png", Size.NORMAL));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ContainingEvent ce = (ContainingEvent) e;
		if (ce.getContained() instanceof AbstractParameter) {
			AbstractParameter p = (AbstractParameter) ce.getContained();

			if (p.getType() == ParameterType.BBOX) {
				IBoundingBoxDialog ibbd = ViewRegistry.getInstance().getCasted(
						BoundingBoxDialog.class, IBoundingBoxDialog.class);
				
				ibbd.setBoundingBox(new Bounds(p.getValue(), Constants.BBOX_SEPERATOR));
				ibbd.showWindow();
				
				Bounds b = ibbd.getBoundingBox();
				if (b != null) {
					p.setValue(b.encodeAsString(Constants.BBOX_SEPERATOR));
				}
			}
		} else {
			Application.handleException(new ControlledException(this,
					ExceptionSeverity.UNEXPECTED_BEHAVIOR, I18N.getInstance()
							.getString("Controller.Actions.InvalidEvent")));
		}
	}
}