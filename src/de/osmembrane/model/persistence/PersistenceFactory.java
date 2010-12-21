package de.osmembrane.model.persistence;

import java.util.Vector;
import de.osmembrane.model.persistence.IPersistence;
import de.osmembrane.model.java.util.Observer;

public class PersistenceFactory implements Observer {
	public Vector<IPersistence> _unnamed_IPersistence_ = new Vector<IPersistence>();

	public PersistenceFactory getInstance() {
		throw new UnsupportedOperationException();
	}

	public void getPersistence(Class<IPersistence> aClazz) {
		throw new UnsupportedOperationException();
	}
}