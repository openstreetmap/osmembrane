package de.osmembrane.view.panels;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import de.osmembrane.model.AbstractFunction;

public class PipelinePanel extends JPanel implements Observer {
	
	private AbstractFunction selectedFunction;
	
	@Override
	public void update(Observable o, Object arg) {
		throw new UnsupportedOperationException();
		
	}

	/**
	 * @return the selectedFunction
	 */
	public AbstractFunction getSelectedFunction() {
		return selectedFunction;
	}
	
	public PipelinePanel() {
		add(new ViewFunction(null));
	}
	
}