package de.osmembrane.model.persistence;

import java.io.IOException;
import java.io.Serializable;
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
	 * @throws IOException
	 *             is thrown if the save fails (file not writable, ...)
	 */
	public abstract void save(String file, Object data) throws IOException;

	/**
	 * Loads a file and returns the object inside of it.
	 * 
	 * @param file
	 *            path to the file from where the object should be loaded
	 * @return the object, which is loaded from the file
	 * 
	 * @throws IOException
	 *             is thrown if the load fails (file not found, ...)
	 * @throws ClassNotFoundException
	 *             is thrown if the loaded class is not available. For example
	 *             when the file is saved under a newer version of OSMembrane.
	 * @throws ClassCastException
	 *             is thrown if the loaded object does not match to the required
	 *             object. For example when the system tries to open a
	 *             Settings-file as an OSMembrane-project-file.
	 */
	public abstract Object load(String file) throws IOException,
			ClassNotFoundException, ClassCastException;
}