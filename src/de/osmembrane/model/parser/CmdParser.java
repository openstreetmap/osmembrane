package de.osmembrane.model.parser;

/**
 * Implementation of {@link IParser} for the cmd (windows) command line.
 * 
 * @author jakob_jarosch
 */
public class CmdParser extends CommandlineParser {

	protected String BREAKLINE_SYMBOL = "^";
	protected String BREAKLINE_COMMAND = "\r\n";

	public CmdParser() {
		super.setBreaklineSymbol(BREAKLINE_SYMBOL);
		super.setBreaklineCommand(BREAKLINE_COMMAND);
	}
}