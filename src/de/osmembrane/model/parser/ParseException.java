/*
 * This file is part of the OSMembrane project.
 * More informations under www.osmembrane.de
 * 
 * The project is licensed under the GNU GENERAL PUBLIC LICENSE 3.0.
 * for more details about the license see http://www.osmembrane.de/license/
 * 
 * Source: $HeadURL$ ($Revision$)
 * Last changed: $Date$
 */

package de.osmembrane.model.parser;

import de.osmembrane.tools.I18N;

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
         * The task has an unknown format, e.g. a parameter is not known, or the
         * pipes do not match.
         */
        UNKNOWN_TASK_FORMAT,

        /**
         * Found no default parameter for the task.
         */
        NO_DEFAULT_PARAMETER_FOUND,

        /**
         * The task has a connection which is not allowed to be one.
         */
        CONNECTION_NOT_PERMITTED,

        /**
         * The pipe-stream direction could not be recognized implicit.
         */
        UNKNOWN_PIPE_STREAM,

        /**
         * The defined inPipe for a task has no counterpart task with a outPipe.
         */
        COUNTERPART_PIPE_MISSING,

        /**
         * The given parameter has an invalid value.
         */
        INVALID_PARAMETER_VALUE,
    }

    private ErrorType type;
    private Object[] messageValues;

    /**
     * Creates a new {@link ParseException} with a given {@link ErrorType}.
     * 
     * @param type
     *            corresponding type for the {@link ParseException}
     * @param messageValues
     *            messages which are used by the exception message
     */
    public ParseException(ErrorType type, Object... messageValues) {
        this.type = type;
        this.messageValues = messageValues;
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
        return I18N.getInstance().getString("Model.Parser.Exceptions." + type,
                messageValues);
    }
}
