package de.osmembrane.view.interfaces;

import java.util.Locale;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;

/**
 * Interface for {@link SettingDialog}.
 * 
 * @author tobias_kuhn
 * 
 */
public interface ISettingsDialog extends IView {

	/**
	 * @return whether or not the changes made in the dialog should be applied
	 *         to the model
	 */
	public boolean shallApplyChanges();

	/**
	 * @return the Osmosis path set in the dialog
	 */
	public String getOsmosisPath();

	/**
	 * @param osmosisPath the Osmosis path to set in the dialog
	 */
	public void setOsmosisPath(String osmosisPath);

	/**
	 * @return the JOSM path set in the dialog
	 */
	public String getJosmPath();

	/**
	 * @param josmPath the JOSM path to set in the dialog
	 */
	public void setJosmPath(String josmPath);

	/**
	 * @return the default zoom set in the dialog
	 */
	public double getDefaultZoom();

	/**
	 * @param defaultZoom the default zoom to set in the dialog
	 */
	public void setDefaultZoom(double defaultZoom);

	/**
	 * @return the language set in the dialog
	 */
	public Locale getLanguage();

	/**
	 * @param language the language to set in the dialog
	 */
	public void setLanguage(Locale language);

	/**
	 * @return the use short tasks flag set in the dialog
	 */
	public boolean getShortTasks();

	/**
	 * @param shortTasks the use short tasks flag to set in the dialog
	 */
	public void setShortTasks(boolean shortTasks);

	/**
	 * @return the export default parameters flag path set in the dialog
	 */
	public boolean getDefaultParamExport();

	/**
	 * @param defaultParamExport the export default parameters flag path to set in the dialog
	 */
	public void setDefaultParamExport(boolean defaultParamExport);

	/**
	 * @return the maximum undo steps amount set in the dialog
	 */
	public int getMaxUndoSteps();

	/**
	 * @param maxUndoSteps the  maximum undo steps amount to set in the dialog
	 */
	public void setMaxUndoSteps(int maxUndoSteps);

}
