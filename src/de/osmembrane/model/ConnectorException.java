package de.osmembrane.model;

/**
 * Exception for reporting a failed connection between two
 * {@link AbstractFunction}s.
 * 
 * @author jakob_jarosch
 */
public class ConnectorException extends Exception {

	private static final long serialVersionUID = 2011010722360001L;
	
	private Problem problem;

	/**
	 * Creates a new {@link ConnectorException} with a given {@link Problem}.
	 * @param problem
	 */
	public ConnectorException(Problem problem) {
		this.problem = problem;
	}

	/**
	 * Returns the {@link Problem} of the {@link ConnectorException}.
	 * 
	 * @return {@link Problem} of the {@link ConnectorException}
	 */
	public Problem getProblem() {
		return problem;
	}
}

enum Problem {
	NO_MATCH, FULL
}