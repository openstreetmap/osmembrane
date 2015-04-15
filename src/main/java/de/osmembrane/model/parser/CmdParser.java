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

import java.util.regex.Pattern;

/**
 * Implementation of {@link IParser} for the cmd (windows) command line.
 * 
 * @author jakob_jarosch
 */
public class CmdParser extends CommandlineParser {

    protected String BREAKLINE_SYMBOL = "^";
    protected String BREAKLINE_COMMAND = "\r\n";
    protected String COMMENT_SYMBOL = ":: ";
    protected Pattern[] COMMENT_PATTERNS = {
            Pattern.compile("::.*$", Pattern.MULTILINE),
            Pattern.compile("^\\p{Space}*REM .*$", Pattern.CASE_INSENSITIVE
                    | Pattern.MULTILINE) };

    /**
     * Creates a new {@link CmdParser}.
     */
    public CmdParser() {
        super.setBreaklineSymbol(BREAKLINE_SYMBOL);
        super.setBreaklineCommand(BREAKLINE_COMMAND);
        super.setCommentSymbol(COMMENT_SYMBOL);
        super.setRegexCommentPatterns(COMMENT_PATTERNS);
    }
}
