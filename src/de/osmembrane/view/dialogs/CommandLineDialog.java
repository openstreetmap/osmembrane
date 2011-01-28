package de.osmembrane.view.dialogs;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import de.osmembrane.controller.ActionRegistry;
import de.osmembrane.controller.actions.ExportPipelineAction;
import de.osmembrane.tools.I18N;
import de.osmembrane.view.AbstractDialog;

/**
 * Simple dialog to display the generated command line, export it, or copy it to
 * the clipboard.
 * 
 * @see Spezifikation.pdf, chapter 2.6
 * 
 * @author tobias_kuhn
 * 
 */
public class CommandLineDialog extends AbstractDialog implements ICommandLineDialog {

	private static final long serialVersionUID = -904804959704267472L;
	
	/**
	 * the component to display the command line
	 */
	private JTextArea commandline;

	/**
	 * Creates a new {@link CommandLineDialog}
	 */
	public CommandLineDialog() {
		// set the basics up
		setLayout(new GridBagLayout());

		setWindowTitle(I18N.getInstance().getString("View.CommandLineDialog"));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 3;
		gbc.insets = new Insets(8, 8, 8, 8);

		// text
		commandline = new JTextArea();
		commandline.setPreferredSize(new Dimension(640, 480));
		add(new JScrollPane(commandline), gbc);

		// export
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		JButton exportButton = new JButton(ActionRegistry.getInstance().get(
				ExportPipelineAction.class));
		add(exportButton, gbc);

		// Copy to clipboard
		gbc.gridx = 1;
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
		add(copyToClipButton, gbc);

		// OK Button
		gbc.gridx = 2;
		JButton okButton = new JButton(I18N.getInstance().getString("View.OK"));
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				hideWindow();
			}
		});
		add(okButton, gbc);

		pack();
		centerWindow();
	}

	@Override
	public void setCommandline(String commandline) {
		this.commandline.setText(commandline);
		pack();
		centerWindow();
	}

}