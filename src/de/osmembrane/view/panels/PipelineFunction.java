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

package de.osmembrane.view.panels;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import de.osmembrane.model.pipeline.AbstractConnector;
import de.osmembrane.model.pipeline.AbstractFunction;
import de.osmembrane.model.pipeline.Connector;
import de.osmembrane.model.pipeline.Function;

/**
 * The pipeline function, i.e. the visual representation of a model
 * {@link Function} that is actually drawn in the pipeline. Note, the functions
 * in the {@link LibraryPanel} and the one being dragged on the
 * {@link PipelinePanel} are just {@link LibraryFunction}.
 * 
 * @author tobias_kuhn
 * 
 */
public class PipelineFunction extends LibraryFunction {

    private static final long serialVersionUID = -7573627124702293974L;

    /**
     * The minimum amount a {@link PipelineFunction} must be dragged to activate
     * the drop event.
     */
    protected static final double PIPELINE_FUNCTION_MIN_DRAG_DISTANCE = 8.0;

    /**
     * The {@link Function} in the model that is represented by this
     * {@link PipelineFunction}
     */
    private AbstractFunction modelFunction;

    /**
     * {@link PipelinePanel} to add this to
     */
    private PipelinePanel pipeline;

    /**
     * List of {@link PipelineConnector}s this functions has
     */
    private List<PipelineConnector> connectors;

    /**
     * Creates a new {@link PipelineFunction} from an {@link AbstractFunction}
     * out of the model
     * 
     * @param modelFunction
     *            the function out of the model
     * @param pipeline
     *            the {@link PipelinePanel} to add it to
     */
    public PipelineFunction(AbstractFunction modelFunction,
            final PipelinePanel pipeline) {
        // pretend this is a prototype
        super(pipeline, modelFunction, false);
        this.modelFunction = modelFunction;
        this.pipeline = pipeline;
        this.connectors = new ArrayList<PipelineConnector>();

        createConnectors(modelFunction.getInConnectors(), false);
        createConnectors(modelFunction.getOutConnectors(), true);

        /*
         * all functions are required to dispatch back to the pipeline,
         * depending on tool
         */
        addMouseListener(new MouseListener() {

            @Override
            public void mouseReleased(MouseEvent e) {
                MouseEvent pipelineEvent = SwingUtilities.convertMouseEvent(
                        PipelineFunction.this, e, pipeline);

                switch (pipeline.getActiveTool()) {
                case DEFAULT_MAGIC_TOOL:
                case VIEW_TOOL:
                case SELECTION_TOOL:
                    pipeline.dispatchEvent(pipelineEvent);
                    break;
                case CONNECTION_TOOL:
                    break;
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                MouseEvent pipelineEvent = SwingUtilities.convertMouseEvent(
                        PipelineFunction.this, e, pipeline);

                switch (pipeline.getActiveTool()) {
                case DEFAULT_MAGIC_TOOL:
                case SELECTION_TOOL:
                    pipeline.selected(PipelineFunction.this);
                    pipeline.setDraggingFrom(pipelineEvent.getPoint());
                    break;
                case VIEW_TOOL:
                    pipeline.dispatchEvent(pipelineEvent);
                    break;
                case CONNECTION_TOOL:
                    pipeline.connect(PipelineFunction.this);
                    break;
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseClicked(MouseEvent e) {
            }
        });

        addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseMoved(MouseEvent e) {
                MouseEvent pipelineEvent = SwingUtilities.convertMouseEvent(
                        PipelineFunction.this, e, pipeline);
                pipeline.dispatchEvent(pipelineEvent);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                MouseEvent pipelineEvent = SwingUtilities.convertMouseEvent(
                        PipelineFunction.this, e, pipeline);

                switch (pipeline.getActiveTool()) {
                case DEFAULT_MAGIC_TOOL:
                case VIEW_TOOL:
                case SELECTION_TOOL:
                    pipeline.dispatchEvent(pipelineEvent);
                    break;
                }
            }
        });
    }

    /**
     * Creates & adds {@link PipelineConnector}s from connectorList for this
     * function
     * 
     * @param connectorList
     *            all the {@link Connector}s that shall be created & added
     * @param areOut
     *            whether the connectors are out or in pipes
     */
    private void createConnectors(AbstractConnector[] connectorList,
            boolean areOut) {
        int size = connectorList.length;
        for (int i = 0; i < size; i++) {
            PipelineConnector pc = new PipelineConnector(connectorList[i],
                    this, pipeline, areOut, i, size);
            connectors.add(pc);
        }
    }

    /**
     * Arranges the {@link PipelineConnector}s, if necessary
     */
    public void arrangeConnectors() {
        Point funcTopLeft = this.getLocation();

        for (PipelineConnector pc : connectors) {
            // standard stuff per connector
            Point offset = pipeline.objToWindowDelta(new Point2D.Double(-pc
                    .getPreferredSize().width / 2.0, 0));
            Point size = pipeline.objToWindowDelta(new Point(pc
                    .getPreferredSize().width, pc.getPreferredSize().height));

            // actual new position
            int newX = pc.isOutpipes() ? getWidth() : 0;
            double startY = ((getHeight() - pc.getAmount() * size.y) / 2.0);
            double offsetThisY = (pc.getId() * size.y);

            Point newPosition = new Point(newX, (int) (startY + offsetThisY));
            pc.setLocation(funcTopLeft.x + newPosition.x + offset.x,
                    funcTopLeft.y + newPosition.y + offset.y);
            pc.setSize(size.x, size.y);

            // cannot arrange links here, cause some connectors probably aren't
            // arranged yet
            pc.repaint();
        }
    }

    /**
     * Arranges the {@link PipelineLink}s, if necessary
     */
    public void arrangeLinks() {
        for (PipelineConnector pc : connectors) {
            pc.arrangeLinks();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        highlighted = this.equals(pipeline.getSelected());

        super.paintComponent(g);

        g.setFont(g.getFont().deriveFont(Font.PLAIN)
                .deriveFont((int) (g.getFont().getSize() * 0.9)));
        printCenteredString(g, modelFunction.getActiveTask().getName(), 0,
                0 + 0.33 * getHeight());
    }

    /**
     * @return the model {@link Function}
     */
    public AbstractFunction getModelFunction() {
        return this.modelFunction;
    }

    /**
     * @return the location this {@link PipelineFunction}'s model
     *         {@link Function} has saved
     */
    public Point2D getModelLocation() {
        return this.modelFunction.getCoordinate();
    }

    /**
     * @return the {@link PipelineConnector}s this function has
     */
    public List<PipelineConnector> getConnectors() {
        return connectors;
    }

    /**
     * @return all the outgoing {@link PipelineLink}s from all the
     *         {@link PipelineConnector}s
     */
    public List<PipelineLink> getAllOutLinks() {
        ArrayList<PipelineLink> result = new ArrayList<PipelineLink>();

        for (PipelineConnector pc : connectors) {
            result.addAll(pc.getOutLinks());
        }

        return result;
    }

}
