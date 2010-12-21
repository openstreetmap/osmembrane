package de.osmembrane.model.xml;

import java.util.List;
import java.util.Vector;
import de.osmembrane.model.xml.XMLFunction;

public class XMLFunctionGroup extends XMLHasDescription {
	protected List _function;
	protected java.lang.String _id;
	protected java.lang.String _friendlyName;
	public XMLOsmosisStructure _unnamed_XMLOsmosisStructure_;
	public Vector<XMLFunction> _group_function = new Vector<XMLFunction>();

	public List getFunction() {
		throw new UnsupportedOperationException();
	}

	public java.lang.String getId() {
		throw new UnsupportedOperationException();
	}

	public void setId(java.lang.String aValue) {
		throw new UnsupportedOperationException();
	}

	public java.lang.String getFriendlyName() {
		throw new UnsupportedOperationException();
	}

	public void setFriendlyName(java.lang.String aValue) {
		throw new UnsupportedOperationException();
	}
}