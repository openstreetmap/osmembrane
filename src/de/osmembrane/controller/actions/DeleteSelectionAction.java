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

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import de.osmembrane.model.ModelProxy;
import de.osmembrane.resources.Resource;
import de.osmembrane.tools.I18N;
import de.osmembrane.tools.IconLoader.Size;
import de.osmembrane.view.ViewRegistry;
import de.osmembrane.view.frames.MainFrame;
import de.osmembrane.view.interfaces.IMainFrame;
import de.osmembrane.view.panels.PipelineFunction;
import de.osmembrane.view.panels.PipelineLink;

/**
 * Action to delete a function or a connection from the pipeline. Receives no
 * specific event, has to look for the currently selected object in the view.
 * 
 * @author tobias_kuhn
 * 
 */
public class DeleteSelectionAction extends AbstractAction {

    private static final long serialVersionUID = 8429188229104025512L;

    /**
     * Creates a new {@link DeleteSelectionAction}
     */
    public DeleteSelectionAction() {
        putValue(
                Action.NAME,
                I18N.getInstance().getString(
                        "Controller.Actions.DeleteSelection.Name"));
        putValue(
                Action.SHORT_DESCRIPTION,
                I18N.getInstance().getString(
                        "Controller.Actions.DeleteSelection.Description"));
        putValue(Action.SMALL_ICON, Resource.PROGRAM_ICON.getImageIcon(
                "delete_function.png", Size.SMALL));
        putValue(Action.LARGE_ICON_KEY, Resource.PROGRAM_ICON.getImageIcon(
                "delete_function.png", Size.NORMAL));
        putValue(Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        setEnabled(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        IMainFrame mainFrame = ViewRegistry.getInstance().getCasted(
                MainFrame.class, IMainFrame.class);
        Object selected = mainFrame.getSelected();

        if (selected != null) {
            if (selected instanceof PipelineFunction) {
                // delete selected function
                PipelineFunction pf = (PipelineFunction) selected;
                ModelProxy.getInstance().getPipeline()
                        .deleteFunction(pf.getModelFunction());

            } else if (selected instanceof PipelineLink) {
                // delete selected connection/link
                PipelineLink pl = (PipelineLink) selected;
                pl.getLinkSource()
                        .getModelConnector()
                        .getParent()
                        .removeConnectionTo(
                                pl.getLinkDestination().getModelConnector()
                                        .getParent());
            }
        }
    } /* actionPerformed */
}
