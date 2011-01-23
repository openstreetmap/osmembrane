package de.osmembrane.model.pipeline;

import java.util.Observable;

import de.osmembrane.model.Identifier;
import de.osmembrane.model.xml.XMLEnumValue;
import de.osmembrane.model.xml.XMLFunction;
import de.osmembrane.model.xml.XMLParameter;
import de.osmembrane.model.xml.XMLPipe;
import de.osmembrane.model.xml.XMLTask;


/**
 * This is an Prototype for accessing the XMLFunctions.
 * 
 * @author jakob_jarosch
 */
public abstract class AbstractFunctionPrototype extends Observable {

	/**
	 * Initiates the Prototype with a given compatible XML-file.
	 * 
	 * @param xmlFilename filename of the given XML-file
	 */
	public abstract void initiate(String xmlFilename);

	public abstract AbstractFunctionGroup[] getFunctionGroups();
	
	protected abstract AbstractFunctionGroup getMatchingFunctionGroup(Identifier identifier);
	protected abstract Identifier getMatchingFunctionGroupIdentifier(AbstractFunctionGroup identifier);
	
	protected abstract XMLFunction getMatchingXMLFunction(Identifier identifier);
	protected abstract Identifier getMatchingXMLFunctionIdentifier(XMLFunction identifier);
	
	protected abstract XMLTask getMatchingXMLTask(Identifier identifier);
	protected abstract Identifier getMatchingXMLTaskIdentifier(XMLTask identifier);
	
	protected abstract XMLPipe getMatchingXMLPipe(Identifier identifier);
	protected abstract Identifier getMatchingXMLPipeIdentifier(XMLPipe identifier);
	
	protected abstract XMLParameter getMatchingXMLParameter(Identifier identifier);
	protected abstract Identifier getMatchingXMLParameterIdentifier(XMLParameter identifier);
	
	protected abstract XMLEnumValue getMatchingXMLEnumValue(Identifier identifier);
	protected abstract Identifier getMatchingXMLEnumValueIdentifier(XMLEnumValue identifier);

	
}