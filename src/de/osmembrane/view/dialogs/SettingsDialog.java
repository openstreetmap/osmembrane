package de.osmembrane.view.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;

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
	 * Components to edit the Osmosis default path
	 */
	private JTextField osmosisPath;

	/**
	 * Components to edit the JOSM default path
	 */
	private JTextField josmPath;

	/**
	 * Components to edit the default zoom
	 */
	private JLabel defaultZoomDisplay;
	private JSlider defaultZoom;

	/**
	 * Components to edit the language / locale
	 */
	private JComboBox language;

	/**
	 * Components to edit whether short task names shall be used
	 */
	private JCheckBox shortTasks;

	/**
	 * Components to edit whether default parameters shall be exported
	 */
	private JCheckBox defaultParamExport;

	/**
	 * Components to edit how much undo steps shall be available
	 */
	private JSpinner maxUndoSteps;

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

		// actual settings
		JPanel settings = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(8, 8, 8, 8);

		final int minFieldWidth = 256;
		final int minSpinnerWidth = 64;

		gbc.gridy = 0;
		gbc.gridx = 0;
		settings.add(new JLabel("Osmosis pfad"), gbc);
		gbc.gridx = 1;
		osmosisPath = new JTextField();
		osmosisPath.setPreferredSize(new Dimension(minFieldWidth, osmosisPath
				.getPreferredSize().height));
		settings.add(osmosisPath, gbc);

		gbc.gridy = 1;
		gbc.gridx = 0;
		settings.add(new JLabel("JOSM pfad"), gbc);
		gbc.gridx = 1;
		josmPath = new JTextField();
		josmPath.setPreferredSize(new Dimension(minFieldWidth, josmPath
				.getPreferredSize().height));
		settings.add(josmPath, gbc);

		gbc.gridy = 2;
		gbc.gridx = 0;
		settings.add(new JLabel("Sprache"), gbc);
		gbc.gridx = 1;
		String[] locales = new String[Locale.getAvailableLocales().length];
		for (int i = 0; i < Locale.getAvailableLocales().length; i++) {
			locales[i] = Locale.getAvailableLocales()[i].getDisplayLanguage();
		}
		language = new JComboBox(locales);
		language.setPreferredSize(new Dimension(minFieldWidth, language
				.getPreferredSize().height));
		settings.add(language, gbc);

		gbc.gridy = 3;
		gbc.gridx = 0;
		settings.add(new JLabel("Default zoom"), gbc);
		gbc.gridx = 1;
		defaultZoomDisplay = new JLabel("10.0x");
		settings.add(defaultZoomDisplay, gbc);

		gbc.gridy = 4;
		gbc.gridx = 0;
		gbc.gridwidth = 2;
		defaultZoom = new JSlider();
		settings.add(defaultZoom, gbc);
		gbc.gridwidth = 1;

		gbc.gridy = 0;
		gbc.gridx = 2;
		gbc.gridwidth = 2;
		shortTasks = new JCheckBox("Kurze Tasknamen");
		settings.add(shortTasks, gbc);
		gbc.gridwidth = 1;

		gbc.gridy = 1;
		gbc.gridx = 2;
		gbc.gridwidth = 2;
		defaultParamExport = new JCheckBox("Default Paramamen");
		settings.add(defaultParamExport, gbc);
		gbc.gridwidth = 1;

		gbc.gridy = 2;
		gbc.gridx = 2;
		settings.add(new JLabel("Max Undo"), gbc);
		gbc.gridx = 3;
		maxUndoSteps = new JSpinner();
		maxUndoSteps.setPreferredSize(new Dimension(minSpinnerWidth,
				maxUndoSteps.getPreferredSize().height));
		settings.add(maxUndoSteps, gbc);

		add(settings, BorderLayout.CENTER);

		pack();
		centerWindow();
	}

	@Override
	public boolean shallApplyChanges() {
		return this.applyChanges;
	}

	@Override
	public String getOsmosisPath() {
		return this.osmosisPath.getText();
	}

	@Override
	public void setOsmosisPath(String osmosisPath) {
		this.osmosisPath.setText(osmosisPath);
	}

	@Override
	public String getJosmPath() {
		return this.josmPath.getText();
	}

	@Override
	public void setJosmPath(String josmPath) {
		this.josmPath.setText(josmPath);
	}

	@Override
	public double getDefaultZoom() {
		double x = 0;
		// TODO arithmetics here
		return x;
	}

	@Override
	public void setDefaultZoom(double defaultZoom) {
		// TODO arithmetics here

	}

	@Override
	public Locale getLanguage() {
		return Locale.getAvailableLocales()[this.language.getSelectedIndex()];
	}

	@Override
	public void setLanguage(Locale language) {
		for (int i = 0; i < Locale.getAvailableLocales().length; i++) {
			Locale l = Locale.getAvailableLocales()[i];

			if (l.equals(language)) {
				this.language.setSelectedIndex(i);
			}
		}
	}

	@Override
	public boolean getShortTasks() {
		return this.shortTasks.isSelected();
	}

	@Override
	public void setShortTasks(boolean shortTasks) {
		this.shortTasks.setSelected(shortTasks);
	}

	@Override
	public boolean getDefaultParamExport() {
		return this.defaultParamExport.isSelected();
	}

	@Override
	public void setDefaultParamExport(boolean defaultParamExport) {
		this.defaultParamExport.setSelected(defaultParamExport);
	}

	@Override
	public int getMaxUndoSteps() {
		return (Integer) this.maxUndoSteps.getValue();
	}

	@Override
	public void setMaxUndoSteps(int maxUndoSteps) {
		this.maxUndoSteps.setValue(maxUndoSteps);
	}

}