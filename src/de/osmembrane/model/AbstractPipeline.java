package de.osmembrane.model;

import java.util.Observable;

public abstract class AbstractPipeline extends Observable {

	public abstract boolean redo();

	public abstract void addFunction(AbstractFunction func);

	public abstract void deleteFunction(AbstractFunction func);

	public abstract AbstractFunction[] getFunctions();

	public abstract boolean undo();

	public abstract boolean optimizeGraph();

	public abstract boolean execute();

	public abstract void save(String filename);

	public abstract void load(String filename);

	public abstract void import_326(String filename);

	public abstract void export(String filename);

	public abstract void truncate();

	public abstract void generate(String filetype);
}