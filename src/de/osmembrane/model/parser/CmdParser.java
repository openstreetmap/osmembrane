package de.osmembrane.model.parser;

import java.util.List;

import de.osmembrane.model.pipeline.AbstractFunction;

/**
 * Implementation of {@link IParser} for the cmd (windows) command line.
 * 
 * @author jakob_jarosch
 */
public class CmdParser implements IParser {

	@Override
	public List<AbstractFunction> parseString(String input) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String parsePipeline(List<AbstractFunction> pipeline) {
		throw new UnsupportedOperationException();
	}
}