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

package de.osmembrane.view.dialogs;

import de.osmembrane.model.pipeline.BoundingBox;
import de.osmembrane.tools.I18N;
import de.osmembrane.view.AbstractDialog;
import de.osmembrane.view.components.JBoundingBoxChooser;
import de.osmembrane.view.interfaces.IBoundingBoxDialog;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * Dialog for the bounding box chooser
 *
 * @author tobias_kuhn
 */
public class BoundingBoxDialog extends AbstractDialog implements IBoundingBoxDialog {

    private JBoundingBoxChooser boundingBoxChooser;
    private boolean canceled;

    /**
     * Creates a new {@link BoundingBoxDialog}
     */
    @SuppressWarnings("serial")
    public BoundingBoxDialog(Window owner) {
        super(owner);
        setLayout(new BorderLayout());
        setTitle(I18N.getInstance().getString("View.BoundingBoxDialog"));

        boundingBoxChooser = new JBoundingBoxChooser();
        add(boundingBoxChooser, BorderLayout.CENTER);

        JPanel buttonGrid = new JPanel(new GridLayout(1, 2, 16, 16));

        JButton cancelButton = new JButton(I18N.getInstance().getString(
                "View.OK"));
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                canceled = false;
                hideWindow();
            }
        });
        buttonGrid.add(cancelButton);

        // OK Button
        JButton okButton = new JButton(I18N.getInstance().getString(
                "View.Cancel"));
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                canceled = true;
                hideWindow();
            }
        });
        buttonGrid.add(okButton);

        add(buttonGrid, BorderLayout.SOUTH);

        pack();
        centerWindow();
        setResizable(true);
    }

    @Override
    public BoundingBox getBoundingBox() {
        return canceled ? null : boundingBoxChooser.getBoundingBox();
    }

    @Override
    public void setBoundingBox(BoundingBox bounds) {
        boundingBoxChooser.setBoundingBox(bounds);

    }
}
