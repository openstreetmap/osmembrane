package de.osmembrane.view;

/**
 * Enum for categorizing an Exception.
 * 
 * @author jakob_jarosch
 */
public enum ExceptionType {

	/** An IO-Operation failed. */
	IO_FAILED(ErrorLevel.ERROR),

	/** An IO-Operation succeed. */
	IO_SUCCEED(ErrorLevel.NOTIFICATION),

	/** automatic backup failed */
	BACKUP_FAILED(ErrorLevel.ERROR),

	/** automatic backup succeed */
	BACKUP_SUCCEED(ErrorLevel.NOTIFICATION),

	/** automatic saving settings failed */
	SAVE_SETTINGS_FAILED(ErrorLevel.ERROR),

	/** automatic saving settings succeed */
	SAVE_SUCCEED_FAILED(ErrorLevel.NOTIFICATION),

	/**
	 * generic error, thrown, when the program does not work like it should
	 * (developer should be informed at this point)
	 */
	ABNORMAL_BEHAVIOR(ErrorLevel.ERROR),

	/** like {@link ExceptionType#ABNORMAL_BEHAVIOR} but more critical */
	CRITICAL_ABNORMAL_BEHAVIOR(ErrorLevel.CRITICAL_ERROR);

	/* *********************************************************************** */

	/**
	 * Error levels from low to high.
	 * 
	 * NOTIFICATION: inform the View about something (must not be a problem)<br/>
	 * WARNING: a small problem occurred<br/>
	 * ERROR: a error what could follow-up an unexpected program-behavior<br/>
	 * CRITICAL_ERROR: a error which crashed the whole program
	 */
	enum ErrorLevel {
		NOTIFICATION, WARNING, ERROR, CRITICAL_ERROR
	}

	private ErrorLevel level;

	/**
	 * Internal constructor.
	 */
	ExceptionType(ErrorLevel level) {
		this.level = level;
	}

	/**
	 * Returns the ErrorLevel of the Exception.
	 * 
	 * @return
	 */
	public ErrorLevel getErrorLevel() {
		return level;
	}
}