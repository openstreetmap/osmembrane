package de.osmembrane.model.pipeline;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import de.osmembrane.model.xml.XMLParameter;
import de.osmembrane.model.xml.XMLPipe;
import de.osmembrane.model.xml.XMLTask;
import de.osmembrane.tools.I18N;

/**
 * Implementation of {@link AbstractTask}.
 * 
 * @author jakob_jarosch
 */
public class Task extends AbstractTask {

	private static final long serialVersionUID = 2011011821570001L;

	/**
	 * The {@link XMLTask} which is represented by this instance.
	 */
	private XMLTask xmlTask;
	
	/**
	 * Parameters which are bound to this Task.
	 */
	private List<Parameter> parameters = new ArrayList<Parameter>();
	
	/**
	 * Creates a new Task.
	 * 
	 * @param xmlTask {@link XMLTask} which will be represented by this instance.
	 */
	public Task(XMLTask xmlTask) {
		this.xmlTask = xmlTask;
		
		for (XMLParameter xmlParam : xmlTask.getParameter()) {
			Parameter param = new Parameter(xmlParam);
			param.addObserver(this);
			parameters.add(param);
		}
	}

	@Override
	public String getDescription() {
		return I18N.getInstance().getDescription(xmlTask);
	}

	@Override
	public String getName() {
		return xmlTask.getName();
	}

	@Override
	public String getShortName() {
		return xmlTask.getShortName();
	}

	@Override
	public String getHelpURI() {
		return xmlTask.getHelpURI();
	}

	@Override
	public String getFriendlyName() {
		return xmlTask.getFriendlyName();
	}
	
	@Override
	public Parameter[] getParameters() {
		Parameter[] parameters = new Parameter[this.parameters.size()];
		return this.parameters.toArray(parameters);
	}
	
	@Override
	protected List<XMLPipe> getInputXMLPipe() {
		return xmlTask.getInputXMLPipe();
	}

	@Override
	protected List<XMLPipe> getOutputXMLPipe() {
		return xmlTask.getOutputXMLPipe();
	}

	@Override
	public void update(Observable o, Object arg) {
		/* A parameter got a change (anything changed) */
		setChanged();
		notifyObservers();
	}

}
