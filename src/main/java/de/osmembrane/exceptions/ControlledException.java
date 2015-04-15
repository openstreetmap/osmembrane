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

package de.osmembrane.exceptions;

/**
 * The general exception class that can be invoked anywhere within the program
 * and is understood by the Controller to fit unto the View's error handling
 * dialog.
 * 
 * If you mean to create a separate ControlledException, please specify an
 * exception message so users can figure out, what has happened.
 * 
 * @author tobias_kuhn
 * 
 */
public class ControlledException extends Exception {

    private static final long serialVersionUID = -5332782603656616624L;

    private Object causingObject;

    private ExceptionSeverity severity;

    /**
     * Creates a new ControlledException.
     * 
     * @param causingObject
     *            the object which caused the exception
     * @param severity
     *            the severity of the exception
     * @param message
     *            the message associated with the exception
     */
    public ControlledException(Object causingObject,
            ExceptionSeverity severity, String message) {
        super(message);
        this.causingObject = causingObject;
        this.severity = severity;
    }

    /**
     * Creates a new ControlledException.
     * 
     * @param causingObject
     *            the object which caused the exception
     * @param severity
     *            the severity of the exception
     * @param cause
     *            the cause associated with the exception
     * @deprecated When only using the cause, it is not useful to create a
     *             separate ControlledException. Instead, <b>please give a
     *             describing message</b>!
     */
    @Deprecated
    public ControlledException(Object causingObject,
            ExceptionSeverity severity, Throwable cause) {
        super(cause);
        this.causingObject = causingObject;
        this.severity = severity;
    }

    /**
     * Creates a new ControlledException.
     * 
     * @param causingObject
     *            the object which caused the exception
     * @param severity
     *            the severity of the exception
     * @param cause
     *            the cause associated with the exception
     * @param message
     *            the message associated with the exception
     */
    public ControlledException(Object causingObject,
            ExceptionSeverity severity, Throwable cause, String message) {
        super(message, cause);
        this.causingObject = causingObject;
        this.severity = severity;
    }

    /**
     * @return the causingClass
     */
    public Object getCausingObject() {
        return causingObject;
    }

    /**
     * @return the kind
     */
    public ExceptionSeverity getKind() {
        return severity;
    }

}
