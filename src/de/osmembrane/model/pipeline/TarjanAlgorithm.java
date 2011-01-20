package de.osmembrane.model.pipeline;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;


/**
 * Checks if functions does create a loop.
 * 
 * @author jakob_jarosch
 */
public class TarjanAlgorithm {
	
	/**
	 * External function-list.
	 */
	private List<AbstractFunction> functions;
	
	/**
	 * Internal store for functions.
	 */
	private Set<AbstractFunction> notYetVisistedFunctions = new HashSet<AbstractFunction>();
	
	/**
	 * Set to check duplicate visited functions.
	 */
	private Stack<AbstractFunction> nodeStack = new Stack<AbstractFunction>();
	
	private int index;
	
	private Map<AbstractFunction, Integer> nodeIndex = new HashMap<AbstractFunction, Integer>();
	private Map<AbstractFunction, Integer> nodeLowlink = new HashMap<AbstractFunction, Integer>();
	
	/**
	 * Creates a new instance with given functions.
	 * 
	 * @param functions fist of functions
	 */
	public TarjanAlgorithm(List<AbstractFunction> functions) {
		this.functions = functions;
	}

	/**
	 * Checks if a Graph has a loop.
	 * 
	 * @return true if a loop exists, otherwise false
	 */
	public boolean hasLoop() {
		reset();
		
		for(AbstractFunction node : notYetVisistedFunctions) {
			if(nodeIndex.get(node) == null) {
				try {
					tarjan(node);
				} catch (FoundSCCException e) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	private void tarjan(AbstractFunction node) throws FoundSCCException {
		nodeIndex.put(node, index);
		nodeLowlink.put(node, index);
		index++;
		
		nodeStack.push(node);
		
		for(AbstractConnector edges : node.getOutConnectors()) {
			for(AbstractConnector edge : edges.getConnections()) {
				AbstractFunction newNode = edge.getParent();
				if(nodeIndex.get(newNode) == null) {
					tarjan(newNode);
					nodeLowlink.put(node, llMin(node, newNode));
				} else if (nodeStack.contains(newNode)) {
					nodeLowlink.put(node, liMin(node, newNode));
				}
			}
		}
		for(AbstractConnector edges : node.getInConnectors()) {
			for(AbstractConnector edge : edges.getConnections()) {
				AbstractFunction newNode = edge.getParent();
				if(nodeIndex.get(newNode) == null) {
					tarjan(newNode);
					nodeLowlink.put(node, llMin(node, newNode));
				} else if (nodeStack.contains(newNode)) {
					nodeLowlink.put(node, liMin(node, newNode));
				}
			}
		}
		
		if(nodeLowlink.get(node) == nodeIndex.get(node)) {
			throw new FoundSCCException();
		}
	}
	
	/**
	 * minimum from lowlevel and lowlevel
	 */
	private Integer llMin(AbstractFunction node1, AbstractFunction node2) {
		if (nodeLowlink.get(node1) < nodeLowlink.get(node2)) {
			return nodeLowlink.get(node1);
		} else {
			return nodeLowlink.get(node2);
		}
	}
	
	/**
	 * minumum from lowlevel and index
	 */
	private Integer liMin(AbstractFunction node1, AbstractFunction node2) {
		if (nodeLowlink.get(node1) < nodeIndex.get(node2)) {
			return nodeLowlink.get(node1);
		} else {
			return nodeLowlink.get(node2);
		}
	}

	private void reset() {
		nodeIndex.clear();
		nodeLowlink.clear();
		notYetVisistedFunctions.clear();
		
		for (AbstractFunction function : functions) {
			notYetVisistedFunctions.add(function);
		}
		nodeStack.clear();
	}
	

}

class FoundSCCException extends Exception {
	
	private static final long serialVersionUID = 2011011617270001L;
	
}
