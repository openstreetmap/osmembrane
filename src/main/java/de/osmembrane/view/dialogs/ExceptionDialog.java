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
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import de.osmembrane.Application;
import de.osmembrane.exceptions.ExceptionSeverity;
import de.osmembrane.tools.I18N;
import de.osmembrane.view.AbstractDialog;

/**
 * the exception message dialog (the window you will see most of the time ;)
 * 
 * @author tobias_kuhn
 * 
 */
public class ExceptionDialog extends AbstractDialog {

    private static final long serialVersionUID = 3750775593370584501L;

    /**
     * The components that will describe the exception occured
     */
    private JLabel icon;
    private JLabel caption;
    private JLabel exceptionMessage;
    private JTextArea exceptionText;
    private JScrollPane exceptionTextPane;

    /**
     * The OK and "Show stack trace" Button
     */
    private JButton okButton;
    private JButton showTraceButton;

    /**
     * Whether the exception was fatal and the application should exit now
     */
    private boolean fatal;

    /**
     * The line break
     */
    private final String NL = System.getProperty("line.separator");

    /**
     * Initializes the {@link ExceptionDialog}
     */
    public ExceptionDialog(Window owner) {
        super(owner);

        // set the basics up
        setLayout(new GridBagLayout());

        icon = new JLabel();
        caption = new JLabel();
        caption.setFont(caption.getFont().deriveFont(Font.BOLD));
        exceptionMessage = new JLabel();
        exceptionText = new JTextArea();
        exceptionText.setEditable(false);

        exceptionTextPane = new JScrollPane(exceptionText);
        exceptionTextPane.setPreferredSize(new Dimension(640, 480));

        okButton = new JButton();
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fatal) {
                    System.exit(0);
                } else {
                    hideWindow();
                }
            }
        });

        showTraceButton = new JButton(I18N.getInstance().getString(
                "View.ShowStackTrace"));
        showTraceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exceptionTextPane.setPreferredSize(new Dimension(
                        (getWidth() > 648) ? getWidth() - 8 : 640, 480));
                showTraceButton.setVisible(false);
                exceptionTextPane.setVisible(true);
                exceptionText.setCaretPosition(0);
                pack();
                centerWindow();
            }
        });

        // grid bag layout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        add(icon, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        add(caption, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(exceptionMessage, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        add(exceptionTextPane, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        JPanel buttons = new JPanel();
        buttons.add(showTraceButton);
        buttons.add(okButton);
        if (showTraceButton.getPreferredSize().width > okButton
                .getPreferredSize().width) {
            okButton.setPreferredSize(showTraceButton.getPreferredSize());
        } else {
            showTraceButton.setPreferredSize(okButton.getPreferredSize());
        }
        add(buttons, gbc);

        pack();
        centerWindow();
    }

    /**
     * 
     * @param ste
     *            the {@link StackTraceElement} to print into a string
     * @return the human readable string representation of the stack trace
     *         element
     */
    private String printStackTraceElement(StackTraceElement ste) {
        return "	" + I18N.getInstance().getString("View.ErrorDialog.At") + " "
                + ste.getClassName() + "." + ste.getMethodName() + "("
                + ste.getFileName() + ":" + ste.getLineNumber() + ")" + NL;
    }

    /**
     * Displays a Throwable and handles the possibly necessary shutdown.
     * 
     * @param t
     *            the occurred Throwable
     * @param severity
     *            the kind of the exception
     * @param causingObject
     *            the causing object or null, if unknown
     */
    public void showException(Throwable t, ExceptionSeverity severity,
            Object causingObject) {

        // ensure valid pointers
        if (t == null) {
            Application.handleException(new NullPointerException());
        }

        if (causingObject == null) {
            if (t.getStackTrace().length > 0) {
                causingObject = t.getStackTrace()[0];
            } else {
                // empty stack traces are some sort of exception cause
                causingObject = t.getStackTrace();
            }
        }

        if (severity == null) {
            severity = ExceptionSeverity.INVALID;
        }

        // set icon

        switch (severity) {
        case WARNING:
            setWindowTitle(I18N.getInstance().getString(
                    "View.ErrorDialog.Warning"));
            break;
        case UNEXPECTED_BEHAVIOR:
            setWindowTitle(I18N.getInstance().getString(
                    "View.ErrorDialog.UnexpectedException"));
            break;
        case CRITICAL_UNEXPECTED_BEHAVIOR:
            setWindowTitle(I18N.getInstance().getString(
                    "View.ErrorDialog.CriticalUnexpectedException"));
            break;
        default:
            setWindowTitle(I18N.getInstance().getString(
                    "View.ErrorDialog.Exception"));
        }

        caption.setText(I18N.getInstance().getString("View.ErrorDialog.In",
                t.getClass().getCanonicalName(), causingObject.toString()));

        // find a suitable description, if one exists
        String message = t.getLocalizedMessage();
        if (message == null) {
            message = t.getMessage();
            if (message == null) {
                if (t.getCause() != null) {
                    message = t.getCause().getLocalizedMessage();
                    if (message == null) {
                        message = t.getCause().getMessage();
                        if (message == null) {
                            message = I18N.getInstance().getString(
                                    "View.ErrorDialog.NoMessage");
                        }
                    }
                } else {
                    message = I18N.getInstance().getString(
                            "View.ErrorDialog.NoMessage");
                }
            }

        }
        exceptionMessage.setText(message);

        // general information
        StringBuilder sb = new StringBuilder();
        sb.append(I18N.getInstance().getString("View.ErrorDialog.Caused",
                causingObject.toString(), t.toString())
                + NL + NL);
        for (StackTraceElement ste : t.getStackTrace()) {
            sb.append(printStackTraceElement(ste));
        }

        /*
         * loop through the causes, find out if any of them was an instance of
         * Error
         */
        boolean causeWasError = false;
        Throwable causedBy = t.getCause();
        while (causedBy != null) {
            sb.append(I18N.getInstance().getString("View.ErrorDialog.CausedBy",
                    causedBy.toString())
                    + NL);
            for (StackTraceElement ste : causedBy.getStackTrace()) {
                sb.append(printStackTraceElement(ste));
            }

            if (causedBy instanceof Error) {
                causeWasError = true;
            }

            causedBy = causedBy.getCause();
        }

        exceptionText.setText(sb.toString());

        // determine whether it was a fatal error
        fatal = (severity == ExceptionSeverity.CRITICAL_UNEXPECTED_BEHAVIOR)
                || (causeWasError);

        icon.setIcon(UIManager.getIcon("OptionPane.warningIcon"));
        if (fatal || (severity == null)) {
            icon.setIcon(UIManager.getIcon("OptionPane.errorIcon"));
        }

        if (fatal) {
            okButton.setText(I18N.getInstance().getString("View.Quit"));
        } else {
            okButton.setText(I18N.getInstance().getString("View.OK"));
        }

        // normally hide the stack trace, for warnings hide most of the dialog
        exceptionTextPane.setVisible(false);
        showTraceButton.setVisible(severity != ExceptionSeverity.WARNING);
        caption.setVisible(severity != ExceptionSeverity.WARNING);
        okButton.requestFocusInWindow();

        pack();
        centerWindow();
        showWindow();
    }

}
