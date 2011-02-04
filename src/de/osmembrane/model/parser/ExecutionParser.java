package de.osmembrane.model.parser;


/**
 * Implementation of {@link IParser} for the bash (unix) command line.
 * 
 * @author jakob_jarosch
 */
public class ExecutionParser extends CommandlineParser {

	protected String BREAKLINE_SYMBOL = " ";
	protected String BREAKLINE_COMMAND = "";
	protected String QUOTATION_SYMBOL = "'";
	
	public ExecutionParser() {
		super.setBreaklineSymbol(BREAKLINE_SYMBOL);
		super.setBreaklineCommand(BREAKLINE_COMMAND);
		super.setQuotationSymbol(QUOTATION_SYMBOL);
		super.addOsmosisPath(false);
	}
}