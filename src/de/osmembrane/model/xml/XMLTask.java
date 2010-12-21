package de.osmembrane.model.xml;

import java.util.List;
import java.util.ArrayList;
import de.osmembrane.model.xml.XMLPipe;
import de.osmembrane.model.xml.XMLParameter;

public class XMLTask extends XMLHasDescription {
	protected List inputPipe;
	protected List outputPipe;
	protected List parameter;
	protected java.lang.String name;
	protected java.lang.String shortName;
	protected java.lang.String friendlyName;
	protected java.lang.String helpURI;
	public XMLFunction function_task;
	public ArrayList<XMLPipe> in__outpipeline = new ArrayList<XMLPipe>();
	public ArrayList<XMLParameter> tasks_parameter = new ArrayList<XMLParameter>();

	public List getInputPipe() {
		throw new UnsupportedOperationException();
	}

	public List getOutputPipe() {
		throw new UnsupportedOperationException();
	}

	public List getParameter() {
		throw new UnsupportedOperationException();
	}

	public java.lang.String getName() {
		throw new UnsupportedOperationException();
	}

	public void setName(java.lang.String value) {
		throw new UnsupportedOperationException();
	}

	public java.lang.String getShortName() {
		throw new UnsupportedOperationException();
	}

	public void setShortName(java.lang.String value) {
		throw new UnsupportedOperationException();
	}

	public java.lang.String getFriendlyName() {
		throw new UnsupportedOperationException();
	}

	public void setFriendlyName(java.lang.String value) {
		throw new UnsupportedOperationException();
	}

	public java.lang.String getHelpURI() {
		throw new UnsupportedOperationException();
	}

	public void setHelpURI(java.lang.String value) {
		throw new UnsupportedOperationException();
	}
}