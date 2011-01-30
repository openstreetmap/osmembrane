package de.osmembrane.model.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.osmembrane.model.ModelProxy;
import de.osmembrane.model.parser.ParseException.ErrorType;
import de.osmembrane.model.pipeline.AbstractConnector;
import de.osmembrane.model.pipeline.AbstractFunction;
import de.osmembrane.model.pipeline.AbstractParameter;
import de.osmembrane.model.pipeline.AbstractPipeline;
import de.osmembrane.model.pipeline.ConnectorException;
import de.osmembrane.model.pipeline.ConnectorType;
import de.osmembrane.model.pipeline.Pipeline;
import de.osmembrane.tools.I18N;

public class CommandlineParser implements IParser {

	protected String breaklineSymbol = "<linebreak>";
	protected String breaklineCommand = "\n";

	protected String DEFAULT_KEY = "DEFAULTKEY";

	protected static final Pattern PATTERN_TASK = Pattern
			.compile("--([a-zA-Z0-9\\-]+)(.+?)((?=--)|$)");

	protected static final Pattern PATTERN_PIPE = Pattern
			.compile("^(in|out)pipe\\.([0-9+])$");

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
	+ "(([^ ]+))");

	@Override
	public List<AbstractFunction> parseString(String input)
			throws ParseException {

		/** A temporary silent pipeline to check loop freeness */
		AbstractPipeline pipeline = new Pipeline(true);

		/** A map for listing all connections belongs to its function */
		Map<String, AbstractFunction> connectionMap = new HashMap<String, AbstractFunction>();

		/**
		 * Map saves for each ConnectorType a function which has such an outPipe
		 * but none explicit defined in the commandline.
		 */
		Map<ConnectorType, AbstractFunction> openOutConnectors = new HashMap<ConnectorType, AbstractFunction>();

		input = input.replace(breaklineSymbol, " ");

		Matcher taskMatcher = PATTERN_TASK.matcher(input);
		while (taskMatcher.find()) {
			String taskName = taskMatcher.group(1).toLowerCase();
			String taskParameters = taskMatcher.group(2).trim();

			Map<String, String> parameters = new HashMap<String, String>();
			Map<Integer, String> inPipes = new HashMap<Integer, String>();
			Map<Integer, String> outPipes = new HashMap<Integer, String>();

			// System.out.println(taskName);

			Matcher paramMatcher = PATTERN_PARAMETER.matcher(taskParameters);
			while (paramMatcher.find()) {
				String[] keyValuePair = getParameter(paramMatcher);

				/*
				 * change the key to an empty String, if it NULL. this is a
				 * default parameter.
				 */
				if (keyValuePair[0] == null) {
					keyValuePair[0] = DEFAULT_KEY;
				}

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

					// System.out.println("     Pipe: " + inOutPipe + " "
					// + pipeIndex + " = " + keyValuePair[1]);
				} else {
					/* found a normal parameter */
					parameters.put(keyValuePair[0], keyValuePair[1]);

					// System.out.println("     Param: "
					// + (keyValuePair[0].equals(DEFAULT_KEY) ? DEFAULT_KEY
					// : keyValuePair[0]) + " = "
					// + keyValuePair[1]);
				}
			}

			/*
			 * find the corresponding function to the task but first of all take
			 * tee and tee-change, 'cause they have no corresponding functions.
			 */
			if (taskName.equals("tee") || taskName.equals("tee-change")) {
				AbstractFunction function = null;
				for (Integer pipeId : inPipes.keySet()) {
					function = connectionMap.get(inPipes.get(pipeId));
				}
				/* check if that could be also an implicit function */
				if (function == null) {
					function = openOutConnectors
							.get((taskName.equals("tee") ? ConnectorType.ENTITY
									: ConnectorType.CHANGE));
				}

				if (function == null) {
					throw new ParseException(ErrorType.UNKNOWN_PIPE_STREAM,
							I18N.getInstance().getString(
									"Model.Parser.UnknownPipeStream", taskName));
				}

				for (Integer pipeId : outPipes.keySet()) {
					String pipeName = outPipes.get(pipeId);
					if (function != null) {
						connectionMap.put(pipeName, function);
					}
				}
			} else {
				AbstractFunction function = ModelProxy.getInstance()
						.accessFunctions()
						.getMatchingFunctionForTaskName(taskName);

				if (function == null) {
					throw new ParseException(ErrorType.UNKNOWN_TASK);
				} else {
					pipeline.addFunction(function);
				}

				/* copy parameters to the function */
				for (String key : parameters.keySet()) {
					boolean foundKey = false;
					for (AbstractParameter parameter : function.getActiveTask()
							.getParameters()) {
						if (parameter.getName().toLowerCase()
								.equals(key.toLowerCase())
								|| (key.equals(DEFAULT_KEY) && parameter
										.isDefaultParameter())) {
							parameter.setValue(parameters.get(key));
							foundKey = true;
						}
					}
					if (!foundKey) {
						throw new ParseException(ErrorType.UNKNOWN_TASK_FORMAT,
								I18N.getInstance().getString(
										"Model.Parser.ParameterNotFound",
										taskName, key));
					}
				}

				/* add the connections */
				for (Integer pipeId : inPipes.keySet()) {
					String pipeName = inPipes.get(pipeId);
					AbstractFunction outFunction = connectionMap.get(pipeName);
					try {
						outFunction.addConnectionTo(function);
					} catch (ConnectorException e) {
						String connectionExceptionMessage = I18N.getInstance()
								.getString(
										"Model.Pipeline.AddConnection."
												+ e.getType());

						String message = I18N.getInstance().getString(
								"Model.Parser.ConnectionNotPermitted",
								outFunction.getActiveTask().getName(),
								function.getActiveTask().getName(),
								connectionExceptionMessage);

						throw new ParseException(
								ErrorType.CONNECTION_NOT_PERMITTED, message);
					}
				}
				/* find connectors without a explicit definition. */
				for (AbstractConnector connector : function.getInConnectors()) {
					/*
					 * check if the connector is one without a explicit defined
					 * pipe.
					 */
					if (inPipes.get(connector.getConnectorIndex()) == null) {
						AbstractFunction outFunction = openOutConnectors
								.get(connector.getType());
						if (outFunction != null) {
							try {
								outFunction.addConnectionTo(function);
							} catch (ConnectorException e) {
								String connectionExceptionMessage = I18N
										.getInstance().getString(
												"Model.Pipeline.AddConnection."
														+ e.getType());

								String message = I18N.getInstance().getString(
										"Model.Parser.ConnectonNotPermitted",
										outFunction.getActiveTask().getName(),
										function.getActiveTask().getName(),
										connectionExceptionMessage);

								throw new ParseException(
										ErrorType.CONNECTION_NOT_PERMITTED,
										message);
							}
							/* remove the openConnector from the map */
							openOutConnectors.remove(connector.getType());
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
						openOutConnectors.put(connector.getType(), function);
					}
				}
			}
		}

		/* create the output List */
		List<AbstractFunction> returnList = new ArrayList<AbstractFunction>();
		for (AbstractFunction function : pipeline.getFunctions()) {
			/* remove the observer of this pipeline (no longer required) */
			function.deleteObserver(pipeline);

			returnList.add(function);
		}
		return returnList;
	}

	@Override
	public String parsePipeline(List<AbstractFunction> pipeline) {
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

		/* TODO get the real path to osmosis from SettingsModel */
		builder.append("osmosis");

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
				// builder.append("--" + (stn != null ? stn : tn));
				
				/*
				 * TODO Inform osmosis developers that there is an error with
				 * --write-pbf --wb, so long we should use only the long version
				 * of task names.
				 */
				builder.append("--" + tn);

				/* write all parameters of the task */
				for (AbstractParameter parameter : function.getActiveTask()
						.getParameters()) {
					
					/* only add a parameter when there is not a default value assigned */
					if(!parameter.getValue().equals(parameter.getDefaultValue())) {
						builder.append(" " + parameter.getName() + "=\""
								+ parameter.getValue() + "\"");
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

	protected void setBreaklineSymbol(String symbol) {
		this.breaklineSymbol = symbol;
	}

	protected void setBreaklineCommand(String breaklineCommand) {
		this.breaklineCommand = breaklineCommand;
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
				"Sorry, but can't parse that, found a connection with only a connection in one dirction.");
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

	private void appendLineBreak(StringBuilder builder) {
		builder.append(" ");
		builder.append(breaklineSymbol);
		builder.append(breaklineCommand);
	}

}
