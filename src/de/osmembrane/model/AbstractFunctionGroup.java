package de.osmembrane.model;

import de.osmembrane.model.xml.XMLFunctionGroup;
import java.io.Serializable;

public abstract class AbstractFunctionGroup implements Serializable {

	public abstract XMLFunctionGroup getXMLFunctionGroup();

	public abstract AbstractFunction[] getFunctions();
}