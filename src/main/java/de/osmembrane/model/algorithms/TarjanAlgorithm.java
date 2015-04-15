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

package de.osmembrane.model.algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;

import de.osmembrane.model.pipeline.AbstractConnector;
import de.osmembrane.model.pipeline.AbstractFunction;

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
    private Queue<AbstractFunction> notYetVisistedFunctions = new LinkedList<AbstractFunction>();

    /**
     * Set to check duplicate visited functions.
     */
    private Stack<AbstractFunction> nodeStack = new Stack<AbstractFunction>();

    /**
     * Global index for tarjan.
     */
    private int index;

    /**
     * Map for index.
     */
    private Map<AbstractFunction, Integer> nodeIndex = new HashMap<AbstractFunction, Integer>();

    /**
     * Map for lowlink.
     */
    private Map<AbstractFunction, Integer> nodeLowlink = new HashMap<AbstractFunction, Integer>();

    /**
     * strongly connected components.
     */

    List<List<AbstractFunction>> scc = new ArrayList<List<AbstractFunction>>();

    /**
     * Creates a new instance with given functions.
     * 
     * @param functions
     *            fist of functions
     */
    public TarjanAlgorithm(List<AbstractFunction> functions) {
        this.functions = functions;
    }

    /**
     * Checks if a Graph has a loop.
     */
    public void run() {
        reset();

        while (notYetVisistedFunctions.size() > 0) {
            AbstractFunction node = notYetVisistedFunctions.peek();
            if (nodeIndex.get(node) == null) {
                tarjan(node);
            }
        }
    }

    /**
     * Returns the SCC in the graph.
     * 
     * @return scc of given graph
     */
    public List<List<AbstractFunction>> getSCC() {
        return scc;
    }

    /**
     * Returns all SCCs with at least a given number of entries.
     * 
     * @param size
     *            minimum size for SCC
     * @return all SCCs with at least "size" entries
     */
    public List<List<AbstractFunction>> getSCC(int size) {
        List<List<AbstractFunction>> returnList = new ArrayList<List<AbstractFunction>>();
        for (List<AbstractFunction> scc : this.scc) {
            if (scc.size() >= size) {
                returnList.add(scc);
            }
        }
        return returnList;
    }

    /**
     * The tarjan algorithm.
     * 
     * @param node
     *            Node with which should be started.
     */
    private void tarjan(AbstractFunction node) {
        notYetVisistedFunctions.remove(node);
        nodeIndex.put(node, index);
        nodeLowlink.put(node, index);
        index++;

        nodeStack.push(node);

        for (AbstractConnector edges : node.getOutConnectors()) {
            for (AbstractConnector edge : edges.getConnections()) {
                AbstractFunction newNode = edge.getParent();
                if (nodeIndex.get(newNode) == null) {
                    tarjan(newNode);
                    nodeLowlink.put(node, llMin(node, newNode));
                } else if (nodeStack.contains(newNode)) {
                    nodeLowlink.put(node, liMin(node, newNode));
                }
            }
        }

        if (nodeLowlink.get(node).equals(nodeIndex.get(node))) {
            /**
             * There seems to be a strongly connected component in the graph,
             * check the size.
             */
            int sccSize = 0;
            List<AbstractFunction> stackNodes = new ArrayList<AbstractFunction>();
            do {
                stackNodes.add(nodeStack.pop());
                sccSize++;
            } while (!stackNodes.contains(node));

            scc.add(stackNodes);
        }
    }

    /**
     * minimum from lowlevel and lowlevel
     */
    private Integer llMin(AbstractFunction node1, AbstractFunction node2) {
        if (nodeLowlink.get(node1).intValue() < nodeLowlink.get(node2)
                .intValue()) {
            return nodeLowlink.get(node1);
        } else {
            return nodeLowlink.get(node2);
        }
    }

    /**
     * minumum from lowlevel and index
     */
    private Integer liMin(AbstractFunction node1, AbstractFunction node2) {
        if (nodeLowlink.get(node1).intValue() < nodeIndex.get(node2).intValue()) {
            return nodeLowlink.get(node1);
        } else {
            return nodeIndex.get(node2);
        }
    }

    private void reset() {
        nodeIndex.clear();
        nodeLowlink.clear();
        notYetVisistedFunctions.clear();
        scc.clear();
        index = 0;

        for (AbstractFunction function : functions) {
            notYetVisistedFunctions.add(function);
        }
        nodeStack.clear();
    }
}
