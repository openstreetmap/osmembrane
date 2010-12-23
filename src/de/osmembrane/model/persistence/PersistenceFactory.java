package de.osmembrane.model.persistence;

import java.util.ArrayList;
import java.util.Observable;

import de.osmembrane.model.persistence.AbstractPersistence;
import java.util.Observer;

public class PersistenceFactory implements Observer {
	public ArrayList<AbstractPersistence> unnamed_IPersistence_ = new ArrayList<AbstractPersistence>();

	public PersistenceFactory getInstance() {
		throw new UnsupportedOperationException();
	}

	public void getPersistence(Class<AbstractPersistence> clazz) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void update(Observable o, Object arg) {
		throw new UnsupportedOperationException();
	}
}