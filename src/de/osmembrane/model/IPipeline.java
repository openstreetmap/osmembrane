package de.osmembrane.model;

public interface IPipeline {

	public boolean redo();

	public void addFunction(IFunction func);

	public void deleteFunction(IFunction func);

	public IFunction[] getFunctions();

	public boolean undo();

	public boolean optimizeGraph();

	public boolean execute();

	public void save(String filename);

	public void load(String filename);

	public void import_326(String filename);

	public void export(String filename);

	public void truncate();

	public void generate(String filetype);
}