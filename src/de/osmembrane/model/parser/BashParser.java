package de.osmembrane.model.parser;

import java.util.List;

import de.osmembrane.model.pipeline.AbstractFunction;

/**
 * Implementation of {@link IParser} for the bash (unix) command line.
 * 
 * @author jakob_jarosch
 */
public class BashParser extends CommandlineParser {

	protected static String BREAKLINE_SYMBOL = "\\\n";
	
	public BashParser() {
		super.setBreaklineSymbol(BREAKLINE_SYMBOL);
	}
	
	@Override
	public List<AbstractFunction> parseString(String input) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String parsePipeline(List<AbstractFunction> pipeline) {
		throw new UnsupportedOperationException();
	}
}