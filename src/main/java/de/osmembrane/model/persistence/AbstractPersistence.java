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

import java.io.Serializable;
import java.net.URL;
import java.util.Observer;

/**
 * AbstractPersistence provides two methods save and load, so all
 * Persistence-classes are used in the same way.
 * 
 * @author jakob_jarosch
 */
public abstract class AbstractPersistence implements Observer {

    /**
     * Saves a given object into the given file.
     * 
     * @param file
     *            path to the file where the object should be saved
     * @param data
     *            object which should be saved into the file, normally it should
     *            be {@link Serializable}
     * 
     * @throws FileException
     */
    public abstract void save(URL file, Object data) throws FileException;

    /**
     * Loads a file and returns the object inside of it.
     * 
     * @param file
     *            path to the file from where the object should be loaded
     * @return the object, which is loaded from the file
     * 
     * @throws FileException
     */
    public abstract Object load(URL file) throws FileException;
}
