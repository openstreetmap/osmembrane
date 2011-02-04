package de.osmembrane.model.parser;


/**
 * Implementation of {@link IParser} for the bash (unix) command line.
 * 
 * @author jakob_jarosch
 */
public class ExecutionParser extends CommandlineParser {

	protected String BREAKLINE_SYMBOL = "<NEWPARAMETER>";
	protected String BREAKLINE_COMMAND = "";
	
	public ExecutionParser() {
		super.setBreaklineSymbol(BREAKLINE_SYMBOL);
		super.setBreaklineCommand(BREAKLINE_COMMAND);
		super.addOsmosisPath(false);
	}
}