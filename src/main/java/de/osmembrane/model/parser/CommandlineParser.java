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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.osmembrane.model.ModelProxy;
import de.osmembrane.model.parser.ParseException.ErrorType;
import de.osmembrane.model.persistence.PipelinePersistenceObject;
import de.osmembrane.model.pipeline.AbstractConnector;
import de.osmembrane.model.pipeline.AbstractFunction;
import de.osmembrane.model.pipeline.AbstractParameter;
import de.osmembrane.model.pipeline.AbstractPipeline;
import de.osmembrane.model.pipeline.AbstractPipelineSettings;
import de.osmembrane.model.pipeline.ConnectorException;
import de.osmembrane.model.pipeline.ConnectorType;
import de.osmembrane.model.pipeline.Pipeline;
import de.osmembrane.model.pipeline.PipelineSettings;
import de.osmembrane.model.settings.SettingType;
import de.osmembrane.tools.I18N;

/**
 * Commandline-parser for osmosis command lines.
 * 
 * @author jakob_jarosch
 */
public class CommandlineParser implements IParser {

    /**
     * If it is not set, the osmosis path will not be added to the pipeline.
     */
    private boolean addOsmosisPath = true;

    protected String breaklineSymbol = "<linebreak>";
    protected String breaklineCommand = "\n";
    protected String quotationSymbol = "\"";
    protected String commentSymbol = "<COMMENT>: ";
    protected Pattern[] regexCommentPatterns = {};
    protected boolean disableComments = false;

    protected static final String DEFAULT_KEY = "DEFAULTKEY";

    protected static final Pattern PATTERN_TASK = Pattern.compile(
            "--([^ ]+)(.*?)((?=--)|$)", Pattern.CASE_INSENSITIVE
                    | Pattern.MULTILINE);

    protected static final Pattern PATTERN_PIPE = Pattern
            .compile("^(in|out)pipe\\.([0-9+])$");

    protected static final Pattern PATTERN_SPLIT_SPACES_PARAMETER = Pattern
            .compile("(inPipe|outPipe)", Pattern.CASE_INSENSITIVE
                    | Pattern.MULTILINE);

    /**
     * Returns a following group-matching:<br/>
     * <br/>
     * 0: whole parameter<br/>
     * 1: parameter(format: key='value') or NULL<br/>
     * 2: key or NULL<br/>
     * 3: value or NULL<br/>
     * 4: parameter(format: key="value") or NULL<br/>
     * 5: key or NULL<br/>
     * 6: value or NULL<br/>
     * 7: parameter(format: key=value) or NULL<br/>
     * 8: key or NULL<br/>
     * 9: value or NULL<br/>
     * 10: parameter(format: 'value') or NULL<br/>
     * 11: value or NULL<br/>
     * 12: parameter(format: "value") or NULL<br/>
     * 13: value or NULL<br/>
     * 14: parameter(format: value) or NULL<br/>
     * 15: value or NULL
     */
    protected static final Pattern PATTERN_PARAMETER = Pattern.compile(
    /* should match on key='value' */
    "(([^= ]+)='([^']+)')|"
    /* should match on key="value" */
    + "(([^= ]+)=\"([^\"]+)\")|"
    /* should match on key=value */
    + "(([^= ]+)=([^ ]+))|"
    /* should match on 'value' */
    + "('([^']+)')|"
    /* should match on "value" */
    + "(\"([^\"]+)\")|"
    /* should match on value */
    + "(([^ ]+))",
    /* case insensitive and multiline matching */
    Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

    @Override
    public PipelinePersistenceObject parseString(String input)
            throws ParseException {

        /** A temporary silent pipeline to check loop freeness */
        AbstractPipeline pipeline = new Pipeline(true, true);

        /** A map for listing all connections belongs to its function */
        Map<String, AbstractFunction> connectionMap = new HashMap<String, AbstractFunction>();

        /**
         * Map saves for each ConnectorType a function which has such an outPipe
         * but none explicit defined in the commandline.
         */
        Map<ConnectorType, Queue<AbstractFunction>> openOutConnectors = new HashMap<ConnectorType, Queue<AbstractFunction>>();
        for (ConnectorType type : ConnectorType.values()) {
            openOutConnectors.put(type, new LinkedList<AbstractFunction>());
        }

        /* replace all comments */
        for (Pattern replacePattern : regexCommentPatterns) {
            Matcher match = replacePattern.matcher(input);
            input = match.replaceAll("");
        }

        /* join the commandlines */
        input = input.replace(breaklineSymbol, "");
        input = input.replace(breaklineCommand, " ");

        Matcher taskMatcher = PATTERN_TASK.matcher(input);

        /* iterate over all found tasks */
        while (taskMatcher.find()) {
            String taskName = taskMatcher.group(1).toLowerCase();
            String taskParameters = taskMatcher.group(2).trim();

            Map<String, String> parameters = new HashMap<String, String>();
            Map<Integer, String> inPipes = new HashMap<Integer, String>();
            Map<Integer, String> outPipes = new HashMap<Integer, String>();

            /* parse all parameters */
            Matcher paramMatcher = PATTERN_PARAMETER.matcher(taskParameters);

            /* iterate over all parameters */
            while (paramMatcher.find()) {
                String[] keyValuePair = getParameter(paramMatcher);

                /*
                 * change the key to an empty String, if it NULL. this is a
                 * default parameter.
                 */
                if (keyValuePair[0] == null) {
                    keyValuePair[0] = DEFAULT_KEY;
                }

                /* try to identify the parameter as an pipe */
                Matcher pipeMatcher = PATTERN_PIPE.matcher(keyValuePair[0]
                        .toLowerCase());
                if (pipeMatcher.find()) {
                    /* found a pipe */
                    String inOutPipe = pipeMatcher.group(1);
                    int pipeIndex = Integer.parseInt(pipeMatcher.group(2));

                    if (inOutPipe.equals("in")) {
                        inPipes.put(pipeIndex, keyValuePair[1]);
                    } else {
                        outPipes.put(pipeIndex, keyValuePair[1]);
                    }
                } else {
                    /* found a normal parameter */
                    parameters.put(keyValuePair[0], keyValuePair[1]);
                }
            }

            /*
             * find the corresponding function to the task but first of all take
             * tee and tee-change, 'cause they have no corresponding functions.
             */
            if (taskName.equals("tee") || taskName.equals("tee-change")) {
                AbstractFunction function = null;

                /* get the count of outPipes defined in the tee-task */
                int countOutPipes = 2;
                if (parameters.get(DEFAULT_KEY) != null) {
                    countOutPipes = Integer.parseInt(parameters
                            .get(DEFAULT_KEY));
                } else if (parameters.get("outputCount") != null) {
                    countOutPipes = Integer.parseInt(parameters
                            .get("outputCount"));
                }

                /* try to get the function at the inPipe */
                for (Integer pipeId : inPipes.keySet()) {
                    function = connectionMap.get(inPipes.get(pipeId));
                }

                /* check if that could be also an implicit function */
                if (function == null) {
                    Queue<AbstractFunction> functions = openOutConnectors
                            .get((taskName.equals("tee") ? ConnectorType.ENTITY
                                    : ConnectorType.CHANGE));
                    function = functions.poll();
                }

                /*
                 * found whether a explicit function nor an implicit one at the
                 * inConnector
                 */
                if (function == null) {
                    throw new ParseException(ErrorType.UNKNOWN_PIPE_STREAM,
                            taskName);
                }

                /* add all specified outPipes to the map */
                int countedOutPipes = 0;
                for (Integer pipeId : outPipes.keySet()) {
                    countedOutPipes++;
                    String pipeName = outPipes.get(pipeId);
                    if (function != null) {
                        connectionMap.put(pipeName, function);
                    }
                }

                /* now add all unspecified outPipes */
                while (countedOutPipes < countOutPipes) {
                    countedOutPipes++;
                    Queue<AbstractFunction> functions = openOutConnectors
                            .get((taskName.equals("tee") ? ConnectorType.ENTITY
                                    : ConnectorType.CHANGE));
                    functions.add(function);
                }

                /*
                 * no tee, try to get a real function
                 */
            } else {
                AbstractFunction function = ModelProxy.getInstance()
                        .getFunctions()
                        .getMatchingFunctionForTaskName(taskName);

                if (function == null) {
                    throw new ParseException(ErrorType.UNKNOWN_TASK, taskName);
                } else {
                    pipeline.addFunction(function);
                }

                /*
                 * We have to check if the function does contain a parameter
                 * with "hasSpaces" set, if that is so, we have to parse the
                 * parameters again.
                 */
                AbstractParameter spacesParam = null;
                for (AbstractParameter param : function.getActiveTask()
                        .getParameters()) {
                    if (param.hasSpaces()) {
                        spacesParam = param;
                    }
                }

                if (spacesParam != null) {
                    String[] results = PATTERN_SPLIT_SPACES_PARAMETER
                            .split(taskParameters);

                    spacesParam.setValue(results[0].trim());
                    if (!spacesParam.isValid()) {
                        throw new ParseException(
                                ErrorType.INVALID_PARAMETER_VALUE, taskName,
                                spacesParam.getName(), results[0].trim());
                    }

                    /*
                     * Okay it seems not to be so, that there is a spaces param,
                     * do it the normal way.
                     */
                } else {
                    /* copy parameters to the function */
                    for (String key : parameters.keySet()) {
                        boolean foundKey = false;
                        for (AbstractParameter parameter : function
                                .getActiveTask().getParameters()) {
                            if (parameter.getName().toLowerCase()
                                    .equals(key.toLowerCase())
                                    || (key.equals(DEFAULT_KEY) && parameter
                                            .isDefaultParameter())) {
                                parameter.setValue(parameters.get(key));
                                if (!parameter.isValid()) {
                                    throw new ParseException(
                                            ErrorType.INVALID_PARAMETER_VALUE,
                                            taskName, parameter.getName(),
                                            parameters.get(key));
                                }

                                foundKey = true;
                            }
                        }
                        if (!foundKey) {
                            if (key.equals(DEFAULT_KEY)) {
                                throw new ParseException(
                                        ErrorType.NO_DEFAULT_PARAMETER_FOUND,
                                        taskName, parameters.get(key));
                            } else {
                                throw new ParseException(
                                        ErrorType.UNKNOWN_TASK_FORMAT,
                                        taskName, key);
                            }
                        }
                    }
                }

                /* add the connections */
                for (Integer pipeId : inPipes.keySet()) {
                    String pipeName = inPipes.get(pipeId);
                    AbstractFunction outFunction = connectionMap.get(pipeName);

                    /* check if the inPipe has no counterpart as a outPipe */
                    if (outFunction == null) {
                        throw new ParseException(
                                ErrorType.COUNTERPART_PIPE_MISSING, taskName,
                                pipeId);
                    }

                    try {
                        outFunction.addConnectionTo(function);
                    } catch (ConnectorException e) {
                        String connectionExceptionMessage = I18N.getInstance()
                                .getString(
                                        "Model.Pipeline.AddConnection."
                                                + e.getType());

                        throw new ParseException(
                                ErrorType.CONNECTION_NOT_PERMITTED, outFunction
                                        .getActiveTask().getName(), function
                                        .getActiveTask().getName(),
                                connectionExceptionMessage);
                    }
                }
                /* find connectors without a explicit definition. */
                for (AbstractConnector connector : function.getInConnectors()) {
                    /*
                     * check if the connector is one without a explicit defined
                     * pipe.
                     */
                    if (inPipes.get(connector.getConnectorIndex()) == null) {
                        Queue<AbstractFunction> functions = openOutConnectors
                                .get(connector.getType());
                        AbstractFunction outFunction = functions.poll();

                        if (outFunction != null) {
                            try {
                                outFunction.addConnectionTo(function);
                            } catch (ConnectorException e) {
                                String connectionExceptionMessage = I18N
                                        .getInstance().getString(
                                                "Model.Pipeline.AddConnection."
                                                        + e.getType());

                                throw new ParseException(
                                        ErrorType.CONNECTION_NOT_PERMITTED,
                                        outFunction.getActiveTask().getName(),
                                        function.getActiveTask().getName(),
                                        connectionExceptionMessage);
                            }
                            /* remove the openConnector from the map */
                            functions.remove(function);
                        }
                    }
                }

                /* register all open outPipes */
                for (Integer pipeId : outPipes.keySet()) {
                    String pipeName = outPipes.get(pipeId);
                    connectionMap.put(pipeName, function);
                }
                /* find connectors without a explicit definition. */
                for (AbstractConnector connector : function.getOutConnectors()) {
                    /*
                     * check if the connector is one without a explicit defined
                     * pipe.
                     */
                    if (outPipes.get(connector.getConnectorIndex()) == null) {
                        Queue<AbstractFunction> functions = openOutConnectors
                                .get(connector.getType());

                        functions.add(function);
                    }
                }
            }
        }

        /* use the pipeline algorithm to arrange the functions */
        pipeline.arrangePipeline();

        /* create the output List */
        List<AbstractFunction> returnList = new ArrayList<AbstractFunction>();
        for (AbstractFunction function : pipeline.getFunctions()) {
            /* remove the observer of this pipeline (no longer required) */
            function.deleteObserver(pipeline);

            returnList.add(function);
        }
        return new PipelinePersistenceObject(returnList, new PipelineSettings());
    }

    /* ************************* */
    /* String-Pipeline Generator */
    /* ************************* */
    @Override
    public String parsePipeline(PipelinePersistenceObject pipelineObject) {
        List<AbstractFunction> pipeline = pipelineObject.getFunctions();
        AbstractPipelineSettings settings = pipelineObject.getSettings();

        /* Queue where functions are stored, that haven't been parsed yet. */
        Queue<AbstractFunction> functionQueue = new LinkedList<AbstractFunction>();

        /* List with all used functions */
        List<AbstractFunction> usedFunctions = new ArrayList<AbstractFunction>();

        /* connectorMap which maps to each used out-connector a uniqueId */
        Map<AbstractConnector, Integer> connectorMap = new HashMap<AbstractConnector, Integer>();

        /* StringBuilder for the String-output. */
        StringBuilder builder = new StringBuilder();

        /* pipeIndex is the uniqueId for out-connectors */
        int pipeIndex = 0;

        /* add all functions to the queue */
        for (AbstractFunction function : pipeline) {
            functionQueue.add(function);
        }

        /* add the comment header block to the pipeline. */
        if (!disableComments) {
            builder.append(getCommentSymbol()
                    + "OSMembrane auto-generated pipeline for osmosis");
            builder.append(getBreaklineCommand());

            builder.append(getCommentSymbol() + "Name: " + settings.getName());
            builder.append(getBreaklineCommand());

            builder.append(getCommentSymbol() + "Date: "
                    + new Date().toString());
            builder.append(getBreaklineCommand());

            builder.append(getCommentSymbol() + "Comment:");
            builder.append(getBreaklineCommand());

            String[] commentLines = settings.getComment().split("(\\r\\n|\\n)");
            for (String line : commentLines) {
                builder.append(getCommentSymbol() + line);
                builder.append(getBreaklineCommand());
            }
            builder.append(getBreaklineCommand());
        }

        /* add the path to the osmosis binary */
        if (addOsmosisPath) {
            builder.append(quotate((String) ModelProxy.getInstance()
                    .getSettings().getValue(SettingType.DEFAULT_OSMOSIS_PATH)));

            if (pipelineObject.getSettings().getVerbose() > 0) {
                builder.append(" -v" + settings.getVerbose());
            }

            if (pipelineObject.getSettings().getDebug() > 0) {
                builder.append(" -d" + settings.getDebug());
            }
        }

        /* do the parsing while a function is in the queue */
        while (!functionQueue.isEmpty()) {
            AbstractFunction function = functionQueue.poll();

            /*
             * Check if the function does not have any dependencies on a
             * function which has not yet been parsed.
             */
            boolean addable = true;
            for (AbstractConnector inConnector : function.getInConnectors()) {
                for (AbstractConnector sourceConnector : inConnector
                        .getConnections()) {
                    AbstractFunction sourceFunction = sourceConnector
                            .getParent();
                    if (!usedFunctions.contains(sourceFunction)) {
                        addable = false;
                        break;
                    }
                }
                if (!addable) {
                    break;
                }
            }

            /*
             * Only do the next steps if the function does _not_ have any
             * dependencies.
             */
            if (addable) {
                usedFunctions.add(function);

                appendLineBreak(builder);

                /*
                 * get the shortName and the name from the activeTask in the
                 * function
                 */
                String stn = function.getActiveTask().getShortName();
                String tn = function.getActiveTask().getName();

                /* write the task(-short)-name */
                if ((Boolean) ModelProxy
                        .getInstance()
                        .getSettings()
                        .getValue(SettingType.USE_SHORT_TASK_NAMES_IF_AVAILABLE)
                        && stn != null) {
                    builder.append("--" + stn);
                } else {
                    builder.append("--" + tn);
                }

                /* write all parameters of the task */
                for (AbstractParameter parameter : function.getActiveTask()
                        .getParameters()) {

                    /*
                     * Only add a parameter when there is not a default value
                     * assigned, or settings say that they are needed.
                     */
                    String value = null;
                    if (parameter.getValue() != null) {
                        value = parameter.getValue();
                    } else if (parameter.isDefaultValue()
                            && (Boolean) ModelProxy
                                    .getInstance()
                                    .getSettings()
                                    .getValue(
                                            SettingType.EXPORT_PARAMETERS_WITH_DEFAULT_VALUES)) {
                        value = parameter.getDefaultValue();
                    }
                    if (value != null) {
                        /* look up if it is a parameter with set "hasSpaces" */
                        if (parameter.hasSpaces()
                                && parameter.isDefaultParameter()) {
                            builder.append(" " + parameter.getValue());
                        } else {
                            builder.append(" " + parameter.getName() + "="
                                    + quotate(parameter.getValue()));
                        }
                    }
                }

                /* write all inConnectors */
                for (AbstractConnector connector : function.getInConnectors()) {
                    for (AbstractConnector otherConnector : connector
                            .getConnections()) {
                        /*
                         * Use the offset to get the right connector of the
                         * attached --tee to otherConnector.
                         */
                        int offset = getConnectorOffset(connector,
                                otherConnector);

                        builder.append(" inPipe."
                                + connector.getConnectorIndex() + "="
                                + (connectorMap.get(otherConnector) + offset));
                    }
                }

                /* Create the out-Connectors and add a tee if needed. */
                StringBuilder teeBuilder = new StringBuilder();
                for (AbstractConnector connector : function.getOutConnectors()) {
                    pipeIndex++;

                    builder.append(" outPipe." + connector.getConnectorIndex()
                            + "=" + pipeIndex);

                    /* Add a tee, 'cause more than one connection is attached. */
                    if (connector.getConnections().length > 1) {
                        /*
                         * add to the index + 1, 'cause the first
                         * tee-out-connector has function.connector + 1 as pipe
                         * key.
                         */
                        connectorMap.put(connector, (pipeIndex + 1));

                        appendLineBreak(teeBuilder);

                        /* add the correct --tee */
                        teeBuilder
                                .append("--"
                                        + (connector.getType() == ConnectorType.ENTITY ? "tee"
                                                : "change-tee") + " ");

                        teeBuilder.append(connector.getConnections().length
                                + " inPipe.0=" + pipeIndex);

                        /* add all outPipes to the --tee */
                        for (int i = 0; i < connector.getConnections().length; i++) {
                            pipeIndex++;
                            /*
                             * append a out-pipe for the tee
                             * (outPipe.Index=pipeIndex
                             */
                            teeBuilder
                                    .append(" outPipe." + i + "=" + pipeIndex);
                        }
                    } else {
                        connectorMap.put(connector, pipeIndex);
                    }
                }

                builder.append(teeBuilder);
            } else {
                /* function does not full-fill all dependencies, enqueue */
                functionQueue.add(function);
            }
        }

        return builder.toString();
    }

    /**
     * Sets if the omsosis path should be added or not
     * 
     * @param addOsmosisPath
     *            boolean
     */
    protected void addOsmosisPath(boolean addOsmosisPath) {
        this.addOsmosisPath = addOsmosisPath;
    }

    /**
     * Returns the offset between the first outPipe of otherConnecor to the
     * connector. Useful for connection to the correct --tee outPipe.
     * 
     * @param connector
     *            the connector to be connected to the otherConnectors --tee
     * @param otherConnector
     *            the connector where a -tee has to be added
     * 
     * @return the offset of the outPipe of otherConnector for connector
     */
    private int getConnectorOffset(AbstractConnector connector,
            AbstractConnector otherConnector) {
        AbstractConnector[] connections = otherConnector.getConnections();
        for (int i = 0; i < connections.length; i++) {
            if (connections[i] == connector) {
                return i;
            }
        }

        /* should never! happen! */
        throw new RuntimeException(
                "Sorry, but can't parse that, found a connection with only a connection in one direction.");
    }

    /**
     * Returns a key-value-pair.
     * 
     * @param paramMatcher
     *            matched parameter which should be parsed.
     * @return String-Array with first entry as key and second one as value
     */
    private String[] getParameter(Matcher paramMatcher) {
        int[] keyEntries = { 1, 4, 7, 10, 12, 14 };
        for (int i : keyEntries) {
            if (paramMatcher.group(i) != null) {
                /* found the right entry */
                String key = null;
                String value = null;

                if (i < 10) {
                    key = paramMatcher.group(i + 1).trim();
                    value = paramMatcher.group(i + 2).trim();
                } else {
                    value = paramMatcher.group(i + 1).trim();
                }

                return new String[] { key, value };
            }
        }

        return null;
    }

    /**
     * Adds a linebreak to a given {@link StringBuilder}.
     */
    private void appendLineBreak(StringBuilder builder) {
        builder.append(" ");
        builder.append(breaklineSymbol);
        builder.append(breaklineCommand);
    }

    /**
     * Adds a quotation to a {@link String} if it is needed.
     * 
     * @return quotated {@link String}
     */
    private String quotate(String string) {
        if (string.contains(" ")) {
            return quotationSymbol + string + quotationSymbol;
        } else {
            return string;
        }
    }

    /* **************************** */
    /* Some getters and setters.... */
    /* **************************** */

    protected void setBreaklineSymbol(String symbol) {
        this.breaklineSymbol = symbol;
    }

    @Override
    public String getBreaklineSymbol() {
        return breaklineSymbol;
    }

    protected void setCommentSymbol(String commentSymbol) {
        this.commentSymbol = commentSymbol;
    }

    @Override
    public String getCommentSymbol() {
        return commentSymbol;
    }

    protected void setBreaklineCommand(String breaklineCommand) {
        this.breaklineCommand = breaklineCommand;
    }

    @Override
    public String getBreaklineCommand() {
        return breaklineCommand;
    }

    protected void setQuotationSymbol(String symbol) {
        this.quotationSymbol = symbol;
    }

    @Override
    public String getQuotationSymbol() {
        return quotationSymbol;
    }

    protected void setRegexCommentPatterns(Pattern[] regexCommentPatterns) {
        this.regexCommentPatterns = regexCommentPatterns;
    }

    @Override
    public Pattern[] getRegexCommentPatterns() {
        return regexCommentPatterns;
    }

    protected void disableComments(boolean disableComments) {
        this.disableComments = disableComments;
    }
}
