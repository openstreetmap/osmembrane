package de.osmembrane.model.pipeline;

import java.io.ObjectStreamException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import de.osmembrane.model.Identifier;
import de.osmembrane.model.ModelProxy;
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
	transient private XMLTask xmlTask;
	private Identifier xmlTaskIdentifier;
	
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
		
		/* set the identifier */
		AbstractFunctionPrototype afp = ModelProxy.getInstance().accessFunctions();
		this.xmlTaskIdentifier = afp.getMatchingXMLTaskIdentifier(this.xmlTask);
		
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
		/* fallback if friendlyName is not available */
		if (xmlTask.getFriendlyName() == null) {
			return getName();
		}
		
		return getName() + " (" + xmlTask.getFriendlyName() + ")";
	}
	
	@Override
	public Parameter[] getParameters() {
		Parameter[] parameters = new Parameter[this.parameters.size()];
		return this.parameters.toArray(parameters);
	}
	
	@Override
	protected List<XMLPipe> getInputPipe() {
		return xmlTask.getInputPipe();
	}

	@Override
	protected List<XMLPipe> getOutputPipe() {
		return xmlTask.getOutputPipe();
	}
	
	@Override
	public void update(Observable o, Object arg) {
		/* A parameter got a change (anything changed) */
		setChanged();
		notifyObservers();
	}

	@Override
	public Task copy(CopyType type) {
		Task newTask = new Task(this.xmlTask);
		
		/* copy the parameters */
		newTask.parameters.clear();
		for(Parameter param : this.parameters) {
			Parameter newParam = param.copy(type);
			newParam.addObserver(newTask);
			newTask.parameters.add(newParam);
		}
		
		return newTask;
	}
	
	private Object readResolve() throws ObjectStreamException {
		AbstractFunctionPrototype afp = ModelProxy.getInstance().accessFunctions();
		this.xmlTask = afp.getMatchingXMLTask(this.xmlTaskIdentifier);
		
		/* create the observers */
		for (Parameter param : parameters) {
			param.addObserver(this);
		}
		
		return this;
	}
}
