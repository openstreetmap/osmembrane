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

public class CommandlineParser implements IParser {

	protected String breaklineSymbol = "<linebreak>\n";
	
	@Override
	public List<AbstractFunction> parseString(String input)
			throws ParseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String parsePipeline(List<AbstractFunction> pipeline) {
		Queue<AbstractFunction> functionQueue = new LinkedList<AbstractFunction>();
		List<AbstractFunction> usedFunctions = new ArrayList<AbstractFunction>();
		Map<AbstractConnector, Integer> connectorMap = new HashMap<AbstractConnector, Integer>();
		StringBuilder builder = new StringBuilder();
		
		int pipeIndex = 0;
		
		for(AbstractFunction function : pipeline) {
			functionQueue.add(function);
		}
		
		while(!functionQueue.isEmpty()) {
			AbstractFunction function = functionQueue.poll();
			boolean addable = true;
			for(AbstractConnector inConnector : function.getInConnectors()) {
				for(AbstractConnector sourceConnector : inConnector.getConnections()) {
					AbstractFunction sourceFunction = sourceConnector.getParent();
					if(!usedFunctions.contains(sourceFunction)) {
						addable = false;
						break;
					}
				}
				if(!addable) {
					break;
				}
			}
			
			if(addable) {
				usedFunctions.add(function);
				
				if(builder.length() > 0) {
					builder.append(breaklineSymbol);
				}
				
				String stn = function.getActiveTask().getShortName();
				String tn = function.getActiveTask().getName();
				
				builder.append("--" + (stn != null ? stn : tn));
				
				for(AbstractParameter parameter : function.getActiveTask().getParameters()) {
					builder.append(" " + parameter.getName() + "='" + parameter.getValue() + "'");
				}
				
				for(AbstractConnector connector : function.getInConnectors()) {
					for(AbstractConnector otherConnector : connector.getConnections()) {
						int offset = getConnectorOffset(connector, otherConnector);
						builder.append(" inPipe." + connector.getConnectorIndex() + "=" + (connectorMap.get(otherConnector)+offset));
					}
				}
				
				StringBuilder teeBuilder = new StringBuilder();
				for(AbstractConnector connector : function.getOutConnectors()) {
					
					/* add to the index + 1, 'cause the first tee-out-connector
					 * has function.connector + 1 as pipe key.
					 */
					connectorMap.put(connector, (pipeIndex+1));
					builder.append(" outPipe." + connector.getConnectorIndex() + "=" + pipeIndex);
						
					/* Add a tee, 'cause more than one connection is attached. */
					if(connector.getConnections().length > 1) {
						teeBuilder.append(breaklineSymbol);
						teeBuilder.append("--tee " + connector.getConnections().length + " inPipe.0=" + pipeIndex);
						for(int i = 0; i < connector.getConnections().length; i++) {
							pipeIndex++;
							/* append a out-pipe for the tee (outPipe.Index=pipeIndex */
							teeBuilder.append(" outPipe." + i + "=" + pipeIndex);
						}
					}
				}
				
				builder.append(teeBuilder);
			} else {
				functionQueue.add(function);
			}
		}
		
		return builder.toString();
	}
	
	private int getConnectorOffset(AbstractConnector connector, AbstractConnector otherConnector) {
		AbstractConnector[] connections = otherConnector.getConnections();
		for(int i = 0; i < connections.length; i++) {
			if(connections[i] == connector) {
				return i;
			}
		}
		
		/* should never! happen! */
		throw new RuntimeException("Sorry, but can't parse that, found a connection with only a connection in one dirction.");
	}
	
	protected void setBreaklineSymbol(String symbol) {
		this.breaklineSymbol = symbol;
	}

}
