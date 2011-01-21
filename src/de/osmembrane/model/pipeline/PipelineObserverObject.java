package de.osmembrane.model.pipeline;

import de.osmembrane.Application;
import de.osmembrane.exceptions.ControlledException;
import de.osmembrane.exceptions.ExceptionSeverity;
import de.osmembrane.tools.I18N;

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
		ADD_FUNCTION,

		/**
		 * A function has been changed (modified parameters, moved, ...).
		 */
		CHANGE_FUNCTION,

		/**
		 * A function has been deleted.
		 */
		DELETE_FUNCTION,

		/**
		 * A connection has been created.
		 */
		ADD_CONNECTION,

		/**
		 * A connection has been removed.
		 */
		DELETE_CONNECTION,

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
	 * Function which has been changed.<br/>
	 * Type must be {@link ChangeType#ADD_FUNCTION},
	 * {@link ChangeType#CHANGE_FUNCTION} or {@link ChangeType#DELETE_FUNCTION}.
	 */
	private AbstractFunction changedFunction = null;

	/**
	 * Connectors which has been changed (added, removed connection between
	 * both).<br/>
	 * Type must be {@link ChangeType#ADD_CONNECTION} or
	 * {@link ChangeType#DELETE_CONNECTION}.
	 */
	private AbstractConnector changedOutConnector;

	private AbstractConnector changedInConnector;

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
	 * Creates a new {@link PipelineObserverObject}.
	 * 
	 * @param type
	 *            type of the observer notification.<br/>
	 *            Should be here {@link ChangeType#ADD_CONNECTION} or
	 *            {@link ChangeType#DELETE_CONNECTION}.
	 * @param outConnector
	 *            connector where the connection begins
	 * @param inConnector
	 *            connector where the connection ends
	 */
	public PipelineObserverObject(ChangeType type,
			AbstractConnector outConnector, AbstractConnector inConnector) {
		this.type = type;
		this.changedOutConnector = outConnector;
		this.changedInConnector = inConnector;
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
	 * Returns the changed {@link AbstractConnector}s. ({@see
	 * PipelineObserverObject#changedConnectors})
	 * 
	 * @return changed connectors, array-length is always 2.<br/>
	 *         first one is out connector, second one in connector.
	 */
	public AbstractConnector[] getChangedConnectors() {
		AbstractConnector[] connectors = { changedOutConnector,
				changedInConnector };
		return connectors;
	}

	/**
	 * Sets the used {@link AbstractPipeline} at a later point.
	 * 
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