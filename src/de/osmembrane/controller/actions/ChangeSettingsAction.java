package de.osmembrane.controller.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Locale;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import de.osmembrane.model.ModelProxy;
import de.osmembrane.model.settings.AbstractSettings;
import de.osmembrane.model.settings.SettingType;
import de.osmembrane.resources.Resource;
import de.osmembrane.tools.I18N;
import de.osmembrane.tools.IconLoader.Size;
import de.osmembrane.view.ViewRegistry;
import de.osmembrane.view.dialogs.SettingsDialog;
import de.osmembrane.view.interfaces.ISettingsDialog;

/**
 * Action to open the change settings dialog.
 * 
 * @author tobias_kuhn
 * 
 */
public class ChangeSettingsAction extends AbstractAction {

	private static final long serialVersionUID = -1491395674816531738L;

	/**
	 * Creates a new {@link ChangeSettingsAction}
	 */
	public ChangeSettingsAction() {
		putValue(
				Action.NAME,
				I18N.getInstance().getString(
						"Controller.Actions.ChangeSettings.Name"));
		putValue(
				Action.SHORT_DESCRIPTION,
				I18N.getInstance().getString(
						"Controller.Actions.ChangeSettings.Description"));
		putValue(Action.SMALL_ICON,
				Resource.PROGRAM_ICON.getImageIcon("settings.png", Size.SMALL));
		putValue(Action.LARGE_ICON_KEY,
				Resource.PROGRAM_ICON.getImageIcon("settings.png", Size.NORMAL));
		putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_F8, 0));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ISettingsDialog sd = ViewRegistry.getInstance().getCasted(
				SettingsDialog.class, ISettingsDialog.class);
		
		// set the display components
		AbstractSettings as = ModelProxy.getInstance().accessSettings();		
		
		sd.setOsmosisPath((String) as.getValue(SettingType.DEFAULT_OSMOSIS_PATH));
		sd.setJosmPath((String) as.getValue(SettingType.DEFAULT_JOSM_PATH));
		sd.setLanguage((Locale) as.getValue(SettingType.ACTIVE_LANGUAGE));
		sd.setDefaultZoom((Double) as.getValue(SettingType.DEFAULT_ZOOM_SIZE));
		sd.setShortTasks((Boolean) as.getValue(SettingType.USE_SHORT_TASK_NAMES_IF_AVAILABLE));
		sd.setDefaultParamExport((Boolean) as.getValue(SettingType.EXPORT_PARAMETERS_WITH_DEFAULT_VALUES));
		sd.setMaxUndoSteps((Integer) as.getValue(SettingType.MAXIMUM_UNDO_STEPS));
		
		sd.showWindow();
		
		if (sd.shallApplyChanges()) {
			// set the model values
			as.setValue(SettingType.DEFAULT_OSMOSIS_PATH, sd.getOsmosisPath());
			as.setValue(SettingType.DEFAULT_JOSM_PATH, sd.getJosmPath());
			as.setValue(SettingType.ACTIVE_LANGUAGE, sd.getLanguage());
			as.setValue(SettingType.DEFAULT_ZOOM_SIZE, sd.getDefaultZoom());
			as.setValue(SettingType.USE_SHORT_TASK_NAMES_IF_AVAILABLE, sd.getShortTasks());
			as.setValue(SettingType.EXPORT_PARAMETERS_WITH_DEFAULT_VALUES, sd.getDefaultParamExport());
			as.setValue(SettingType.MAXIMUM_UNDO_STEPS, sd.getMaxUndoSteps());
		}
	}
}