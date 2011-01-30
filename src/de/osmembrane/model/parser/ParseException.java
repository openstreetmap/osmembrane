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
		UNKNOWN_TASK_FORMAT,
		
		/**
		 * The task has a connection which is not allowed to be one.
		 */
		CONNECTION_NOT_PERMITTED,
		
		/** 
		 * The pipe-stream direction could not be recognized implicit.
		 */
		UNKNOWN_PIPE_STREAM,
	}

	private ErrorType type;
	private String message;
	
	/**
	 * Creates a new {@link ParseException} with a given {@link ErrorType}.
	 * 
	 * @param type corresponding type for the {@link ParseException}
	 */
	public ParseException(ErrorType type) {
		this(type, null);
	}
	
	public ParseException(ErrorType type, String message) {
		this.type = type;
		this.message = message;
	}

	/**
	 * Returns the {@link ErrorType} of the {@link ParseException}.
	 * 
	 * @return {@link ErrorType} of the {@link ParseException}
	 */
	public ErrorType getType() {
		return type;
	}
	
	@Override
	public String getMessage() {
		return message;
	}
}
