package de.osmembrane.model;

import java.util.Vector;
import de.osmembrane.model.Function;
import de.osmembrane.model.xml.XMLFunctionGroup;

public class FunctionGroup implements IFunctionGroup {
	public Vector<Function> _unnamed_Function_ = new Vector<Function>();

	public XMLFunctionGroup getXMLFunctionGroup() {
		throw new UnsupportedOperationException();
	}

	public IFunction[] getFunctions() {
		throw new UnsupportedOperationException();
	}
}