package de.osmembrane.view.dialogs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import de.osmembrane.model.settings.FunctionPreset;
import de.osmembrane.tools.I18N;
import de.osmembrane.view.AbstractDialog;
import de.osmembrane.view.interfaces.IFunctionPresetDialog;

/**
 * Displays a dialog to select the correct FunctionPreset to load.
 * 
 * @author tobias_kuhn
 * 
 */
public class FunctionPresetDialog extends AbstractDialog implements
		IFunctionPresetDialog {

	private static final long serialVersionUID = -1165660042954662085L;

	/**
	 * Internal enum to keep track of the selection.
	 * 
	 * @author tobias_kuhn
	 * 
	 */
	enum ButtonSelection {
		NONE, LOAD, DELETE
	};

	/**
	 * Keeps track of the last selection.
	 */
	private ButtonSelection selectedButton;

	/**
	 * The combo box to select the preset
	 */
	private JComboBox selection;

	/**
	 * The presets to select from
	 */
	private FunctionPreset[] presets;

	/**
	 * Buttons for loading & deleting
	 */
	private JButton loadButton;
	private JButton deleteButton;

	/**
	 * Creates a new {@link FunctionPresetDialog}.
	 */
	public FunctionPresetDialog() {
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(8, 8, 8, 8);

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		selection = new JComboBox();
		selection.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("ÄKSCHN");
				loadButton.setEnabled(selection.getSelectedIndex() > 0);
				deleteButton.setEnabled(selection.getSelectedIndex() > 0);
			}
		});
		add(selection, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.fill = GridBagConstraints.NONE;
		JPanel buttonGrid = new JPanel(new GridLayout(1, 3));

		loadButton = new JButton(I18N.getInstance().getString("View.Load"));
		loadButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selectedButton = ButtonSelection.LOAD;
				hideWindow();
			}
		});
		loadButton.addKeyListener(returnButtonListener);
		buttonGrid.add(loadButton);

		deleteButton = new JButton(I18N.getInstance().getString("View.Delete"));
		deleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selectedButton = ButtonSelection.DELETE;
				hideWindow();
			}
		});
		deleteButton.addKeyListener(returnButtonListener);
		buttonGrid.add(deleteButton);

		JButton cancelButton = new JButton(I18N.getInstance().getString(
				"View.Cancel"));
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selectedButton = ButtonSelection.NONE;
				hideWindow();
			}
		});
		cancelButton.addKeyListener(returnButtonListener);
		buttonGrid.add(cancelButton);

		add(buttonGrid, gbc);

		selectedButton = ButtonSelection.NONE;
		setTitle(I18N.getInstance().getString("View.FunctionPresetDialog"));

		pack();
		centerWindow();
	}

	@Override
	public void open(FunctionPreset[] presets) {
		this.presets = presets;

		String[] selectables = new String[presets.length];
		for (int i = 0; i < presets.length; i++) {
			selectables[i] = presets[i].getName();
		}

		selection.setModel(new DefaultComboBoxModel(selectables));

		loadButton.setEnabled(false);
		deleteButton.setEnabled(false);
		selectedButton = ButtonSelection.NONE;
		showWindow();
	}

	@Override
	public FunctionPreset getSelectedPreset() {
		if (selection.getSelectedIndex() < 0) {
			return null;
		} else {
			return presets[selection.getSelectedIndex()];
		}
	}

	@Override
	public boolean loadSelected() {
		return selectedButton.equals(ButtonSelection.LOAD);
	}

	@Override
	public boolean deleteSelected() {
		return selectedButton.equals(ButtonSelection.DELETE);
	}

}
