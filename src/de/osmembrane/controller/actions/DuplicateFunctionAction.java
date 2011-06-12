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

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import de.osmembrane.model.ModelProxy;
import de.osmembrane.model.pipeline.AbstractFunction;
import de.osmembrane.model.pipeline.CopyType;
import de.osmembrane.resources.Resource;
import de.osmembrane.tools.HeadlessSafe;
import de.osmembrane.tools.I18N;
import de.osmembrane.tools.IconLoader.Size;
import de.osmembrane.view.ViewRegistry;
import de.osmembrane.view.frames.MainFrame;
import de.osmembrane.view.interfaces.IMainFrame;
import de.osmembrane.view.panels.PipelineFunction;

/**
 * Action to duplicate a function in the pipeline. Receives no specific event,
 * has to look for the currently selected object in the view.
 * 
 * @author tobias_kuhn
 * 
 */
public class DuplicateFunctionAction extends AbstractAction {

    private static final long serialVersionUID = -592625521848954610L;

    /**
     * Creates a new {@link DuplicateFunctionAction}
     */
    public DuplicateFunctionAction() {
        putValue(
                Action.NAME,
                I18N.getInstance().getString(
                        "Controller.Actions.DuplicateFunction.Name"));
        putValue(
                Action.SHORT_DESCRIPTION,
                I18N.getInstance().getString(
                        "Controller.Actions.DuplicateFunction.Description"));
        putValue(Action.SMALL_ICON,
                Resource.PROGRAM_ICON.getImageIcon("duplicate.png", Size.SMALL));
        putValue(Action.LARGE_ICON_KEY, Resource.PROGRAM_ICON.getImageIcon(
                "duplicate.png", Size.NORMAL));
        putValue(
                Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke(KeyEvent.VK_D,
                        HeadlessSafe.getMenuShortcutKeyMask()));
        setEnabled(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        IMainFrame mainFrame = ViewRegistry.getInstance().getCasted(
                MainFrame.class, IMainFrame.class);
        Object selected = mainFrame.getSelected();

        if (selected != null) {
            if (selected instanceof PipelineFunction) {
                PipelineFunction pf = (PipelineFunction) selected;

                // create new duplicate
                AbstractFunction duplicate = pf.getModelFunction().copy(
                        CopyType.COPY_ALL);

                // set its location
                Point2D duplLoc = duplicate.getCoordinate();
                duplicate.setCoordinate(new Point2D.Double(duplLoc.getX() + 1.0
                        * Math.random() * pf.getPreferredSize().width, duplLoc
                        .getY() + 1.1 * pf.getPreferredSize().height));

                // add it
                ModelProxy.getInstance().getPipeline().addFunction(duplicate);
            }
        }
    }
}
