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

	public abstract void savePipeline(String filename);

	public abstract void loadPipeline(String filename);

	public abstract void importPipeline(String filename);

	public abstract void exportPipeline(String filename);

	public abstract void truncate();

	public abstract void generate(String filetype);
}