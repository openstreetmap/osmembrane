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

package de.osmembrane.view.frames;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.osmembrane.resources.Resource;
import de.osmembrane.tools.I18N;
import de.osmembrane.tools.IconLoader.Size;
import de.osmembrane.view.AbstractFrame;

/**
 * The dialog to show the quickstart tutorial.
 * 
 * @author tobias_kuhn
 * 
 */
public class TutorialFrame extends AbstractFrame {

    private static final long serialVersionUID = -370948835878778575L;

    /**
     * Class to represent one step in the tutorial.
     * 
     * @author tobias_kuhn
     * 
     */
    static class TutorialStep {
        /**
         * File from which the image shall be loaded
         */
        private String imageFile;

        /**
         * Name of the step
         */
        private String imageName;

        /**
         * Creates a new {@link TutorialStep} with the values specified.
         * 
         * @param imageFile
         * @param imageName
         */
        public TutorialStep(String imageFile, String imageName) {
            this.imageFile = imageFile;
            this.imageName = imageName;
        }

        /**
         * @return the image file name
         */
        public String getImageFile() {
            return this.imageFile;
        }

        /**
         * @return the image name
         */
        public String getImageName() {
            return this.imageName;
        }
    } /* TutorialStep */

    /**
     * The arrays of the images to display and their card names.
     */
    private static final TutorialStep[] steps = {
            new TutorialStep("step1.png", "AddFunction"),
            new TutorialStep("step2.png", "SetTaskParam"),
            new TutorialStep("step3.png", "ConnectFuncs"),
            new TutorialStep("step4.png", "Toolbar") };

    /**
     * id of the image currently shown
     */
    private int currentlyShowing;

    /**
     * The panel showing the tutorial's content.
     */
    private JPanel content;

    /**
     * The layout of content.
     */
    private CardLayout cards;

    /**
     * Buttons to go backwards and forwards.
     */
    private JButton backButton;
    private JButton nextButton;

    /**
     * Creates a new {@link TutorialFrame}.
     */
    public TutorialFrame() {
        setLayout(new BorderLayout());

        content = new JPanel();
        cards = new CardLayout();
        content.setLayout(cards);

        for (TutorialStep ts : steps) {
            content.add(
                    new JLabel(Resource.QUICKSTART_IMAGE.getImageIcon(
                            ts.getImageFile(), Size.ORIGINAL)),
                    ts.getImageName());
        }

        add(content, BorderLayout.CENTER);

        // buttons
        JPanel buttonGrid = new JPanel();
        buttonGrid.setLayout(new GridLayout(1, 3));

        backButton = new JButton(I18N.getInstance().getString("View.Back"));
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentlyShowing > 0) {
                    setCurrentlyShowing(currentlyShowing - 1);
                }
            }
        });
        buttonGrid.add(backButton);

        nextButton = new JButton(I18N.getInstance().getString("View.Next"));
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentlyShowing < steps.length - 1) {
                    setCurrentlyShowing(currentlyShowing + 1);
                }
            }
        });
        buttonGrid.add(nextButton);

        JButton closeButton = new JButton(I18N.getInstance().getString(
                "View.Close"));
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hideWindow();
            }
        });
        buttonGrid.add(closeButton);

        nextButton.requestFocus();

        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(buttonGrid);
        add(buttons, BorderLayout.SOUTH);

        setTitle(I18N.getInstance().getString("View.TutorialDialog"));
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        setCurrentlyShowing(0);
        pack();
        centerWindow();
    }

    /**
     * Sets the new step to be currently showing.
     * 
     * @param toShow
     *            step to show
     */
    private void setCurrentlyShowing(int toShow) {
        if ((toShow < 0) || (toShow >= steps.length)) {
            return;
        }
        this.currentlyShowing = toShow;
        cards.show(content, steps[this.currentlyShowing].getImageName());

        backButton.setEnabled(toShow > 0);
        nextButton.setEnabled(toShow < steps.length - 1);
    }

}
