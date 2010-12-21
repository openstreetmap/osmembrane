package de.osmembrane.model.persistence;

import java.util.ArrayList;
import de.osmembrane.model.persistence.IPersistence;
import java.util.Observer;

public class PersistenceFactory implements Observer {
	public ArrayList<IPersistence> unnamed_IPersistence_ = new ArrayList<IPersistence>();

	public PersistenceFactory getInstance() {
		throw new UnsupportedOperationException();
	}

	public void getPersistence(Class<IPersistence> clazz) {
		throw new UnsupportedOperationException();
	}
}