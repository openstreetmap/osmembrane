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

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.font.TextAttribute;
import java.text.AttributedCharacterIterator.Attribute;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import de.osmembrane.tools.I18N;
import de.osmembrane.view.AbstractDialog;
import de.osmembrane.view.interfaces.IExecutionStateDialog;

/**
 * The dialog that is displayed when an external program is executed.
 * 
 * @author tobias_kuhn
 * 
 */
public class ExecutionStateDialog extends AbstractDialog implements
        IExecutionStateDialog {

    private static final long serialVersionUID = 956559876768946717L;

    /**
     * text field to display the state
     */
    private JTextField stateField;

    /**
     * progress bar to display the progress
     */
    private JProgressBar progress;

    /**
     * text area to display the output lines
     */
    private JTextArea lines;

    /**
     * button to close
     */
    private JButton closeButton;

    /**
     * Creates a new {@link ExecutionStateDialog}.
     */
    public ExecutionStateDialog(Window owner) {
        super(owner);
        setLayout(new BorderLayout());

        // state stuff
        JPanel top = new JPanel();
        top.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.0;
        top.add(new JLabel(I18N.getInstance().getString("View.State") + ":"),
                gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        stateField = new JTextField();
        stateField.setEditable(false);
        top.add(stateField, gbc);

        add(top, BorderLayout.NORTH);

        // progress & lines
        JPanel content = new JPanel();
        content.setLayout(new BorderLayout());

        progress = new JProgressBar(0, 100);
        content.add(progress, BorderLayout.NORTH);

        lines = new JTextArea(25, 80);
        lines.setEditable(false);
        lines.setLineWrap(true);
        lines.setWrapStyleWord(true);

        Map<Attribute, String> fontAttrib = new HashMap<Attribute, String>();
        fontAttrib.put(TextAttribute.FAMILY, Font.MONOSPACED);
        lines.setFont(lines.getFont().deriveFont(fontAttrib));

        content.add(new JScrollPane(lines,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER),
                BorderLayout.CENTER);

        add(content, BorderLayout.CENTER);

        // buttons
        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout(FlowLayout.RIGHT));

        closeButton = new JButton();
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hideWindow();
                for (WindowListener wl : getWindowListeners()) {
                    wl.windowClosing(new WindowEvent(ExecutionStateDialog.this,
                            WindowEvent.WINDOW_CLOSING));
                }
            }
        });
        buttons.add(closeButton);

        add(buttons, BorderLayout.SOUTH);
        setTitle(I18N.getInstance().getString("View.ExecutionStateDialog"));

        pack();
        centerWindow();
    }

    @Override
    public void setState(String state) {
        stateField.setText(state);
    }

    @Override
    public void setProgress(int progress) {
        this.progress.setValue(progress);
    }

    @Override
    public void addOutputLine(String outputLine) {
        if ((outputLine == null) || (outputLine.isEmpty())) {
            return;
        }

        if (lines.getText().isEmpty()) {
            lines.setText(outputLine);
        } else {
            lines.setText(lines.getText()
                    + System.getProperty("line.separator") + outputLine);
        }

        int lastChar = lines.getText().length() - 1;
        if (lastChar < 0) {
            lastChar = 0;
        }
        lines.setCaretPosition(lastChar);
    }

    @Override
    public void clear() {
        stateField.setText("");
        progress.setValue(0);
        lines.setText("");
        closeButton.setText("");
    }

    @Override
    public void setCloseButtonCaption(String caption) {
        closeButton.setText(caption);
    }

}
