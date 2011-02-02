package de.osmembrane.view.dialogs;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import de.osmembrane.tools.I18N;
import de.osmembrane.view.AbstractDialog;
import de.osmembrane.view.interfaces.ISettingsDialog;

/**
 * The dialog containing the program's settings
 * 
 * @author tobias_kuhn
 *
 */
public class SettingsDialog extends AbstractDialog implements ISettingsDialog {

	private static final long serialVersionUID = 6498307196575629577L;
	
	/**
	 * Whether or not to apply the changes made in the dialog
	 */
	private boolean applyChanges;
	
	/**
	 * Generates a new {@link SettingsDialog}.
	 */
	public SettingsDialog() {
		// set the basics up
		setLayout(new BorderLayout());

		setWindowTitle(I18N.getInstance().getString("View.SettingsDialog"));

		// control buttons
		JButton okButton = new JButton(I18N.getInstance().getString("View.OK"));
		okButton.addKeyListener(returnButtonListener);
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				applyChanges = true;
				hideWindow();
			}
		});

		JButton cancelButton = new JButton(I18N.getInstance().getString(
				"View.Cancel"));
		cancelButton.addKeyListener(returnButtonListener);
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				applyChanges = false;
				hideWindow();
			}
		});

		JPanel buttonCtrlGrid = new JPanel(new GridLayout(1, 2));
		buttonCtrlGrid.add(okButton);
		buttonCtrlGrid.add(cancelButton);

		JPanel buttonCtrlFlow = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonCtrlFlow.add(buttonCtrlGrid);

		add(buttonCtrlFlow, BorderLayout.SOUTH);

		JPanel settings = new JPanel();
		add(settings, BorderLayout.CENTER);

		pack();
		centerWindow();
	}
	
	@Override
	public boolean shallApplyChanges() {
		return this.applyChanges;
	}

}