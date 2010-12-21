package de.osmembrane.model;

import IPipelien.FILE_TYPE;

public interface IPipeline implements de.osmembrane.model.java.io.Serializable {

	public boolean redo();

	public void addFunction(IFunction aFunc);

	public void deleteFunction(IFunction aFunc);

	public IFunction[] getFunctions();

	public boolean undo();

	public boolean optimizeGraph();

	public boolean execute();

	public void save(String aFilename);

	public void load(String aFilename);

	public void import_325(String aFilename);

	public void export(String aFilename);

	public void truncate();

	public void generate(FILE_TYPE aFiletype);
}