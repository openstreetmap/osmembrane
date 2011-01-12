package de.osmembrane.view.dialogs;

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

import de.osmembrane.tools.I18N;
import de.osmembrane.view.AbstractDialog;
import de.osmembrane.view.ExceptionType;
import de.osmembrane.view.ViewRegistry;

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
	private JLabel captionLabel;
	private JLabel messageLabel;
	private JTextArea exceptionText;

	/**
	 * The Ok Button
	 */
	private JButton okButton;

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

		// grid bag layout
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(8, 8, 8, 8);
		gbc.gridx = 1;
		gbc.gridy = 0;

		add(captionLabel, gbc);

		gbc.gridy++;
		add(messageLabel, gbc);

		gbc.gridy++;
		add(new JScrollPane(exceptionText), gbc);

		gbc.gridy++;
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
	 * Handles an occurring exception using the ExceptionDialog. A parameter
	 * being null results automatically in a fatal error blaming the initial
	 * caller.
	 * 
	 * @param triggerClass
	 *            class in which the Exception occurred
	 * @param type
	 *            indicates what happened and how fatal it is
	 * @param exception
	 *            original Exception
	 */
	public void showException(Class<?> triggerClass, ExceptionType type,
			Exception exception) {
		// if call is invalid, exit abruptly
		if ((exception == null) || (type == null) || (triggerClass == null)) {
			ViewRegistry.showException(
					this.getClass(),
					ExceptionType.CRITICAL_ABNORMAL_BEHAVIOR,
					new NullPointerException(I18N.getInstance().getString(
							"View.ErrorDialog.InvalidCall")));
		}

		setWindowTitle(exception.getClass().getCanonicalName());

		captionLabel.setText(I18N.getInstance().getString(
				"View.ErrorDialog.In", exception.getClass().getCanonicalName(),
				triggerClass.getCanonicalName()));

		// find a suitable description, if one exists
		String message = exception.getLocalizedMessage();
		if (message == null) {
			message = exception.getMessage();
			if (message == null) {
				if (exception.getCause() != null) {
					message = exception.getCause().getLocalizedMessage();
					if (message == null) {
						message = exception.getCause().getMessage();
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
				triggerClass.getCanonicalName(), exception.toString())
				+ NL + NL);
		for (StackTraceElement ste : exception.getStackTrace()) {
			sb.append(printStackTraceElement(ste));
		}

		/*
		 * loop through the causes, find out if any of them was an instance of
		 * Error
		 */
		boolean causeWasError = false;
		Throwable causedBy = exception.getCause();
		while (causedBy != null) {
			sb.append(I18N.getInstance().getString("View.ErrorDialog.CausedBy",
					causedBy.toString())
					+ NL);
			for (StackTraceElement ste : exception.getStackTrace()) {
				sb.append(printStackTraceElement(ste));
			}

			if (causedBy instanceof Error) {
				causeWasError = true;
			}

			causedBy = causedBy.getCause();
		}

		exceptionText.setText(sb.toString());

		// determine whether it was a fatal error
		fatal = (type == ExceptionType.CRITICAL_ABNORMAL_BEHAVIOR)
				|| (causeWasError);

		if (fatal) {
			okButton.setText(I18N.getInstance().getString("View.Quit"));
		} else {
			okButton.setText(I18N.getInstance().getString("View.OK"));
		}

		pack();
		centerWindow();
		showWindow();
	}

}