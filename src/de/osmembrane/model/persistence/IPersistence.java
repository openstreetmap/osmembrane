package de.osmembrane.model.persistence;

public interface IPersistence {

	public void save(String file, Object data);

	public Object load(String file);
}