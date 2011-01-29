package de.osmembrane.model.parser;


/**
 * Implementation of {@link IParser} for the bash (unix) command line.
 * 
 * @author jakob_jarosch
 */
public class BashParser extends CommandlineParser {

	protected String BREAKLINE_SYMBOL = "\\";
	protected String BREAKLINE_COMMAND = "\n";
	
	public BashParser() {
		super.setBreaklineSymbol(BREAKLINE_SYMBOL);
		super.setBreaklineCommand(BREAKLINE_COMMAND);
	}
}