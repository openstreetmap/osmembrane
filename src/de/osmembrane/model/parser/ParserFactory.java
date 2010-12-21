package de.osmembrane.model.parser;

import java.util.ArrayList;
import de.osmembrane.model.parser.IParser;

public class ParserFactory {
	public ArrayList<IParser> unnamed_IParser_ = new ArrayList<IParser>();

	public ParserFactory getInstance() {
		throw new UnsupportedOperationException();
	}

	public void getParser(Class<IParser> clazz) {
		throw new UnsupportedOperationException();
	}
}