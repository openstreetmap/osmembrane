package de.osmembrane.view.interfaces;

import java.util.Locale;

import de.osmembrane.model.settings.SettingType;

/**
 * Interface for {@link SettingDialog}.
 * 
 * @author tobias_kuhn
 * 
 */
public interface ISettingsDialog extends IView {

	/**
	 * Sets the available locales to locales.
	 * 
	 * @param locales
	 *            the available locales
	 */
	public void setLocales(Locale[] locales);

	/**
	 * @return whether or not the changes made in the dialog should be applied
	 *         to the model
	 */
	public boolean shallApplyChanges();

	/**
	 * Returns the set value for type.
	 * 
	 * @param type
	 *            the {@link SettingType}
	 * @return the value for type
	 */
	public Object getValue(SettingType type);

	/**
	 * Sets the value of type in the SettingsDialog to value.
	 * 
	 * @param type
	 *            the {@link SettingType}
	 * @param value
	 *            the value for type
	 */
	public void setValue(SettingType type, Object value);

}
