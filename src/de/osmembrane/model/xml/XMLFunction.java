package de.osmembrane.model.xml;

import java.util.List;
import java.util.ArrayList;
import de.osmembrane.model.xml.XMLTask;

public class XMLFunction extends XMLHasDescription {
	protected List task;
	protected java.lang.String id;
	protected java.lang.String friendlyName;
	public XMLFunctionGroup group_function;
	public ArrayList<XMLTask> function_task = new ArrayList<XMLTask>();

	public List getTask() {
		throw new UnsupportedOperationException();
	}

	public java.lang.String getId() {
		throw new UnsupportedOperationException();
	}

	public void setId(java.lang.String value) {
		throw new UnsupportedOperationException();
	}

	public java.lang.String getFriendlyName() {
		throw new UnsupportedOperationException();
	}

	public void setFriendlyName(java.lang.String value) {
		throw new UnsupportedOperationException();
	}
}