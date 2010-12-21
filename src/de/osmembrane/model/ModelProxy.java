package de.osmembrane.model;

public class ModelProxy extends de.osmembrane.model.java.util.Observable implements de.osmembrane.model.java.util.Observer {
	public IFunctionFactory _unnamed_IFunctionFactory_;
	public IPipeline _unnamed_IPipeline_;
	public ISettings _unnamed_ISettings_;

	public void accessPipeline() {
		throw new UnsupportedOperationException();
	}

	public void accessSettings() {
		throw new UnsupportedOperationException();
	}

	public void accessFunctions() {
		throw new UnsupportedOperationException();
	}
}