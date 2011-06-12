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

package de.osmembrane.model.persistence;

/**
 * Exception is thrown when while an IO-Operation went something wrong.
 * 
 * @author jakob_jarosch
 */
public class FileException extends Exception {

    private static final long serialVersionUID = 2011011213070001L;

    /**
     * Enumeration for categorizing the {@link FileException}.
     */
    public enum Type {
        /**
         * Is thrown when the file was not found.
         */
        NOT_FOUND,

        /**
         * Is thrown when the file was not readable.
         */
        NOT_READABLE,

        /**
         * Is thrown when the file was not writable.
         */
        NOT_WRITABLE,

        /**
         * Is thrown when the input- or output-data are in a wrong format, e.g.
         * a wrong has been loaded.
         */
        WRONG_FORMAT,

        /**
         * Is thrown when the input has some syntax errors and the parser can't
         * recognize the rest. See {@link FileException#getParentException()}
         * for details.
         */
        SYNTAX_PROBLEM,
    }

    private Type type;
    private Exception parentException;

    /**
     * @see FileException#FileException(Type, Exception)
     */
    public FileException(Type type) {
        this(type, null);
    }

    /**
     * Creates a {@link FileException} with a given type.
     * 
     * @param type
     *            of the {@link FileException}
     * @param parentException
     *            the Exception which is responsible for this exception
     */
    public FileException(Type type, Exception parentException) {
        this.type = type;
        this.parentException = parentException;
    }

    /**
     * Returns the type of the {@link FileException}.
     * 
     * @return type of the {@link FileException}
     */
    public Type getType() {
        return type;
    }

    /**
     * Returns the {@link Exception} which is responsible for this exception.
     * 
     * @return responsible {@link Exception} or NULL if none is available
     */
    public Exception getParentException() {
        return parentException;
    }
}
