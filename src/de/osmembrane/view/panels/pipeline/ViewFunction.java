package de.osmembrane.view.panels.pipeline;

import de.osmembrane.model.Function;
import de.osmembrane.model.AbstractFunction;

import java.util.Observable;
import java.util.Observer;

public class ViewFunction implements Observer {
	
	private Function function;

	public ViewFunction(AbstractFunction function) {
		throw new UnsupportedOperationException();
	}

	public AbstractFunction getFunction() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void update(Observable o, Object arg) {
		throw new UnsupportedOperationException();
		
	}
}