package de.osmembrane.model.persistence;

public abstract class AbstractPersistence {

	public abstract void save(String file, Object data);

	public abstract Object load(String file);
}