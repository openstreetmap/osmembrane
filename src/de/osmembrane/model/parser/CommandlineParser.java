package de.osmembrane.model.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import de.osmembrane.model.pipeline.AbstractConnector;
import de.osmembrane.model.pipeline.AbstractFunction;
import de.osmembrane.model.pipeline.AbstractParameter;
import de.osmembrane.model.pipeline.ConnectorType;

public class CommandlineParser implements IParser {

	protected String breaklineSymbol = "<linebreak>\n";

	@Override
	public List<AbstractFunction> parseString(String input)
			throws ParseException {
		
		input = input.replace(breaklineSymbol, " ");
		System.out.println(input);
		
		return null;
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

				builder.append(breaklineSymbol);

				/*
				 * get the shortName and the name from the activeTask in the
				 * function
				 */
				String stn = function.getActiveTask().getShortName();
				String tn = function.getActiveTask().getName();

				/* write the task(-short)-name */
				builder.append("--" + (stn != null ? stn : tn));

				/* write all parameters of the task */
				for (AbstractParameter parameter : function.getActiveTask()
						.getParameters()) {
					builder.append(" " + parameter.getName() + "='"
							+ parameter.getValue() + "'");
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
						 * add to the index + 1, 'cause the first tee-out-connector
						 * has function.connector + 1 as pipe key.
						 */
						connectorMap.put(connector, (pipeIndex + 1));
						
						teeBuilder.append(breaklineSymbol);
						
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

	protected void setBreaklineSymbol(String symbol) {
		this.breaklineSymbol = symbol;
	}

}
