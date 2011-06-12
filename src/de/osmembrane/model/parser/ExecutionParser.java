/*
 * This file is part of the OSMembrane project.
 * More informations under www.osmembrane.de
 * 
 * The project is licensed under the GNU GENERAL PUBLIC LICENSE 3.0.
 * for more details about the license see http://www.osmembrane.de/license/
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

    /**
     * Creates a new {@link ExecutionParser}.
     */
    public ExecutionParser() {
        super.setBreaklineSymbol(BREAKLINE_SYMBOL);
        super.setBreaklineCommand(BREAKLINE_COMMAND);
        super.setQuotationSymbol(QUOTATION_SYMBOL);
        super.disableComments(true);
        super.addOsmosisPath(false);
    }
}
