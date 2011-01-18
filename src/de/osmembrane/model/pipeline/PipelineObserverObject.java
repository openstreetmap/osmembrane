package de.osmembrane.model.pipeline;

/**
 * Object which is passed through by the {@link AbstractPipeline}.
 * 
 * @author jakob_jarosch
 */
public class PipelineObserverObject {

	/**
	 * Shows, what has been changed.
	 * 
	 * @author jakob_jarosch
	 */
	public enum ChangeType {
		/**
		 * A new function has been added.
		 */
		ADD,

		/**
		 * A function has been changed (modified parameters, moved, ...).
		 */
		CHANGE,

		/**
		 * A function has been deleted.
		 */
		DELETE,

		/**
		 * A full change of the pipeline.
		 */
		FULLCHANGE
	};

	/**
	 * Type of the {@link PipelineObserverObject}.
	 */
	private ChangeType type;

	/**
	 * Function which has been changed (when type is {@link ChangeType#ADD},
	 * {@link ChangeType#CHANGE} or {@link ChangeType#DELETE}).
	 */
	private AbstractFunction changedFunction = null;

	/**
	 * Pipeline on which the event occurred.
	 */
	private AbstractPipeline pipeline;

	/**
	 * Creates a new {@link PipelineObserverObject}.
	 * 
	 * @param type
	 *            type of the observer-notification.
	 * @param changedFunction
	 *            function which has been changed ({@see
	 *            PipelineObserverObject#changedFunction}).
	 */
	public PipelineObserverObject(ChangeType type,
			AbstractFunction changedFunction) {
		this.type = type;
		this.changedFunction = changedFunction;
	}

	/**
	 * Returns the type of the {@link PipelineObserverObject}.
	 * 
	 * @return type of the observer-notification
	 */
	public ChangeType getType() {
		return type;
	}

	/**
	 * Returns the changed {@link AbstractFunction}. ({@see
	 * PipelineObserverObject#changedFunction})
	 * 
	 * @return changed function
	 */
	public AbstractFunction getChangedFunction() {
		return changedFunction;
	}

	/**
	 * Sets the used {@link AbstractPipeline} at a later point.
	 * @param pipeline
	 */
	public void setPipeline(AbstractPipeline pipeline) {
		this.pipeline = pipeline;
	}

	/**
	 * Returns the {@link AbstractPipeline} on which the event occurred.
	 * 
	 * @return the used pipeline
	 */
	public AbstractPipeline getPipeline() {
		return pipeline;
	}
}