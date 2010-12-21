package de.osmembrane.model.xml;

import java.util.List;
import java.util.Vector;
import de.osmembrane.model.xml.XMLTask;

public class XMLFunction extends XMLHasDescription {
	protected List _task;
	protected java.lang.String _id;
	protected java.lang.String _friendlyName;
	public XMLFunctionGroup _group_function;
	public Vector<XMLTask> _function_task = new Vector<XMLTask>();

	public List getTask() {
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