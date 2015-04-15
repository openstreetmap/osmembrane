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

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import de.osmembrane.controller.ActionRegistry;
import de.osmembrane.controller.actions.ExportPipelineAction;
import de.osmembrane.model.persistence.FileType;
import de.osmembrane.model.pipeline.AbstractPipeline;
import de.osmembrane.tools.I18N;
import de.osmembrane.view.AbstractDialog;
import de.osmembrane.view.interfaces.ICommandLineDialog;

/**
 * Simple dialog to display the generated command line, export it, or copy it to
 * the clipboard.
 * 
 * @see "Spezifikation.pdf, chapter 2.5 (German)"
 * 
 * @author tobias_kuhn
 * 
 */
public class CommandLineDialog extends AbstractDialog implements
        ICommandLineDialog {

    private static final long serialVersionUID = -904804959704267472L;

    private FileType fileType;

    /**
     * the component to display the command line
     */
    private JTextArea commandline;

    private AbstractPipeline pipeline;

    /**
     * Creates a new {@link CommandLineDialog}
     */
    public CommandLineDialog(Window owner) {
        super(owner);
        setWindowTitle(I18N.getInstance().getString("View.CommandLineDialog"));

        // set the basics up
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(8, 8, 8, 8);

        // format
        JRadioButton cmdRB = new JRadioButton(I18N.getInstance().getString(
                "Controller.Actions.FileType.CMD.Name"));
        cmdRB.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                fileType = FileType.CMD;
                regenerate();
            }
        });

        JRadioButton bashRB = new JRadioButton(I18N.getInstance().getString(
                "Controller.Actions.FileType.BASH.Name"));

        bashRB.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                fileType = FileType.BASH;
                regenerate();
            }
        });

        ButtonGroup group = new ButtonGroup();
        group.add(cmdRB);
        group.add(bashRB);

        fileType = FileType.BASH;
        bashRB.setSelected(true);

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel formatLabel = new JLabel(I18N.getInstance().getString(
                "View.CommandLineDialog.Format"));
        panel.add(formatLabel);
        panel.add(cmdRB);
        panel.add(bashRB);

        add(panel, gbc);

        gbc.insets = new Insets(0, 8, 8, 8);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;
        gbc.gridy = 1;

        // text
        commandline = new JTextArea();
        commandline.setLineWrap(true);
        commandline.setWrapStyleWord(true);
        JScrollPane clPane = new JScrollPane(commandline);
        clPane.setPreferredSize(new Dimension(640, 480));
        add(clPane, gbc);

        // export
        gbc.gridx = 0;
        gbc.gridy = 2;

        JPanel buttonGrid = new JPanel(new GridLayout(1, 3, 10, 0));

        JButton exportButton = new JButton(ActionRegistry.getInstance().get(
                ExportPipelineAction.class));
        buttonGrid.add(exportButton);

        // Copy to clipboard
        JButton copyToClipButton = new JButton(I18N.getInstance().getString(
                "View.CopyToClipboard"));
        copyToClipButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Toolkit.getDefaultToolkit()
                        .getSystemClipboard()
                        .setContents(
                                new StringSelection(commandline.getText()),
                                null);
            }
        });
        buttonGrid.add(copyToClipButton);

        // OK Button
        JButton okButton = new JButton(I18N.getInstance().getString(
                "View.Close"));
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hideWindow();
            }
        });
        buttonGrid.add(okButton);

        add(buttonGrid, gbc);

        pack();
        centerWindow();
    }

    @Override
    public void setPipeline(AbstractPipeline pipeline) {
        this.pipeline = pipeline;
        regenerate();
    }

    private void regenerate() {
        this.commandline.setText(pipeline.generate(fileType));
    }

}
