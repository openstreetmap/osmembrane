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

import java.util.List;
import java.util.regex.Pattern;

import de.osmembrane.model.pipeline.AbstractFunction;
import de.osmembrane.model.pipeline.Pipeline;

/**
 * Interface for a parser.
 * 
 * @author jakob_jarosch
 */
public interface IParser {

	/**
	 * Creates a {@link Pipeline} from a given string.
	 * 
	 * @param input
	 *            string which should be transformed
	 * @return List of functions, which are used in the {@link Pipeline}
	 */
	public List<AbstractFunction> parseString(String input)
			throws ParseException;

	/**
	 * Creates a string from a given {@link Pipeline}.
	 * 
	 * @param pipeline
	 *            which should be transformed
	 * @return String representation a given format
	 */
	public String parsePipeline(List<AbstractFunction> pipeline);
	
	/**
	 * Returns the quotation symbol of the parser.
	 * 
	 * @return quotation symbol
	 */
	public String getQuotationSymbol();

	/**
	 * Returns the breakline command.
	 * 
	 * @return breakline command
	 */
	public String getBreaklineCommand();

	/**
	 * Returns the breakline symbol.
	 * 
	 * @return breakline symbol
	 */
	public String getBreaklineSymbol();

	/**
	 * Returns the regex patterns for comments.
	 * 
	 * @return regex comment patterns
	 */
	public Pattern[] getRegexCommentPatterns();
}
