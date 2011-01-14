package de.osmembrane.model.pipeline;

import java.io.Serializable;
import java.util.Observable;

import de.osmembrane.model.xml.XMLParameter;

public abstract class AbstractParameter extends Observable implements Serializable {

	public abstract String getName();
	
	public abstract String getFriendlyName();
	
	public abstract String getDescription();
	
	public abstract ParameterType getType();
	
	public abstract AbstractEnumValue[] getEnumValue();
	
	public abstract String getDefaultValue();
	
	public abstract String getValue();
	
	public abstract boolean setValue(String value);
	
	public abstract boolean isRequired();
	
	public abstract boolean isDefaulXMLParameter();
}