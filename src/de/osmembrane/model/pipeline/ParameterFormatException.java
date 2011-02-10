package de.osmembrane.model.pipeline;

/**
 * Exception is thrown if a parameter is not right formatted.
 * 
 * @author jakob_jarosch
 */
public class ParameterFormatException extends RuntimeException {

	private static final long serialVersionUID = 2011021020590001L;

	/**
	 * Type of the wrong format exception.
	 */
	public enum Type {

		/**
		 * The default exception type.
		 */
		DEFAULT,
	}

	private Type type;

	/**
	 * Creates a new {@link ParameterFormatException}.
	 * 
	 * @param type
	 *            type of the exception
	 */
	public ParameterFormatException(Type type) {
		this.type = type;
	}

	/**
	 * Returns the type of the exception.
	 * 
	 * @return type of the exception
	 */
	public Type getType() {
		return type;
	}
}
