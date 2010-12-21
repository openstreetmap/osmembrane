package de.osmembrane.model;

import de.osmembrane.model.xml.XMLFunctionGroup;
import java.io.Serializable;

public interface IFunctionGroup extends Serializable {

	public XMLFunctionGroup getXMLFunctionGroup();

	public IFunction[] getFunctions();
}