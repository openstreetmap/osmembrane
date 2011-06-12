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

import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import de.osmembrane.resources.Constants;

/**
 * A special pipeline link used only for the preview.
 * 
 * @see PipelineLink
 * 
 * @author tobias_kuhn
 * 
 */
public class PipelinePreviewLink extends PipelineLink {

    private static final long serialVersionUID = -4333235292752505928L;

    /**
     * The source of the current preview
     */
    protected PipelineFunction source;

    /**
     * The destination of the current preview
     */
    protected Point target;

    /**
     * Creates a new {@link PipelinePreviewLink}
     * 
     * @param panel
     *            the {@link PipelinePanel} to put the link on
     */
    public PipelinePreviewLink(PipelinePanel panel) {
        super(panel);
        this.line = new Line2D.Double();
        this.color = Constants.DEFAULT_FUNCTIONGROUP_COLOR;
    }

    /**
     * Regenerates the coordinates of the line, if destination has moved.
     * Furthermore assigns the size and location.
     */
    @Override
    public void regenerateLine() {
        Point2D leftAbs = new Point2D.Double(source.getX() + source.getWidth()
                / 2.0, source.getY() + source.getHeight() / 2.0);

        double basicX, basicY;
        double sizeX, sizeY;
        if (target.getX() < leftAbs.getX()) {
            // left
            basicX = source.getX() + source.getWidth();
            sizeX = Math.max(source.getWidth(), basicX - target.getX());

            basicX = basicX - sizeX;
        } else {
            // right
            basicX = source.getX();
            sizeX = Math.max(source.getWidth(), target.getX() - basicX);
        }
        if (target.getY() < leftAbs.getY()) {
            // above
            basicY = source.getY() + source.getHeight();
            sizeY = Math.max(source.getHeight(), basicY - target.getY());

            basicY = basicY - sizeY;
        } else {
            // below
            basicY = source.getY();
            sizeY = Math.max(source.getHeight(), target.getY() - basicY);
        }

        setLocation((int) basicX, (int) basicY);
        setSize((int) sizeX, (int) sizeY);

        Point2D left;
        Point2D right;

        double leftX, leftY;
        if (target.getX() < leftAbs.getX()) {
            // left
            leftX = getWidth() - source.getWidth() / 2.0;
        } else {
            // right
            leftX = source.getWidth() / 2.0;
        }
        if (target.getY() < leftAbs.getY()) {
            // above
            leftY = getHeight() - source.getHeight() / 2.0;
        } else {
            // below
            leftY = source.getHeight() / 2.0;
        }

        left = new Point2D.Double(leftX, leftY);
        right = new Point2D.Double(target.getX() - basicX, target.getY()
                - basicY);

        line.setLine(left, right);
        repaint();
    }

    /**
     * Sets the source to connectionSource and sets the location to the same.
     * 
     * @param connectionSource
     */
    public void setSource(PipelineFunction connectionSource) {
        this.source = connectionSource;
        double newX = connectionSource.getLocation().x + 0.5
                * connectionSource.getWidth();
        double newY = connectionSource.getLocation().y + 0.5
                * connectionSource.getHeight();
        setLocation(new Point((int) newX, (int) newY));
        setVisible(true);
    }

    /**
     * Sets the target to target
     * 
     * @param target
     */
    public void setTarget(Point target) {
        this.target = target;
    }

}
