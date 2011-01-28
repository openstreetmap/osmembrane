package de.osmembrane.model.parser;

import java.util.List;

import de.osmembrane.model.pipeline.AbstractFunction;

/**
 * Implementation of {@link IParser} for the cmd (windows) command line.
 * 
 * @author jakob_jarosch
 */
public class CmdParser extends CommandlineParser {

	protected String BREAKLINE_SYMBOL = "^\r\n";
	
	public CmdParser() {
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