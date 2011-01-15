package de.osmembrane.view.dialogs;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import de.osmembrane.Application;
import de.osmembrane.exceptions.ExceptionSeverity;
import de.osmembrane.tools.I18N;
import de.osmembrane.view.AbstractDialog;

/**
 * the error message dialog (the window you will see most of the time ;)
 * 
 * @author tobias_kuhn
 * 
 */
public class ErrorDialog extends AbstractDialog {

	private static final long serialVersionUID = 3750775593370584501L;

	/**
	 * The components that will describe the error
	 */
	private JLabel messageIcon;
	private JLabel captionLabel;
	private JLabel messageLabel;
	private JTextArea exceptionText;

	/**
	 * The OK and Show Stacktrace Button
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
	 * Initializes the error dialog
	 */
	public ErrorDialog() {

		// set the basics up
		setLayout(new GridBagLayout());

		messageIcon = new JLabel();
		captionLabel = new JLabel();
		captionLabel.setFont(captionLabel.getFont().deriveFont(Font.BOLD));
		messageLabel = new JLabel();
		exceptionText = new JTextArea();
		exceptionText.setEditable(false);

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
		
		showTraceButton = new JButton(I18N.getInstance().getString("View.ShowStackTrace"));
		showTraceButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showTraceButton.setVisible(false);
				exceptionText.setPreferredSize(null);
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
		add(messageIcon, gbc);

		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		add(captionLabel, gbc);

		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		add(messageLabel, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;		
		gbc.gridwidth = 2;
		add(new JScrollPane(exceptionText), gbc);
		
		/*gbc.gridx = 1;
		gbc.gridy = 3;
		gbc.gridwidth = 1;
		add(showTraceButton, gbc);*/
		
		gbc.gridx = 1;
		gbc.gridy = 3;
		gbc.gridwidth = 1;
		add(okButton, gbc);

		pack();
		centerWindow();
	}

	/**
	 * 
	 * @param ste
	 *            the stack trace element to print into a string
	 * @return the human readable string representation of the stack trace
	 *         element
	 */
	private String printStackTraceElement(StackTraceElement ste) {
		return "	" + I18N.getInstance().getString("View.ErrorDialog.At") + " "
				+ ste.getClassName() + "." + ste.getMethodName() + "("
				+ ste.getFileName() + ":" + ste.getLineNumber() + ")" + NL;
	}

	/**
	 * Displays an exception and handles the possibly necessary shutdown.
	 * 
	 * @param t
	 *            the occurred exception
	 * @param type
	 *            the kind of the exception or null, if none known
	 * @param causingObject
	 *            the causing object or null, if none known
	 */
	public void showException(Throwable t, ExceptionSeverity severity,
			Object causingObject) {
		
		if (t == null) {
			Application.handleException(new NullPointerException());
		}
		
		if (causingObject == null) {
			causingObject = t.getStackTrace()[0].getClass();
		}	

		switch (severity) {
		case WARNING:
			setWindowTitle("Warning");
			break;
		case UNEXPECTED_BEHAVIOR:
			setWindowTitle("Unexpected exception");
			break;
		case CRITICAL_UNEXPECTED_BEHAVIOR:
			setWindowTitle("Critical unexpected exception");
			break;
		default:
			setWindowTitle("Exception");
		}
		
		captionLabel.setText(I18N.getInstance().getString(
				"View.ErrorDialog.In", t.getClass().getCanonicalName(),
				causingObject.toString()));

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
		messageLabel.setText(message);

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
			for (StackTraceElement ste : t.getStackTrace()) {
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
		
		messageIcon.setIcon(UIManager.getIcon("OptionPane.warningIcon"));
		if (fatal || (severity == null)) {
			messageIcon.setIcon(UIManager.getIcon("OptionPane.errorIcon"));
		}

		if (fatal) {
			okButton.setText(I18N.getInstance().getString("View.Quit"));
		} else {
			okButton.setText(I18N.getInstance().getString("View.OK"));
		}		
		/*
		if (severity == ExceptionSeverity.WARNING) {
			getContentPane().remove(exceptionText);
			showTraceButton.setVisible(true);
		} else {
			addExceptionText();
			showTraceButton.setVisible(false);
		}*/

		pack();
		centerWindow();
		showWindow();
	}

}