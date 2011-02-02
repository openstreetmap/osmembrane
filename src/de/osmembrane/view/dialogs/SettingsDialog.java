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

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;

import de.osmembrane.model.settings.SettingType;
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
	private Locale[] locales;

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
		settings.add(
				new JLabel(I18N.getInstance().getString(
						"Model.Settings.Type.DEFAULT_OSMOSIS_PATH")
						+ ":"), gbc);
		gbc.gridx = 1;
		osmosisPath = new JTextField();
		osmosisPath.setPreferredSize(new Dimension(minFieldWidth, osmosisPath
				.getPreferredSize().height));
		osmosisPath.setToolTipText(I18N.getInstance().getString(
				"Model.Settings.Type.DEFAULT_OSMOSIS_PATH.Description"));
		settings.add(osmosisPath, gbc);

		gbc.gridy = 1;
		gbc.gridx = 0;
		settings.add(
				new JLabel(I18N.getInstance().getString(
						"Model.Settings.Type.DEFAULT_JOSM_PATH")
						+ ":"), gbc);
		gbc.gridx = 1;
		josmPath = new JTextField();
		josmPath.setPreferredSize(new Dimension(minFieldWidth, josmPath
				.getPreferredSize().height));
		josmPath.setToolTipText(I18N.getInstance().getString(
				"Model.Settings.Type.DEFAULT_JOSM_PATH.Description"));
		settings.add(josmPath, gbc);

		gbc.gridy = 2;
		gbc.gridx = 0;
		settings.add(
				new JLabel(I18N.getInstance().getString(
						"Model.Settings.Type.ACTIVE_LANGUAGE")
						+ ":"), gbc);
		gbc.gridx = 1;
		language = new JComboBox();
		language.setPreferredSize(new Dimension(minFieldWidth, language
				.getPreferredSize().height));
		language.setToolTipText(I18N.getInstance().getString(
				"Model.Settings.Type.ACTIVE_LANGUAGE.Description"));
		settings.add(language, gbc);

		gbc.gridy = 3;
		gbc.gridx = 0;
		settings.add(
				new JLabel(I18N.getInstance().getString(
						"Model.Settings.Type.DEFAULT_ZOOM_SIZE")
						+ ":"), gbc);
		gbc.gridx = 1;
		defaultZoomDisplay = new JLabel("10.0x");
		settings.add(defaultZoomDisplay, gbc);

		gbc.gridy = 4;
		gbc.gridx = 0;
		gbc.gridwidth = 2;
		defaultZoom = new JSlider();
		defaultZoom.setToolTipText(I18N.getInstance().getString(
				"Model.Settings.Type.DEFAULT_ZOOM_SIZE.Description"));
		settings.add(defaultZoom, gbc);
		gbc.gridwidth = 1;

		gbc.gridy = 0;
		gbc.gridx = 2;
		gbc.gridwidth = 2;
		shortTasks = new JCheckBox(I18N.getInstance().getString(
				"Model.Settings.Type.USE_SHORT_TASK_NAMES_IF_AVAILABLE"));
		shortTasks.setToolTipText(I18N.getInstance().getString(
				"Model.Settings.Type.USE_SHORT_TASK_NAMES_IF_AVAILABLE.Description"));
		settings.add(shortTasks, gbc);
		gbc.gridwidth = 1;

		gbc.gridy = 1;
		gbc.gridx = 2;
		gbc.gridwidth = 2;
		defaultParamExport = new JCheckBox(I18N.getInstance().getString(
				"Model.Settings.Type.EXPORT_PARAMETERS_WITH_DEFAULT_VALUES"));
		defaultParamExport.setToolTipText(I18N.getInstance().getString(
				"Model.Settings.Type.EXPORT_PARAMETERS_WITH_DEFAULT_VALUES.Description"));
		settings.add(defaultParamExport, gbc);
		gbc.gridwidth = 1;

		gbc.gridy = 2;
		gbc.gridx = 2;
		settings.add(
				new JLabel(I18N.getInstance().getString(
						"Model.Settings.Type.MAXIMUM_UNDO_STEPS")
						+ ":"), gbc);
		gbc.gridx = 3;
		maxUndoSteps = new JSpinner();
		maxUndoSteps.setPreferredSize(new Dimension(minSpinnerWidth,
				maxUndoSteps.getPreferredSize().height));
		maxUndoSteps.setToolTipText(I18N.getInstance().getString(
				"Model.Settings.Type.MAXIMUM_UNDO_STEPS.Description"));
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
	public void setLocales(Locale[] locales) {
		this.locales = locales;
		String[] localeStrings = new String[locales.length];
		for (int i = 0; i < locales.length; i++) {
			localeStrings[i] = locales[i].getDisplayLanguage();
		}
		language.setModel(new DefaultComboBoxModel(localeStrings));
	}

	@Override
	public Object getValue(SettingType type) {
		switch (type) {
		case DEFAULT_OSMOSIS_PATH:
			return this.osmosisPath.getText();

		case DEFAULT_JOSM_PATH:
			return this.josmPath.getText();

		case ACTIVE_LANGUAGE:
			int selected = this.language.getSelectedIndex();
			return (selected >= 0) ? this.locales[this.language
					.getSelectedIndex()] : null;

		case DEFAULT_ZOOM_SIZE:
			// arithmetics
			return 1.0;

		case USE_SHORT_TASK_NAMES_IF_AVAILABLE:
			return this.shortTasks.isSelected();

		case EXPORT_PARAMETERS_WITH_DEFAULT_VALUES:
			return this.defaultParamExport.isSelected();

		case MAXIMUM_UNDO_STEPS:
			return this.maxUndoSteps.getValue();

		case PIPELINE_RASTER_SIZE:

		default:
			return null;
		}
	}

	@Override
	public void setValue(SettingType type, Object value) {
		switch (type) {
		case DEFAULT_OSMOSIS_PATH:
			this.osmosisPath.setText((String) value);
			break;

		case DEFAULT_JOSM_PATH:
			this.josmPath.setText((String) value);
			break;

		case ACTIVE_LANGUAGE:
			Locale newLocale = (Locale) value;
			for (int i = 0; i < locales.length; i++) {
				if (locales[i].equals(newLocale)) {
					language.setSelectedIndex(i);
					return;
				}
			}
			language.setSelectedIndex(-1);
			break;

		case DEFAULT_ZOOM_SIZE:
			// arithmetics
			break;

		case USE_SHORT_TASK_NAMES_IF_AVAILABLE:
			this.shortTasks.setSelected((Boolean) value);
			break;

		case EXPORT_PARAMETERS_WITH_DEFAULT_VALUES:
			this.defaultParamExport.setSelected((Boolean) value);
			break;

		case MAXIMUM_UNDO_STEPS:
			this.maxUndoSteps.setValue((Integer) value);
			break;
		}
	}

}