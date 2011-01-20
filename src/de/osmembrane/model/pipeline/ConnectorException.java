package de.osmembrane.model.pipeline;

/**
 * Exception for reporting a failed connection between two
 * {@link AbstractFunction}s.
 * 
 * @author jakob_jarosch
 */
public class ConnectorException extends Exception {

	private static final long serialVersionUID = 2011010722360001L;

	public enum Type {
		NO_MATCH, FULL, LOOP_CREATED
	}

	private Type type;

	/**
	 * Creates a new {@link ConnectorException} with a given {@link Type}.
	 * 
	 * @param type
	 */
	public ConnectorException(Type type) {
		this.type = type;
	}

	/**
	 * Returns the {@link Type} of the {@link ConnectorException}.
	 * 
	 * @return {@link Type} of the {@link ConnectorException}
	 */
	public Type getType() {
		return type;
	}
}