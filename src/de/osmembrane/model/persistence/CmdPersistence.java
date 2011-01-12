package de.osmembrane.model.persistence;

import java.util.Observable;

import de.osmembrane.model.parser.CmdParser;

public class CmdPersistence extends AbstractPersistence {

	private static final Class<CmdParser> PARSER = CmdParser.class;
	
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