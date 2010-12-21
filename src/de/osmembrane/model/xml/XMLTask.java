package de.osmembrane.model.xml;

import java.util.List;
import java.util.Vector;
import de.osmembrane.model.xml.XMLPipe;
import de.osmembrane.model.xml.XMLParameter;

public class XMLTask extends XMLHasDescription {
	protected List _inputPipe;
	protected List _outputPipe;
	protected List _parameter;
	protected java.lang.String _name;
	protected java.lang.String _shortName;
	protected java.lang.String _friendlyName;
	protected java.lang.String _helpURI;
	public XMLFunction _function_task;
	public Vector<XMLPipe> _in__outpipeline = new Vector<XMLPipe>();
	public Vector<XMLParameter> _tasks_parameter = new Vector<XMLParameter>();

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

	public void setName(java.lang.String aValue) {
		throw new UnsupportedOperationException();
	}

	public java.lang.String getShortName() {
		throw new UnsupportedOperationException();
	}

	public void setShortName(java.lang.String aValue) {
		throw new UnsupportedOperationException();
	}

	public java.lang.String getFriendlyName() {
		throw new UnsupportedOperationException();
	}

	public void setFriendlyName(java.lang.String aValue) {
		throw new UnsupportedOperationException();
	}

	public java.lang.String getHelpURI() {
		throw new UnsupportedOperationException();
	}

	public void setHelpURI(java.lang.String aValue) {
		throw new UnsupportedOperationException();
	}
}