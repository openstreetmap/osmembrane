package de.osmembrane.model.pipeline;

import java.beans.XMLEncoder;
import java.io.Serializable;

import de.osmembrane.model.xml.XMLEnumValue;

public abstract class AbstractEnumValue implements Serializable {

	public abstract String getDescription();
	public abstract String getFriendlyName();
	public abstract String getValue();
}
