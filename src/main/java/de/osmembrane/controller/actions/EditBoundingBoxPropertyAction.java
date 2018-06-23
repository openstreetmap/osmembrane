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

package de.osmembrane.controller.actions;

import de.osmembrane.Application;
import de.osmembrane.controller.events.ContainingEvent;
import de.osmembrane.controller.mapper.StringBoundingBoxMapper;
import de.osmembrane.exceptions.ControlledException;
import de.osmembrane.exceptions.ExceptionSeverity;
import de.osmembrane.model.pipeline.AbstractParameter;
import de.osmembrane.model.pipeline.BoundingBox;
import de.osmembrane.model.pipeline.ParameterType;
import de.osmembrane.tools.I18N;
import de.osmembrane.view.ViewRegistry;
import de.osmembrane.view.dialogs.BoundingBoxDialog;
import de.osmembrane.view.interfaces.IBoundingBoxDialog;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

/**
 * Action to edit a parameter which is a bounding box and therefore open the
 * {@link BoundingBoxDialog}. Receives a {@link ContainingEvent}.
 *
 * @author tobias_kuhn
 */
public class EditBoundingBoxPropertyAction extends AbstractAction {

    private static final long serialVersionUID = -8977717015720840558L;

    private final StringBoundingBoxMapper mapper = new StringBoundingBoxMapper();

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
                        ibbd.setBoundingBox(mapper.toBoundingBox(p.getParent().getBBox()));
                    } catch (IllegalArgumentException e1) {
                        ibbd.setBoundingBox(new BoundingBox(0, 0, 0, 0));
                    }
                } else {
                    ibbd.setBoundingBox(null);
                }
                ibbd.centerWindow();
                ibbd.showWindow();

                BoundingBox b = ibbd.getBoundingBox();
                if (b != null) {
                    p.getParent().setBBox(mapper.toString(b));
                }
            }
        } else {
            Application.handleException(new ControlledException(this,
                    ExceptionSeverity.UNEXPECTED_BEHAVIOR, I18N.getInstance()
                    .getString("Controller.Actions.InvalidEvent")));
        }
    }
}
