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
import java.util.List;

import de.osmembrane.model.ModelProxy;
import de.osmembrane.model.pipeline.AbstractConnector;
import de.osmembrane.model.pipeline.AbstractFunction;
import de.osmembrane.model.settings.SettingType;
import de.osmembrane.resources.Constants;

/**
 * The graph planarizer tries to optimize the given graph.
 * 
 * @author jakob_jarosch
 */
public class GraphPlanarizer {

    private List<AbstractFunction> functions;

    private double pipelineRasterSize;
    private double X_OFFSET;
    private double Y_OFFSET;

    /**
     * Creates an new planarizer.
     * 
     * @param functions
     *            which should be planarized
     */
    public GraphPlanarizer(List<AbstractFunction> functions) {
        this.functions = functions;
    }

    /**
     * Execute the planarization.
     */
    public void planarize() {
        resetAllFunctions();
        calculateGridSize();

        List<AbstractFunction> primaryFunctions = getPrimaryFunctions();

        /*
         * set the xOffsetfactor to -1.0 'cause orderFunction will add 1.0 in
         * the first step
         */
        double xOffsetFactor = -1.0;
        double yOffsetFactor = 0.0;

        for (AbstractFunction function : primaryFunctions) {
            yOffsetFactor = orderFunctions(function, xOffsetFactor,
                    yOffsetFactor);
            yOffsetFactor++;
        }
    }

    /**
     * Resets all functions to the (0.0, 0.0) coordinate.
     */
    private void resetAllFunctions() {
        for (AbstractFunction function : functions) {
            function.getCoordinate().setLocation(0.0, 0.0);
        }
    }

    /**
     * Returns all functions with no connections at their inConnectors.
     * 
     * @return all functions with no connections at their inConnectors
     */
    private List<AbstractFunction> getPrimaryFunctions() {
        List<AbstractFunction> primaryFunctions = new ArrayList<AbstractFunction>();
        for (AbstractFunction function : functions) {
            boolean foundConnection = false;
            for (AbstractConnector connector : function.getInConnectors()) {
                if (connector.getConnections().length > 0) {
                    /* found a inConnection, do not add as a primary one */
                    foundConnection = true;
                }
            }
            if (!foundConnection) {
                primaryFunctions.add(function);
            }
        }
        return primaryFunctions;
    }

    /**
     * Orders all functions connected to this function.
     * 
     * @return returns the required space in height
     */
    private double orderFunctions(AbstractFunction function,
            double xOffsetFactor, double yOffsetFactor) {

        /* move in this recursision step one position right */
        xOffsetFactor++;

        /* Set the coordinate of the functions */
        function.getUnrasteredCoordinate().setLocation(
                X_OFFSET * xOffsetFactor, Y_OFFSET * yOffsetFactor);

        boolean moreThanOneConnection = false;
        for (AbstractConnector connector : function.getOutConnectors()) {
            for (AbstractConnector connector2 : connector.getConnections()) {
                /* move all functions step by step one position lower */
                if (moreThanOneConnection) {
                    yOffsetFactor++;
                } else {
                    moreThanOneConnection = true;
                }
                yOffsetFactor = orderFunctions(connector2.getParent(),
                        xOffsetFactor, yOffsetFactor);
            }
        }

        return yOffsetFactor;
    }

    /**
     * Calculates the grid size.
     */
    private void calculateGridSize() {
        pipelineRasterSize = Math.max(1.0,
                new Double((Integer) ModelProxy.getInstance().getSettings()
                        .getValue(SettingType.PIPELINE_RASTER_SIZE)));
        X_OFFSET = 0.0;
        Y_OFFSET = 0.0;

        while (X_OFFSET < Constants.PIPELINE_FUNCTION_MINIMAL_X_DISTANCE) {
            X_OFFSET += pipelineRasterSize;
        }

        while (Y_OFFSET < Constants.PIPELINE_FUNCTION_MINIMAL_Y_DISTANCE) {
            Y_OFFSET += pipelineRasterSize;
        }
    }
}
