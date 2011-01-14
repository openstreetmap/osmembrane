package de.osmembrane.model.pipeline;

import java.io.Serializable;
import java.util.List;
import java.util.Observable;

import de.osmembrane.model.xml.XMLPipe;

public abstract class AbstractTask extends Observable implements Serializable {

	public abstract String getDescription();
	
	public abstract String getName();
	
	public abstract String getShortName();
	
	public abstract String getFriendlyName();
	
	public abstract String getHelpURI();
	
	public abstract AbstractParameter[] getParameters();
	
	protected abstract List<XMLPipe> getInputXMLPipe();
	
	protected abstract List<XMLPipe> getOutputXMLPipe();
}
