package de.osmembrane.model;

import java.util.Observable;

public class Pipeline extends Observable implements IPipeline {
	private Stack<pipelineMemento> undoStack;
	private List<Function> functions;
	private Stack<pipelineMemento> redoStack;

	private pipelineMemento getState() {
		throw new UnsupportedOperationException();
	}

	public boolean redo() {
		throw new UnsupportedOperationException();
	}

	public void addFunction(IFunction func) {
		throw new UnsupportedOperationException();
	}

	public void deleteFunction(IFunction func) {
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

	public void save(String filename) {
		throw new UnsupportedOperationException();
	}

	public void load(String filename) {
		throw new UnsupportedOperationException();
	}

	public void import_326(String filename) {
		throw new UnsupportedOperationException();
	}

	public void export(String filename) {
		throw new UnsupportedOperationException();
	}

	public void truncate() {
		throw new UnsupportedOperationException();
	}

	public void generate(FILE_TYPE filetype) {
		throw new UnsupportedOperationException();
	}
}