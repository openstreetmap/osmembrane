package de.osmembrane.model;

/**
 * The ObserverObject includes information about the reason of notifying.
 * 
 * @author jakob_jarosch
 */
public class ObserverObject {

	private Class<?> clazz;
	private String string;
	private Object object;

	/**
	 * @see ObserverObject#ObserverObject(Class, String, Object)
	 */
	public ObserverObject(Class<?> clazz) {
		this(clazz, null, null);
	}

	/**
	 * @see ObserverObject#ObserverObject(Class, String, Object)
	 */
	public ObserverObject(Class<?> clazz, String string) {
		this(clazz, string, null);
	}

	/**
	 * Creates a new ObserverObject instance.
	 * 
	 * @param clazz
	 *            class name of the triggered class.
	 * @param string
	 *            a string with some information for the consuming Observer
	 * @param object
	 *            a object with some extended information for the consuming
	 *            Observer
	 */
	public ObserverObject(Class<?> clazz, String string, Object object) {
		this.clazz = clazz;
		this.string = string;
		this.object = object;
	}

	/**
	 * Returns the class of the triggered class.
	 * 
	 * @return class of the trigger
	 */
	public Class<?> getClazz() {
		return clazz;
	}

	/**
	 * Returns a string with some information for the consuming Observer.
	 * 
	 * @return a string with some information
	 */
	public String getString() {
		return string;
	}

	/**
	 * Returns a object with some extended information for the consuming
	 * Observer.
	 * 
	 * @return a object with some extended information
	 */
	public Object getObject() {
		return object;
	}
}
