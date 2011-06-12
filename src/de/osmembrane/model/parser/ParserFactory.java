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

/**
 * Factory for Parser-Instances.
 * 
 * @author jakob_jarosch
 */
public class ParserFactory {

    /**
     * Implements the Singleton pattern.
     */
    private static ParserFactory instance = new ParserFactory();

    /**
     * Initiates the ModelProxy.
     */
    private ParserFactory() {

    }

    /**
     * Getter for the Singleton pattern.
     * 
     * @return the one and only instance of ParserFactory
     */
    public static ParserFactory getInstance() {
        return instance;
    }

    /**
     * Returns a given parser-Class as a instance.
     * 
     * @param clazz
     *            which should be created
     */
    public IParser getParser(Class<? extends IParser> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            /* should not happen */
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            /* should not happen */
            e.printStackTrace();
        }

        return null;
    }
}
