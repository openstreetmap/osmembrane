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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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

	private JLabel rasterSizeDisplay;

	private JSlider rasterSize;

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
		defaultZoomDisplay = new JLabel("");
		settings.add(defaultZoomDisplay, gbc);

		gbc.gridy = 4;
		gbc.gridx = 0;
		gbc.gridwidth = 2;
		defaultZoom = new JSlider(1, 2000);
		defaultZoom.setMajorTickSpacing(1000);
		defaultZoom.setMinorTickSpacing(50);
		defaultZoom.setPaintTicks(true);
		defaultZoom.setPaintLabels(false);
		defaultZoom.setToolTipText(I18N.getInstance().getString(
				"Model.Settings.Type.DEFAULT_ZOOM_SIZE.Description"));
		defaultZoom.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				double x = defaultZoomToModel(defaultZoom.getValue());
				defaultZoomDisplay.setText(String.format("%.3fx", x));
			}
		});
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
		
		gbc.gridy = 3;
		gbc.gridx = 2;
		settings.add(
				new JLabel(I18N.getInstance().getString(
						"Model.Settings.Type.PIPELINE_RASTER_SIZE")
						+ ":"), gbc);
		gbc.gridx = 3;
		rasterSizeDisplay = new JLabel("");
		settings.add(rasterSizeDisplay, gbc);
		
		gbc.gridy = 4;
		gbc.gridx = 2;
		gbc.gridwidth = 2;
		rasterSize = new JSlider(0, 11);
		rasterSize.setMajorTickSpacing(4);
		rasterSize.setMinorTickSpacing(1);
		rasterSize.setPaintTicks(true);
		rasterSize.setPaintLabels(false);
		rasterSize.setSnapToTicks(true);
		rasterSize.setToolTipText(I18N.getInstance().getString(
				"Model.Settings.Type.PIPELINE_RASTER_SIZE.Description"));
		rasterSize.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				double x = rasterSizeToModel(rasterSize.getValue());
				rasterSizeDisplay.setText(String.valueOf(x));
			}
		});
		settings.add(rasterSize, gbc);
		gbc.gridwidth = 1;

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
	
	/**
	 * Translates a JSlider value in [1,2000] to a modelValue of (0,10].
	 * @param viewValue the slider position in [1,2000]
	 * @return the modelValue in (0,2]
	 */
	private double defaultZoomToModel(int viewValue) {
		return (double) viewValue / 1000.0;
	}
	
	/**
	 * Translates a modelValue of (0,2] to a JSlider value in [1,2000].
	 * @param modelValue the modelValue in (0,2]
	 * @return the slider position in [0,2000]
	 */
	private int defaultZoomFromModel(double modelValue) {
		return (int) (1000.0 * modelValue);
	}
	
	/**
	 * Translates a JSlider value of [0,11] to a model value in [0,1024]
	 * @param viewValue JSlider value in [0,11]
	 * @return the the modelValue in [0,1024]
	 */
	private int rasterSizeToModel(int viewValue) {
		if (viewValue == 0) {
			return 0;
		} else {
			return (int) Math.pow(2.0, viewValue - 1);
		}
	}
	
	/**
	 * Translates a modelValue of [0,1024] to a JSlider value in [0,11]
	 * @param viewValue the modelValue in [0,1024]
	 * @return the JSlider value in [0,11]
	 */
	private int rasterSizeFromModel(int modelValue) {
		if ((int) modelValue == 0) {
			return 0;
		} else {
			return (int) (Math.log(modelValue) / Math.log(2.0)) - 1;
		}
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
			int x = defaultZoom.getValue();
			return defaultZoomToModel(x);

		case USE_SHORT_TASK_NAMES_IF_AVAILABLE:
			return this.shortTasks.isSelected();

		case EXPORT_PARAMETERS_WITH_DEFAULT_VALUES:
			return this.defaultParamExport.isSelected();

		case MAXIMUM_UNDO_STEPS:
			return this.maxUndoSteps.getValue();

		case PIPELINE_RASTER_SIZE:
			int y = this.rasterSize.getValue(); 
			return rasterSizeToModel(y);

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
			double x = (Double) value;
			defaultZoom.setValue(defaultZoomFromModel(x));
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
			
		case PIPELINE_RASTER_SIZE:
			int y = (Integer) value;
			this.rasterSize.setValue(rasterSizeFromModel(y));
		}
	}

}