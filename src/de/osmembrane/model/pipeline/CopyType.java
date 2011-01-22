package de.osmembrane.model.pipeline;

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
	WITHOUT_CONNECTIONS,

	/**
	 * @see CopyType#WITHOUT_VALUES
	 * @see CopyType#WITHOUT_CONNECTIONS
	 */
	WITHOUT_VALUES_AND_CONNECTIONS;

	protected boolean copyConnections() {
		return (this != WITHOUT_CONNECTIONS && this != WITHOUT_VALUES_AND_CONNECTIONS);
	}

	protected boolean copyValues() {
		return (this != WITHOUT_VALUES && this != WITHOUT_VALUES_AND_CONNECTIONS);
	}
}