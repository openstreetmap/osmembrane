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
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Locale;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

import de.osmembrane.model.settings.SettingType;
import de.osmembrane.model.settings.SettingsTypeUpdateInterval;
import de.osmembrane.tools.I18N;
import de.osmembrane.view.AbstractDialog;
import de.osmembrane.view.components.JTextFieldWithButton;
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
    private JTextFieldWithButton osmosisPath;

    /**
     * Components to edit the JOSM default path
     */
    private JTextFieldWithButton josmPath;

    /**
     * Components to edit the working path
     */
    private JTextFieldWithButton workingPath;

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
     * Components to edit the grid size
     */
    private JLabel rasterSizeDisplay;
    private JSlider rasterSize;
    private JCheckBox rasterSizeEnable;

    /**
     * Components to edit whether the startup screen shall be displayed
     */
    private JCheckBox showStartup;

    /**
     * Components to edit how often updates shall be searched
     */
    private JComboBox updateInterval;

    /**
     * Components to edit the PLaF
     */
    private JComboBox pLaF;

    /**
     * Generates a new {@link SettingsDialog}.
     */
    public SettingsDialog(Window owner) {
        super(owner);

        // set the basics up
        setLayout(new BorderLayout());

        setWindowTitle(I18N.getInstance().getString("View.SettingsDialog"));

        // control buttons
        JButton okButton = new JButton(I18N.getInstance().getString("View.OK"));
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applyChanges = true;
                hideWindow();
            }
        });

        JButton defaultButton = new JButton(I18N.getInstance().getString(
                "View.ResetDefault"));

        defaultButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // reset default values
                for (SettingType st : SettingType.values()) {
                    setValue(st, st.getDefaultValue());
                }
            }
        });

        JButton cancelButton = new JButton(I18N.getInstance().getString(
                "View.Cancel"));
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applyChanges = false;
                hideWindow();
            }
        });

        JPanel buttonCtrlGrid = new JPanel(new GridLayout(1, 3));
        buttonCtrlGrid.add(okButton);
        buttonCtrlGrid.add(defaultButton);
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

        ActionListener selectFiles = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final JTextFieldWithButton jtfwb = (JTextFieldWithButton) ((JButton) e
                        .getSource()).getParent();

                JFileChooser fc = new JFileChooser();
                fc.setSelectedFile(new File(jtfwb.getValue()));
                if (jtfwb.equals(workingPath)) {
                    fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    fc.setAcceptAllFileFilterUsed(false);
                }
                fc.setFileFilter(new FileFilter() {

                    @Override
                    public String getDescription() {
                        return jtfwb.getToolTipText();
                    }

                    @Override
                    public boolean accept(File f) {
                        return (jtfwb.equals(workingPath)) ? f.isDirectory()
                                : f.canExecute();
                    }
                });
                if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    jtfwb.setValue(fc.getSelectedFile().getAbsolutePath());
                }
            }
        };

        gbc.gridy = 0;
        gbc.gridx = 0;
        settings.add(
                new JLabel(I18N.getInstance().getString(
                        "Model.Settings.Type.DEFAULT_OSMOSIS_PATH")
                        + ":"), gbc);
        gbc.gridx = 1;
        osmosisPath = new JTextFieldWithButton("", "...");
        osmosisPath.setPreferredSize(new Dimension(minFieldWidth, osmosisPath
                .getPreferredSize().height));
        osmosisPath.setToolTipText(I18N.getInstance().getString(
                "Model.Settings.Type.DEFAULT_OSMOSIS_PATH.Description"));
        osmosisPath.addButtonActionListener(selectFiles);
        settings.add(osmosisPath, gbc);

        gbc.gridy = 1;
        gbc.gridx = 0;
        settings.add(
                new JLabel(I18N.getInstance().getString(
                        "Model.Settings.Type.DEFAULT_JOSM_PATH")
                        + ":"), gbc);
        gbc.gridx = 1;
        josmPath = new JTextFieldWithButton("", "...");
        josmPath.setPreferredSize(new Dimension(minFieldWidth, josmPath
                .getPreferredSize().height));
        josmPath.setToolTipText(I18N.getInstance().getString(
                "Model.Settings.Type.DEFAULT_JOSM_PATH.Description"));
        josmPath.addButtonActionListener(selectFiles);
        settings.add(josmPath, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        settings.add(
                new JLabel(I18N.getInstance().getString(
                        "Model.Settings.Type.DEFAULT_WORKING_DIRECTORY")
                        + ":"), gbc);
        gbc.gridx = 1;
        workingPath = new JTextFieldWithButton("", "...");
        workingPath.setPreferredSize(new Dimension(minFieldWidth, workingPath
                .getPreferredSize().height));
        workingPath.setToolTipText(I18N.getInstance().getString(
                "Model.Settings.Type.DEFAULT_WORKING_DIRECTORY.Description"));
        workingPath.addButtonActionListener(selectFiles);
        settings.add(workingPath, gbc);

        gbc.gridy = 3;
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

        gbc.gridy = 4;
        gbc.gridx = 0;
        settings.add(
                new JLabel(I18N.getInstance().getString(
                        "Model.Settings.Type.ACTIVE_PLAF")
                        + ":"), gbc);
        gbc.gridx = 1;
        String[] pLaFOptions = new String[UIManager.getInstalledLookAndFeels().length];
        for (int i = 0; i < UIManager.getInstalledLookAndFeels().length; i++) {
            pLaFOptions[i] = UIManager.getInstalledLookAndFeels()[i].getName();
        }
        pLaF = new JComboBox(pLaFOptions);
        pLaF.setPreferredSize(new Dimension(minFieldWidth, pLaF
                .getPreferredSize().height));
        pLaF.setToolTipText(I18N.getInstance().getString(
                "Model.Settings.Type.ACTIVE_PLAF.Description"));
        settings.add(pLaF, gbc);

        gbc.gridy = 5;
        gbc.gridx = 0;
        settings.add(
                new JLabel(I18N.getInstance().getString(
                        "Model.Settings.Type.UPDATE_INTERVAL")
                        + ":"), gbc);
        gbc.gridx = 1;
        String[] updateOptions = {
                I18N.getInstance().getString(
                        "Model.Settings.Type.UPDATE_INTERVAL.Never"),
                I18N.getInstance().getString(
                        "Model.Settings.Type.UPDATE_INTERVAL.PerDay"),
                I18N.getInstance().getString(
                        "Model.Settings.Type.UPDATE_INTERVAL.PerWeek") };
        updateInterval = new JComboBox(updateOptions);
        updateInterval.setPreferredSize(new Dimension(minFieldWidth,
                updateInterval.getPreferredSize().height));
        updateInterval.setToolTipText(I18N.getInstance().getString(
                "Model.Settings.Type.UPDATE_INTERVAL.Description"));
        settings.add(updateInterval, gbc);

        gbc.gridy = 6;
        gbc.gridx = 0;
        settings.add(
                new JLabel(I18N.getInstance().getString(
                        "Model.Settings.Type.DEFAULT_ZOOM_SIZE")
                        + ":"), gbc);
        gbc.gridx = 1;
        defaultZoomDisplay = new JLabel("");
        settings.add(defaultZoomDisplay, gbc);

        gbc.gridy = 7;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        defaultZoom = new JSlider(1, 200);
        defaultZoom.setMajorTickSpacing(10);
        defaultZoom.setMinorTickSpacing(1);
        defaultZoom.setPaintTicks(true);
        defaultZoom.setPaintLabels(false);
        defaultZoom.setPreferredSize(new Dimension(minFieldWidth, defaultZoom
                .getPreferredSize().height));
        defaultZoom.setToolTipText(I18N.getInstance().getString(
                "Model.Settings.Type.DEFAULT_ZOOM_SIZE.Description"));
        defaultZoom.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                double x = defaultZoomToModel(defaultZoom.getValue());
                defaultZoomDisplay.setText(String.format("%.2fx", x));
            }
        });
        settings.add(defaultZoom, gbc);
        gbc.gridwidth = 1;

        gbc.gridy = 0;
        gbc.gridx = 2;
        gbc.gridwidth = 2;
        shortTasks = new JCheckBox(I18N.getInstance().getString(
                "Model.Settings.Type.USE_SHORT_TASK_NAMES_IF_AVAILABLE"));
        shortTasks
                .setToolTipText(I18N
                        .getInstance()
                        .getString(
                                "Model.Settings.Type.USE_SHORT_TASK_NAMES_IF_AVAILABLE.Description"));
        settings.add(shortTasks, gbc);
        gbc.gridwidth = 1;

        gbc.gridy = 1;
        gbc.gridx = 2;
        gbc.gridwidth = 2;
        defaultParamExport = new JCheckBox(I18N.getInstance().getString(
                "Model.Settings.Type.EXPORT_PARAMETERS_WITH_DEFAULT_VALUES"));
        defaultParamExport
                .setToolTipText(I18N
                        .getInstance()
                        .getString(
                                "Model.Settings.Type.EXPORT_PARAMETERS_WITH_DEFAULT_VALUES.Description"));
        settings.add(defaultParamExport, gbc);
        gbc.gridwidth = 1;

        gbc.gridy = 2;
        gbc.gridx = 2;
        gbc.gridwidth = 2;
        showStartup = new JCheckBox(I18N.getInstance().getString(
                "Model.Settings.Type.SHOW_STARTUP_SCREEN"));
        showStartup.setToolTipText(I18N.getInstance().getString(
                "Model.Settings.Type.SHOW_STARTUP_SCREEN.Description"));
        settings.add(showStartup, gbc);
        gbc.gridwidth = 1;

        gbc.gridy = 3;
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

        gbc.gridy = 6;
        gbc.gridx = 2;
        rasterSizeEnable = new JCheckBox(I18N.getInstance().getString(
                "Model.Settings.Type.PIPELINE_RASTER_SIZE")
                + ":");
        rasterSizeEnable.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                rasterSize.setEnabled(rasterSizeEnable.isSelected());
            }
        });
        settings.add(rasterSizeEnable, gbc);
        gbc.gridx = 3;
        rasterSizeDisplay = new JLabel("");
        settings.add(rasterSizeDisplay, gbc);

        gbc.gridy = 7;
        gbc.gridx = 2;
        gbc.gridwidth = 2;
        rasterSize = new JSlider(1, 100);
        rasterSize.setMajorTickSpacing(10);
        rasterSize.setMinorTickSpacing(1);
        rasterSize.setPaintTicks(true);
        rasterSize.setPaintLabels(false);
        rasterSize.setSnapToTicks(true);
        rasterSize.setToolTipText(I18N.getInstance().getString(
                "Model.Settings.Type.PIPELINE_RASTER_SIZE.Description"));
        rasterSize.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                int x = rasterSizeToModel(rasterSize.getValue());
                rasterSizeDisplay.setText(String.valueOf(x) + " px");
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
     * Translates a JSlider value in [1,200] to a modelValue of (0,10].
     * 
     * @param viewValue
     *            the slider position in [1,200]
     * @return the modelValue in (0,2]
     */
    private double defaultZoomToModel(int viewValue) {
        return (double) viewValue / 100.0;
    }

    /**
     * Translates a modelValue of (0,2] to a JSlider value in [1,200].
     * 
     * @param modelValue
     *            the modelValue in (0,2]
     * @return the slider position in [0,200]
     */
    private int defaultZoomFromModel(double modelValue) {
        return (int) (100.0 * modelValue);
    }

    /**
     * Translates a JSlider value of [0,100] to a model value in [0,500]
     * 
     * @param viewValue
     *            JSlider value in [0,100]
     * @return the the modelValue in [0,500]
     */
    private int rasterSizeToModel(int viewValue) {
        if (!rasterSizeEnable.isSelected()) {
            return 0;
        } else {
            return 5 * rasterSize.getValue();
        }
    }

    /**
     * Translates a modelValue of [0,500] to a JSlider value in [0,100]
     * 
     * @param viewValue
     *            the modelValue in [0,500]
     * @return the JSlider value in [0,100]
     */
    private int rasterSizeFromModel(int modelValue) {
        return modelValue / 5;
    }

    @Override
    public Object getValue(SettingType type) {
        switch (type) {
        case DEFAULT_OSMOSIS_PATH:
            return this.osmosisPath.getValue();

        case DEFAULT_JOSM_PATH:
            return this.josmPath.getValue();

        case DEFAULT_WORKING_DIRECTORY:
            return this.workingPath.getValue();

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

        case SHOW_STARTUP_SCREEN:
            return this.showStartup.isSelected();

        case UPDATE_INTERVAL:
            switch (this.updateInterval.getSelectedIndex()) {
            case 1:
                return SettingsTypeUpdateInterval.ONCE_A_DAY;
            case 2:
                return SettingsTypeUpdateInterval.ONCE_A_WEEK;
            default:
                return SettingsTypeUpdateInterval.NEVER;
            }

        case ACTIVE_PLAF:
            return this.pLaF.getSelectedItem();

        default:
            return null;
        }
    }

    @Override
    public void setValue(SettingType type, Object value) {
        switch (type) {
        case DEFAULT_OSMOSIS_PATH:
            this.osmosisPath.setValue((String) value);
            break;

        case DEFAULT_JOSM_PATH:
            this.josmPath.setValue((String) value);
            break;

        case DEFAULT_WORKING_DIRECTORY:
            this.workingPath.setValue((String) value);
            break;

        case ACTIVE_LANGUAGE:
            Locale newLocale = (Locale) value;
            for (int i = 0; i < locales.length; i++) {
                if (locales[i].equals(newLocale)) {
                    this.language.setSelectedIndex(i);
                    return;
                }
            }
            this.language.setSelectedIndex(-1);
            break;

        case DEFAULT_ZOOM_SIZE:
            double x = (Double) value;
            this.defaultZoom.setValue(0);
            this.defaultZoom.setValue(defaultZoomFromModel(x));
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
            int rasterSize = rasterSizeFromModel((Integer) value);
            this.rasterSizeEnable.setSelected(rasterSize != 0);
            this.rasterSize.setEnabled(rasterSize != 0);
            this.rasterSize.setValue(0);
            this.rasterSize.setValue(rasterSize);
            break;

        case SHOW_STARTUP_SCREEN:
            this.showStartup.setSelected((Boolean) value);
            break;

        case UPDATE_INTERVAL:
            SettingsTypeUpdateInterval stui = (SettingsTypeUpdateInterval) value;
            int newIndex = -1;
            switch (stui) {
            case NEVER:
                newIndex = 0;
                break;
            case ONCE_A_DAY:
                newIndex = 1;
                break;
            case ONCE_A_WEEK:
                newIndex = 2;
                break;
            }
            this.updateInterval.setSelectedIndex(newIndex);
            break;

        case ACTIVE_PLAF:
            this.pLaF.setSelectedItem((String) value);

        }
    }

}
