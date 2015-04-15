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
 * Enumeration for categorizing the severity an Exception.
 * 
 * @author jakob_jarosch
 */
public enum ExceptionSeverity {

    /**
     * don't use
     */
    INVALID,

    /**
     * a warning has occurred to inform the user for example about failed
     * IO-operations, failed backups, unsaved settings
     */
    WARNING,

    /**
     * generic error, thrown, when the program does not work like it should
     * (developer should be informed at this point)
     */
    UNEXPECTED_BEHAVIOR,

    /**
     * like {@link ExceptionSeverity#UNEXPECTED_BEHAVIOR} but the program's
     * integrity cannot be guaranteed anymore and it should be closed after the
     * exception
     */
    CRITICAL_UNEXPECTED_BEHAVIOR;
}
