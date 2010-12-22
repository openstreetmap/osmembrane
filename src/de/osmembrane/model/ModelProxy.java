package de.osmembrane.model;

import java.util.Observable;
import java.util.Observer;

public class ModelProxy extends Observable implements Observer {
	public IFunctionFactory unnamed_IFunctionFactory_;
	public IPipeline unnamed_IPipeline_;
	public ISettings unnamed_ISettings_;

	public void accessPipeline() {
		throw new UnsupportedOperationException();
	}

	public void accessSettings() {
		throw new UnsupportedOperationException();
	}

	public void accessFunctions() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void update(Observable o, Object arg) {
		throw new UnsupportedOperationException();
	}
}