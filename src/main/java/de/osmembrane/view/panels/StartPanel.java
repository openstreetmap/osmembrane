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

package de.osmembrane.view.panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import de.osmembrane.controller.ActionRegistry;
import de.osmembrane.controller.actions.LoadPipelineAction;
import de.osmembrane.controller.actions.NewPipelineAction;
import de.osmembrane.controller.actions.ShowQuickstartAction;
import de.osmembrane.resources.Resource;
import de.osmembrane.tools.I18N;
import de.osmembrane.tools.IconLoader.Size;
import de.osmembrane.view.ViewRegistry;
import de.osmembrane.view.frames.MainFrame;
import de.osmembrane.view.interfaces.IMainFrame;

/**
 * The panel on the glass pane that displays the start screen.
 * 
 * @author tobias_kuhn
 * 
 */
public class StartPanel extends JPanel {

    private static final long serialVersionUID = -3089642204222037837L;

    /**
     * Creates a new {@link StartPanel}.
     */
    public StartPanel() {
        setLayout(new GridBagLayout());

        // the hint display
        JPanel startHint = new JPanel();
        startHint.setLayout(new GridLayout(1, 1));
        startHint.setBorder(BorderFactory.createEtchedBorder());
        final JLabel startHintLabel = new JLabel();
        startHintLabel.setHorizontalAlignment(SwingConstants.CENTER);
        startHintLabel.setText(" ");
        startHint.add(startHintLabel);

        // the actual actions
        JLabel newPipeline = new JLabel(Resource.PROGRAM_ICON.getImageIcon(
                "new_pipeline.png", Size.BIG));
        newPipeline.setHorizontalAlignment(SwingConstants.CENTER);
        newPipeline.addMouseListener(new MouseListener() {
            @Override
            public void mouseReleased(MouseEvent e) {
                setVisible(false);
                getParent().setVisible(false);
                getParent().setLayout(null);
                ViewRegistry.getInstance()
                        .getCasted(MainFrame.class, IMainFrame.class)
                        .maximizeWindow();

                Action a = ActionRegistry.getInstance().get(
                        NewPipelineAction.class);
                a.actionPerformed(null);
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
                startHintLabel.setText(" ");

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                startHintLabel.setText(I18N.getInstance().getString(
                        "Controller.Actions.NewPipeline.Description"));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
            }
        });

        JLabel openPipeline = new JLabel(Resource.PROGRAM_ICON.getImageIcon(
                "load_pipeline.png", Size.BIG));
        openPipeline.setHorizontalAlignment(SwingConstants.CENTER);
        openPipeline.addMouseListener(new MouseListener() {
            @Override
            public void mouseReleased(MouseEvent e) {
                setVisible(false);
                getParent().setVisible(false);
                getParent().setLayout(null);
                ViewRegistry.getInstance()
                        .getCasted(MainFrame.class, IMainFrame.class)
                        .maximizeWindow();

                Action a = ActionRegistry.getInstance().get(
                        LoadPipelineAction.class);
                a.actionPerformed(null);
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
                startHintLabel.setText(" ");

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                startHintLabel.setText(I18N.getInstance().getString(
                        "Controller.Actions.LoadPipeline.Description"));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
            }
        });

        JLabel showQuickstart = new JLabel(Resource.PROGRAM_ICON.getImageIcon(
                "quickstarttutorial.png", Size.BIG));
        showQuickstart.setHorizontalAlignment(SwingConstants.CENTER);
        showQuickstart.addMouseListener(new MouseListener() {
            @Override
            public void mouseReleased(MouseEvent e) {
                setVisible(false);
                getParent().setVisible(false);
                getParent().setLayout(null);
                ViewRegistry.getInstance()
                        .getCasted(MainFrame.class, IMainFrame.class)
                        .maximizeWindow();

                Action a = ActionRegistry.getInstance().get(
                        ShowQuickstartAction.class);
                a.actionPerformed(null);
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
                startHintLabel.setText(" ");

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                startHintLabel.setText(I18N.getInstance().getString(
                        "Controller.Actions.ShowQuickstart.Description"));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
            }
        });

        // add them all together
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.weightx = 1.0 / 3.0;
        gbc.weighty = 2.0 / 3.0;

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(newPipeline, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        add(openPipeline, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        add(showQuickstart, gbc);

        gbc.weightx = 1.0;
        gbc.weighty = 1.0 / 3.0;
        gbc.fill = GridBagConstraints.BOTH;

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        add(startHint, gbc);

        setVisible(true);
    }

}
