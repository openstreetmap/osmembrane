package de.osmembrane.model.settings;

import java.io.Serializable;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import de.osmembrane.model.ModelProxy;

/**
 * Interface for accessing the {@link Settings} through the {@link ModelProxy}.
 * 
 * @author jakob_jarosch
 */
public abstract class AbstractSettings extends Observable implements
		Serializable, Observer {

	private static final long serialVersionUID = 2010122714350001L;
	
	/**
	 * Initiates the Settings-model.
	 */
	public abstract void initiate();
	
	public abstract Object getValue(SettingType type);
	
	public abstract void setValue(SettingType type, Object value);
	
	public abstract Locale[] getLanguages();
}