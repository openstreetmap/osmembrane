package de.osmembrane.model.persistence;

public interface IPersistence {

	public void save(String aFile, Object aData);

	public Object load(String aFile);
}