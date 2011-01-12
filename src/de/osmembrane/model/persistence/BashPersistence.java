package de.osmembrane.model.persistence;

import java.util.Observable;

import de.osmembrane.model.parser.BashParser;

public class BashPersistence extends AbstractPersistence {

	private static final Class<BashParser> PARSER = BashParser.class;
	
	public void save(String file, Object data) {
		throw new UnsupportedOperationException();
	}

	public Object load(String file) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}
}