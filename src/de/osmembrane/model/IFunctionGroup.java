package de.osmembrane.model;

import de.osmembrane.model.xml.XMLFunctionGroup;

public interface IFunctionGroup {

	public XMLFunctionGroup getXMLFunctionGroup();

	public IFunction[] getFunctions();
}