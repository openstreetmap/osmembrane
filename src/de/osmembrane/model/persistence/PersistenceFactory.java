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

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * Factory for generating an instance of a given {@link AbstractPersistence}.
 * 
 * @author jakob_jarosch
 */
public class PersistenceFactory extends Observable implements Observer {

    /**
     * Saves the instances from {@link AbstractPersistence} of this object.
     */
    private Map<Class<? extends AbstractPersistence>, AbstractPersistence> persistences = new HashMap<Class<? extends AbstractPersistence>, AbstractPersistence>();

    /**
     * Implements the Singleton pattern.
     */
    private static PersistenceFactory instance = new PersistenceFactory();

    /**
     * Initializes the PersistenceFactory.
     */
    private PersistenceFactory() {
    }

    /**
     * Getter for the Singleton pattern.
     * 
     * @return the one and only instance of PersistenceFactory
     */
    public static PersistenceFactory getInstance() {
        return instance;
    }

    /**
     * Returns a given {@link AbstractPersistence} for the given class name.
     * 
     * @param persistence
     *            class name of the AbstractPersistence
     * @return an instance of the given AbstractPersistence
     */
    public AbstractPersistence getPersistence(
            Class<? extends AbstractPersistence> persistence) {
        if (!persistences.containsKey(persistence)) {
            try {
                AbstractPersistence persistenceInstance = persistence
                        .newInstance();
                addObserver(persistenceInstance);

                persistences.put(persistence, persistenceInstance);
            } catch (Exception e) {
                /*
                 * If that happens, the method will later return NULL. Happens
                 * if constructor is not public or something like that.
                 */
            }
        }

        return persistences.get(persistence);
    }

    @Override
    public void update(Observable o, Object arg) {
        setChanged();
        notifyObservers(arg);
    }
}
