package de.osmembrane.view.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import de.osmembrane.resources.Constants;
import de.osmembrane.view.ViewRegistry;

/**
 * A special pipeline link used only for the preview.
 * 
 * @see {@link PipelineLink}
 * 
 * @author whaite
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
	 * Furthermore assigns the size.
	 */
	@Override
	public void regenerateLine() {
		int newX = (int) Math.min(source.getX(), target.getX());
		int newY = (int) Math.min(source.getY(), target.getY());
		// TODO
		int newWidth = (int) Math.abs(target.getX() - source.getX());
		int newHeight = (int) Math.abs(target.getX() - source.getX());
		setLocation(newX, newY);
		setSize(new Dimension(newWidth, newHeight));
		
		Point2D left;
		Point2D right;

		Point offset = new Point(0, 0);//source.getWidth() / 2, source.getHeight() / 2);

		if (source.getX() < target.getX()) {
			// arrow goes like left -----> right
			if (source.getY() < target.getY()) {
				// left top to right bottom
				left = new Point2D.Double(offset.x, offset.y);
				right = new Point2D.Double(getWidth() - offset.x, getHeight()
						- offset.y);
			} else {
				// left bottom to right top
				left = new Point2D.Double(offset.x, getHeight() - offset.y);
				right = new Point2D.Double(getWidth() - offset.x, offset.y);
			}
		} else {
			// arrow goes like left <----- right
			if (source.getY() < target.getY()) {
				// right top to left bottom
				left = new Point2D.Double(offset.x, getHeight() - offset.y);
				right = new Point2D.Double(getWidth() - offset.x, offset.y);
			} else {
				// right bottom to left top
				left = new Point2D.Double(offset.x, offset.y);
				right = new Point2D.Double(getWidth() - offset.x, getHeight()
						- offset.y);
			}
		}

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
		double newX = connectionSource.getLocation().x + 0.5 * connectionSource.getWidth();
		double newY = connectionSource.getLocation().y + 0.5 * connectionSource.getHeight();
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