package de.osmembrane.model.pipeline;

/**
 * Defines the type how a function should be copied.
 * 
 * @author jakob_jarosch
 */
public enum CopyType {
	/**
	 * Copy everything.
	 */
	COPY_ALL,

	/**
	 * Copy all without the values of a task, and without the activeTask of a
	 * function.
	 */
	WITHOUT_VALUES,

	/**
	 * Copy all without the connections of the connectors.
	 */
	WITHOUT_POSITION,

	/**
	 * @see CopyType#WITHOUT_VALUES
	 * @see CopyType#WITHOUT_POSITION
	 */
	WITHOUT_VALUES_AND_POSITION;

	protected boolean copyPosition() {
		return (this != WITHOUT_POSITION && this != WITHOUT_VALUES_AND_POSITION);
	}

	protected boolean copyValues() {
		return (this != WITHOUT_VALUES && this != WITHOUT_VALUES_AND_POSITION);
	}
}