package de.osmembrane.controller.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import de.osmembrane.Application;
import de.osmembrane.controller.events.ContainingEvent;
import de.osmembrane.exceptions.ControlledException;
import de.osmembrane.exceptions.ExceptionSeverity;
import de.osmembrane.model.pipeline.AbstractParameter;
import de.osmembrane.model.pipeline.ParameterType;
import de.osmembrane.resources.Constants;
import de.osmembrane.tools.I18N;
import de.osmembrane.view.ViewRegistry;
import de.osmembrane.view.dialogs.BoundingBoxDialog;
import de.osmembrane.view.interfaces.IBoundingBoxDialog;
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
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ContainingEvent ce = (ContainingEvent) e;
		if (ce.getContained() instanceof AbstractParameter) {
			AbstractParameter p = (AbstractParameter) ce.getContained();

			if (p.getType() == ParameterType.BBOX) {
				IBoundingBoxDialog ibbd = ViewRegistry.getInstance().getCasted(
						BoundingBoxDialog.class, IBoundingBoxDialog.class);
				
				if (p.getParent().getBBox() != null) {
					try {
						ibbd.setBoundingBox(new Bounds(p.getParent().getBBox(), Constants.BBOX_SEPERATOR));
					} catch (IllegalArgumentException e1) {
						ibbd.setBoundingBox(null);
					}
				} else {
					ibbd.setBoundingBox(null);
				}
				ibbd.centerWindow();
				ibbd.showWindow();
				
				Bounds b = ibbd.getBoundingBox();
				if (b != null) {
					p.getParent().setBBox(b.encodeAsString(Constants.BBOX_SEPERATOR));
				}
			}
		} else {
			Application.handleException(new ControlledException(this,
					ExceptionSeverity.UNEXPECTED_BEHAVIOR, I18N.getInstance()
							.getString("Controller.Actions.InvalidEvent")));
		}
	}
}