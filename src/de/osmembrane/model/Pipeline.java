package de.osmembrane.model;

import java.util.List;
import java.util.Observable;
import java.util.Stack;

public class Pipeline extends AbstractPipeline {
	private Stack<Pipeline> undoStack;
	private List<Function> functions;
	private Stack<Pipeline> redoStack;

	private Pipeline getState() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean redo() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addFunction(AbstractFunction func) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void deleteFunction(AbstractFunction func) {
		throw new UnsupportedOperationException();
	}

	@Override
	public AbstractFunction[] getFunctions() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean undo() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean optimizeGraph() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean execute() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void savePipeline(String filename) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void loadPipeline(String filename) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void importPipeline(String filename) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void exportPipeline(String filename) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void truncate() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void generate(String filetype) {
		throw new UnsupportedOperationException();
	}
}