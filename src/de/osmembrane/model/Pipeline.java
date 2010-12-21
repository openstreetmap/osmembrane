package de.osmembrane.model;

import IPipelien.FILE_TYPE;

public class Pipeline extends de.osmembrane.model.java.util.Observable implements IPipeline {
	private Stack<pipelineMemento> _undoStack;
	private List<Function> _functions;
	private Stack<pipelineMemento> _redoStack;

	private pipelineMemento getState() {
		throw new UnsupportedOperationException();
	}

	public boolean redo() {
		throw new UnsupportedOperationException();
	}

	public void addFunction(IFunction aFunc) {
		throw new UnsupportedOperationException();
	}

	public void deleteFunction(IFunction aFunc) {
		throw new UnsupportedOperationException();
	}

	public IFunction[] getFunctions() {
		throw new UnsupportedOperationException();
	}

	public boolean undo() {
		throw new UnsupportedOperationException();
	}

	public boolean optimizeGraph() {
		throw new UnsupportedOperationException();
	}

	public boolean execute() {
		throw new UnsupportedOperationException();
	}

	public void save(String aFilename) {
		throw new UnsupportedOperationException();
	}

	public void load(String aFilename) {
		throw new UnsupportedOperationException();
	}

	public void import_325(String aFilename) {
		throw new UnsupportedOperationException();
	}

	public void export(String aFilename) {
		throw new UnsupportedOperationException();
	}

	public void truncate() {
		throw new UnsupportedOperationException();
	}

	public void generate(FILE_TYPE aFiletype) {
		throw new UnsupportedOperationException();
	}
}