package de.osmembrane.view.interfaces;


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

}
