package de.osmembrane.model.parser;


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
}