package de.osmembrane.model.persistence;


import java.io.Serializable;
import java.util.List;

import de.osmembrane.model.pipeline.AbstractFunction;
import de.osmembrane.model.pipeline.AbstractPipelineSettings;

/**
 * Represents a storable object for a pipeline.
 * 
 * @author jakob_jarosch
 */
public class PipelinePersistenceObject implements Serializable {

	private static final long serialVersionUID = 2011021314540001L;
	
	private List<AbstractFunction> functions;
	private AbstractPipelineSettings settings;
	
	/** 
	 * Creates a new {@link PipelinePersistenceObject}.
	 * 
	 * @param functions functions which should be saved
	 * @param settings settings which should be saved
	 */
	public PipelinePersistenceObject(List<AbstractFunction> functions, AbstractPipelineSettings settings) {
		this.settings = settings;
		this.functions = functions;
	}
	
	/**
	 * @return the functions of the pipeline.
	 */
	public List<AbstractFunction> getFunctions() {
		return functions;
	}
	
	/**
	 * @return the settings of the pipeline.
	 */
	public AbstractPipelineSettings getSettings() {
		return settings;
	}
	
}
