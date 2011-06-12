/*
 * This file is part of the OSMembrane project.
 * More informations under www.osmembrane.de
 * 
 * The project is licensed under the GNU GENERAL PUBLIC LICENSE 3.0.
 * for more details about the license see http://www.osmembrane.de/license/
 * 
 * Source: $HeadURL: https://osmembrane.de/svn/sources/src/header.txt $ ($Revision: 703 $)
 * Last changed: $Date: 2011-02-07 10:56:49 +0100 (Mo, 07 Feb 2011) $
 */

package de.osmembrane.view.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import de.osmembrane.tools.I18N;
import de.osmembrane.view.AbstractDialog;
import de.osmembrane.view.interfaces.IPipelineSettingsDialog;

/**
 * The dialog containing the pipeline's settings
 * 
 * @author tobias_kuhn
 * 
 */
public class PipelineSettingsDialog extends AbstractDialog implements
        IPipelineSettingsDialog {

    private static final long serialVersionUID = 6498307196575629577L;

    /**
     * Whether or not to apply the changes made in the dialog
     */
    private boolean applyChanges;

    /**
     * all those settings
     */
    private JTextField pipelineName;

    private JCheckBox verboseFlag;
    private JSpinner verbose;

    private JCheckBox debugFlag;
    private JSpinner debug;

    private JTextArea comment;

    /**
     * Generates a new {@link PipelineSettingsDialog}.
     * 
     * @param owner
     */
    public PipelineSettingsDialog(Window owner) {
        super(owner);

        // set the basics up
        setLayout(new BorderLayout());

        setWindowTitle(I18N.getInstance().getString(
                "View.PipelineSettingsDialog"));

        // control buttons
        JButton okButton = new JButton(I18N.getInstance().getString("View.OK"));
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applyChanges = true;
                hideWindow();
            }
        });

        JButton cancelButton = new JButton(I18N.getInstance().getString(
                "View.Cancel"));
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applyChanges = false;
                hideWindow();
            }
        });

        JPanel buttonCtrlGrid = new JPanel(new GridLayout(1, 3));
        buttonCtrlGrid.add(okButton);
        buttonCtrlGrid.add(cancelButton);

        JPanel buttonCtrlFlow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonCtrlFlow.add(buttonCtrlGrid);

        add(buttonCtrlFlow, BorderLayout.SOUTH);

        // actual settings
        JPanel settings = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);

        final int minFieldWidth = 256;
        final int minSpinnerWidth = 64;

        gbc.gridy = 0;
        gbc.gridx = 0;
        settings.add(
                new JLabel(I18N.getInstance().getString(
                        "View.PipelineSettingsDialog.Name")
                        + ":"), gbc);
        gbc.gridx = 1;
        pipelineName = new JTextField();
        pipelineName.setPreferredSize(new Dimension(minFieldWidth, pipelineName
                .getPreferredSize().height));
        settings.add(pipelineName, gbc);

        gbc.gridy = 1;
        gbc.gridx = 0;
        verboseFlag = new JCheckBox(I18N.getInstance().getString(
                "View.PipelineSettingsDialog.Verbose")
                + ":");
        verboseFlag.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verbose.setEnabled(verboseFlag.isSelected());
                if (verbose.getValue().equals(-1)) {
                    verbose.setValue(0);
                }
            }
        });
        settings.add(verboseFlag, gbc);
        gbc.gridx = 1;
        verbose = new JSpinner();
        verbose.setPreferredSize(new Dimension(minSpinnerWidth, verbose
                .getPreferredSize().height));
        settings.add(verbose, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        debugFlag = new JCheckBox(I18N.getInstance().getString(
                "View.PipelineSettingsDialog.Debug")
                + ":");
        debugFlag.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                debug.setEnabled(debugFlag.isSelected());
                if (debug.getValue().equals(-1)) {
                    debug.setValue(0);
                }
            }
        });
        settings.add(debugFlag, gbc);
        gbc.gridx = 1;
        debug = new JSpinner();
        debug.setPreferredSize(new Dimension(minSpinnerWidth, debug
                .getPreferredSize().height));
        settings.add(debug, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        settings.add(
                new JLabel(I18N.getInstance().getString(
                        "View.PipelineSettingsDialog.Comment")
                        + ":"), gbc);

        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        comment = new JTextArea();
        comment.setPreferredSize(new Dimension(minFieldWidth * 2, minFieldWidth));
        settings.add(new JScrollPane(comment), gbc);

        add(settings, BorderLayout.CENTER);

        pack();
        centerWindow();
    }

    @Override
    public boolean shallApplyChanges() {
        return this.applyChanges;
    }

    @Override
    public void setVerbose(int mode) {
        verboseFlag.setSelected(mode >= 0);
        verbose.setEnabled(mode >= 0);
        verbose.setValue(mode);
    }

    @Override
    public int getVerbose() {
        if (verboseFlag.isSelected()) {
            return (Integer) verbose.getValue();
        } else {
            return -1;
        }
    }

    @Override
    public void setDebug(int mode) {
        debugFlag.setSelected(mode >= 0);
        debug.setEnabled(mode >= 0);
        debug.setValue(mode);
    }

    @Override
    public int getDebug() {
        if (debugFlag.isSelected()) {
            return (Integer) debug.getValue();
        } else {
            return -1;
        }
    }

    @Override
    public void setComment(String comment) {
        this.comment.setText(comment);
    }

    @Override
    public String getComment() {
        return comment.getText();
    }

    @Override
    public void setName(String name) {
        pipelineName.setText(name);
    }

    @Override
    public String getName() {
        return pipelineName.getText();
    }

}
