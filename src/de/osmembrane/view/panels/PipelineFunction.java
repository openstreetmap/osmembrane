package de.osmembrane.view.panels;

import de.osmembrane.model.pipeline.AbstractFunction;

/**
 * The pipeline function, i.e. the visual representation of a model function
 * that is actually drawn in the pipeline. Note, the functions in the
 * {@link LibraryPanel} and the one being dragged on the {@link PipelinePanel}
 * are just {@link ViewFunction}.
 * 
 * @author tobias_kuhn
 * 
 */
public class PipelineFunction extends ViewFunction {

	private static final long serialVersionUID = -7573627124702293974L;

	private AbstractFunction modelFunction;

	/**
	 * Creates a new pipeline function from an AbstractFunction out of the model
	 * 
	 * @param modelFunction
	 *            the function out of the model
	 */
	public PipelineFunction(AbstractFunction modelFunction) {
		// pretend this is a prototype
		super(modelFunction);
		this.modelFunction = modelFunction;
	}
	
	/**
	 * @return the model function
	 */
	public AbstractFunction getModelFunction() {
		return this.modelFunction;
	}

}
