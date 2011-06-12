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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;

import de.osmembrane.model.pipeline.AbstractConnector;
import de.osmembrane.model.pipeline.Connector;
import de.osmembrane.resources.Constants;

/**
 * Represents a function connector, i.e. {@link AbstractConnector} in the View
 * and the pipeline ({@link PipelinePanel}).
 * 
 * @author tobias_kuhn
 * 
 */
public class PipelineConnector extends DisplayTemplatePanel {

    private static final long serialVersionUID = -3777471525969957175L;

    /**
     * The {@link ImageIcon} resource that keeps an image template used to
     * prerender the actual image that will be drawn on this connector
     */
    protected static ImageIcon displayTemplate = new ImageIcon(
            PipelineConnector.class
                    .getResource("/de/osmembrane/resources/images/connector.png"));

    /**
     * The model {@link Connector} associated with this connector
     */
    private AbstractConnector modelConnector;

    /**
     * The parent {@link PipelineFunction} associated with this connector
     */
    private PipelineFunction parentFunction;

    /**
     * {@link PipelinePanel} this connector is drawn on.
     */
    private PipelinePanel pipeline;

    /**
     * The image to use for display
     */
    private Image display;

    /**
     * Whether this is an outpipes connector (true) or an inpipes connector
     * (false)
     */
    private boolean isOutpipes;

    /**
     * the id this connector has in a group of connectors. one group of
     * connectors is all the in- or out-connectors of a function
     */
    private int id;

    /**
     * amount of the connectors in the group of connectors. one group of
     * connectors is all the in- or out-connectors of a function
     */
    private int amount;

    /**
     * list of {@link PipelineLink}s going from this connector to other
     * connectors
     */
    private List<PipelineLink> outLinks;

    /**
     * lists of {@link PipelineLink}s coming in from other connectors to this
     * connector
     */
    private List<PipelineLink> inLinks;

    /**
     * Creates a new connector for a model {@link Connector} on a
     * {@link PipelineFunction}.
     * 
     * @param modelConnector
     *            connector in the model which is represented
     * @param parentFunction
     *            the function this connector belongs to
     * @param pipeline
     *            Pipeline this connector is drawn on
     * @param isOutpipes
     *            true, if this is an outpipes connector, false if this is an
     *            inpipes connector
     * @param id
     *            unique id for this connector starting by 0 < ofCount
     * @param amount
     *            the amount of this kind of connectors (out or in) on the
     *            specific function they are created for
     */
    public PipelineConnector(AbstractConnector modelConnector,
            final PipelineFunction parentFunction,
            final PipelinePanel pipeline, boolean isOutpipes, int id, int amount) {
        this.modelConnector = modelConnector;
        setPreferredSize(new Dimension(
                (int) (displayTemplate.getIconWidth() * Constants.DEFAULT_SIZE_FACTOR),
                (int) (displayTemplate.getIconHeight() * Constants.DEFAULT_SIZE_FACTOR)));
        this.pipeline = pipeline;
        this.parentFunction = parentFunction;

        this.isOutpipes = isOutpipes;
        this.id = id;
        this.amount = amount;

        this.outLinks = new ArrayList<PipelineLink>();
        this.inLinks = new ArrayList<PipelineLink>();

        this.addMouseListener(new MouseListener() {

            @Override
            public void mouseReleased(MouseEvent e) {
                MouseEvent pipelineEvent = SwingUtilities.convertMouseEvent(
                        PipelineConnector.this, e, pipeline);
                if (pipeline.getActiveTool() != Tool.DEFAULT_MAGIC_TOOL) {
                    pipeline.dispatchEvent(pipelineEvent);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                MouseEvent pipelineEvent = SwingUtilities.convertMouseEvent(
                        PipelineConnector.this, e, pipeline);

                if (pipeline.getActiveTool() == Tool.DEFAULT_MAGIC_TOOL) {
                    pipeline.connect(parentFunction);
                } else {
                    pipeline.dispatchEvent(pipelineEvent);
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

        this.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseMoved(MouseEvent e) {
                MouseEvent pipelineEvent = SwingUtilities.convertMouseEvent(
                        PipelineConnector.this, e, pipeline);
                pipeline.dispatchEvent(pipelineEvent);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                MouseEvent pipelineEvent = SwingUtilities.convertMouseEvent(
                        PipelineConnector.this, e, pipeline);

                switch (pipeline.getActiveTool()) {
                case VIEW_TOOL:
                    pipeline.dispatchEvent(pipelineEvent);
                    break;
                }
            }
        });

        /*
         * use DisplayTemplatePanel prerender capabilites in this case we store
         * it for *CONNECTOR TYPES ONLY* if you ever want images/connector,
         * change this
         */
        List<Image> prerender = DisplayTemplatePanel
                .givePrerender(modelConnector.getType());
        if (prerender != null) {
            // prender exists, use it
            display = prerender.get(0);
        } else {
            // find right color

            Color color = modelConnector.getType().getColor();

            // create it
            display = DisplayTemplatePanel.prerenderDisplay(
                    modelConnector.getType(), displayTemplate, color, null);
        }

        this.setOpaque(false);

        /*
         * do not create outLinks here already, we don't know if we are done
         * with all other connectors yet
         */
    }

    /**
     * Generates all outLinks that the model maintains
     */
    public void generateLinksFromModel() {
        if (isOutpipes) {
            createLinks();
        }
    }

    /**
     * Creates all the outflowing {@link PipelineLink}s from this connector.
     */
    private void createLinks() {
        for (AbstractConnector ac : modelConnector.getConnections()) {
            PipelineConnector toCon = pipeline.findConnector(ac);
            PipelineLink pl = new PipelineLink(pipeline, this, toCon);

            // out link here
            outLinks.add(pl);
            // in link there
            toCon.inLinks.add(pl);
        }
    }

    /**
     * Arranges all {@link PipelineLink}s to conform to this connector
     */
    public void arrangeLinks() {
        for (PipelineLink pl : outLinks) {
            PipelineConnector dest = pl.getLinkDestination();

            /* set size, set location */
            if (this.getX() < dest.getX()) {
                // arrow goes like ----->
                if (this.getY() < dest.getY()) {
                    // left top to right bottom
                    pl.setLocation(this.getX(), this.getY());
                    pl.setSize(dest.getWidth() + dest.getX() - this.getX(),
                            dest.getHeight() + dest.getY() - this.getY());
                } else {
                    // left bottom to right top
                    pl.setLocation(this.getX(), dest.getY());
                    pl.setSize(dest.getWidth() + dest.getX() - this.getX(),
                            this.getHeight() + this.getY() - dest.getY());
                }
            } else {
                // arrow goes like <-----
                if (this.getY() < dest.getY()) {
                    // right top to left bottom
                    pl.setLocation(dest.getX(), this.getY());
                    pl.setSize(this.getWidth() + this.getX() - dest.getX(),
                            dest.getHeight() + dest.getY() - this.getY());
                } else {
                    // right bottom to left top
                    pl.setLocation(dest.getX(), dest.getY());
                    pl.setSize(this.getWidth() + this.getX() - dest.getX(),
                            this.getHeight() + this.getY() - dest.getY());
                }
            }

            pl.regenerateLine();
        }

    }

    @Override
    protected void paintComponent(Graphics g) {
        g.drawImage(display, 0, 0, getWidth(), getHeight(), this);
    }

    /**
     * @return the model {@link Connector} represented by this
     */
    public AbstractConnector getModelConnector() {
        return this.modelConnector;
    }

    /**
     * @return whether this connector represents in pipes or out pipes
     */
    public boolean isOutpipes() {
        return this.isOutpipes;
    }

    /**
     * @return the id this connector has in a group of connectors. one group of
     *         connectors is all the in- or out-connectors of a function
     */
    public int getId() {
        return this.id;
    }

    /**
     * @return amount of the connectors in the group of connectors. one group of
     *         connectors is all the in- or out-connectors of a function
     */
    public int getAmount() {
        return this.amount;
    }

    /**
     * @return the list of {@link PipelineLink}s flowing out from this connector
     */
    public List<PipelineLink> getOutLinks() {
        return this.outLinks;
    }

    /**
     * @return the list of {@link PipelineLink}s coming in to this connector
     */
    public List<PipelineLink> getInLinks() {
        return this.inLinks;
    }

    /**
     * Creates a new {@link PipelineLink} to toConnector and returns it.
     * 
     * @param toConnector
     *            the target of the connection from here
     * @return the newly created link
     */
    public PipelineLink addLinkTo(PipelineConnector toConnector) {
        PipelineLink pl = new PipelineLink(pipeline, this, toConnector);
        outLinks.add(pl);
        toConnector.inLinks.add(pl);
        return pl;
    }

    /**
     * Removes the {@link PipelineLink} to toConnector and returns it.
     * 
     * @param toConnector
     *            the target of the connection from here
     * @return the removed link, or null if none found
     */
    public PipelineLink removeLinkTo(PipelineConnector toConnector) {
        PipelineLink result = null;

        // out from here
        for (int i = 0; i < outLinks.size(); i++) {
            result = outLinks.get(i);

            if (result.doesLink(this, toConnector)) {
                outLinks.remove(i);
                break;
            }
        }

        // in to there
        for (int i = 0; i < toConnector.inLinks.size(); i++) {
            if (toConnector.inLinks.get(i).equals(result)) {
                toConnector.inLinks.remove(i);
                break;
            }
        }

        return result;
    }

    /**
     * @return the parent {@link PipelineFunction} this connector belongs to
     */
    public PipelineFunction getParentFunction() {
        return this.parentFunction;
    }

}
