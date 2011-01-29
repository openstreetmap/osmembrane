package de.osmembrane.model.parser;

/**
 * Is thrown when something is not right while parsing.
 * 
 * @author jakob_jarosch
 */
public class ParseException extends Exception {

	private static final long serialVersionUID = 2011011214380001L;
	
	/**
	 * type of the {@link ParseException}.
	 */
	public enum ErrorType {
		
		/**
		 * There is no known Task with this name.
		 */
		UNKNOWN_TASK,
		
		/**
		 * The task has an unknown format, e.g. a parameter is not known, or the pipes do not match.
		 */
		UNKNOWN_TASK_FORMAT
	}

	private ErrorType type;
	
	/**
	 * Creates a new {@link ParseException} with a given {@link ErrorType}.
	 * 
	 * @param type corresponding type for the {@link ParseException}
	 */
	public ParseException(ErrorType type) {
		this.type = type;
	}
	
	/**
	 * Returns the {@link ErrorType} of the {@link ParseException}.
	 * 
	 * @return {@link ErrorType} of the {@link ParseException}
	 */
	public ErrorType getType() {
		return type;
	}
}
