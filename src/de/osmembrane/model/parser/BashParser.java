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

import de.osmembrane.model.persistence.PipelinePersistenceObject;

/**
 * Implementation of {@link IParser} for the bash (unix) command line.
 * 
 * @author jakob_jarosch
 */
public class BashParser extends CommandlineParser {

    protected String BREAKLINE_SYMBOL = "\\";
    protected String BREAKLINE_COMMAND = "\n";
    protected String COMMENT_SYMBOL = "# ";
    protected Pattern[] COMMENT_PATTERNS = { Pattern.compile("#.*$",
            Pattern.MULTILINE) };

    /**
     * Creates a new {@link BashParser}.
     */
    public BashParser() {
        super.setBreaklineSymbol(BREAKLINE_SYMBOL);
        super.setBreaklineCommand(BREAKLINE_COMMAND);
        super.setCommentSymbol(COMMENT_SYMBOL);
        super.setRegexCommentPatterns(COMMENT_PATTERNS);
    }

    @Override
    public String parsePipeline(PipelinePersistenceObject pipeline) {
        String result = super.parsePipeline(pipeline);
        return "#!/bin/bash\n\n" + result;
    }
}
