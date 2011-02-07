/*
 * This file is part of the OSMembrane project.
 * More informations under www.osmembrane.de
 * 
 * The project is licensed under the Creative Commons
 * Attribution-NonCommercial-ShareAlike 3.0 Unported License.
 * for more details about the license see
 * http://www.osmembrane.de/license/
 * 
 * Source: $HeadURL$ ($Revision$)
 * Last changed: $Date$
 */

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
